package frc.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.GZFileMaker.ValidFileExtensions;

public class TalonSRXChecker {

    public static class Current {
        private final double mCurrent;
        private boolean mFail = false;

        public Current(double current) {
            this.mCurrent = current;
        }

        public void setFail() {
            this.mFail = true;
        }

        public boolean getFail() {
            return this.mFail;
        }

        public Double getCurrent() {
            return this.mCurrent;
        }
    }

    public static class TalonGroup {
        private String mName;
        private double averageForwardAmperage;
        private double averageReverseAmperage;

        private GZSubsystem mSubsystem;

        private ArrayList<GZSRX> talons = null;
        private ArrayList<Current> forwardCurrents = null;
        private ArrayList<Current> reverseCurrents = null;

        private CheckerConfig mCheckerConfig;

        public TalonGroup(GZSubsystem subsystem, String name, List<GZSRX> talons, CheckerConfig config) {
            this.talons = (ArrayList) talons;
            this.mName = name;
            this.mSubsystem = subsystem;
            this.mCheckerConfig = config;
        }

        public boolean hasFail()
        {
            boolean hasFail = false;
            
            for (Current c : forwardCurrents)
                hasFail |= c.getFail();

            for (Current c : reverseCurrents)
                hasFail |= c.getFail();

            return hasFail;
        }

        public Double getAverageForwardAmperage() {
            return averageForwardAmperage;
        }

        public void setAverageForwardAmperage(double averageForwardAmperage) {
            this.averageForwardAmperage = averageForwardAmperage;
        }

        public Double getAverageReverseAmperage() {
            return averageReverseAmperage;
        }

        public void setAverageReverseAmperage(double averageReverseAmperage) {
            this.averageReverseAmperage = averageReverseAmperage;
        }

        public CheckerConfig getConfig() {
            return this.mCheckerConfig;
        }

        public String getName() {
            return this.mName;
        }

        public GZSubsystem getSubsystem() {
            return this.mSubsystem;
        }

        public String getSubsystemName() {
            return getSubsystem().getClass().getSimpleName();
        }

        public ArrayList<GZSRX> getTalons() {
            return talons;
        }

        public ArrayList<Current> getForwardCurrents() {
            return forwardCurrents;
        }

        public ArrayList<Current> getReverseCurrents() {
            return reverseCurrents;
        }
    }

    public static class CheckerConfig {
        public double mCurrentFloor = 0;
        public double mCurrentEpsilon = 0;

        public double mRunTimeSec = 0;
        public double mWaitTimeSec = 0;
        public double mRunOutputPercentage = 0.0;

        public CheckerConfig(double currentFloor, double currentEpsilon, double runTimeSec, double waitTimeSec,
                double outputPercentage) {
            this.mCurrentFloor = currentFloor;
            this.mCurrentEpsilon = currentEpsilon;
            this.mRunTimeSec = runTimeSec;
            this.mWaitTimeSec = waitTimeSec;
            this.mRunOutputPercentage = outputPercentage;
        }

        public String configAsString() {
            String retval = "";

            retval += "Current floor: " + mCurrentFloor + "\n";
            retval += "Current epsilon: " + mCurrentEpsilon + "\n";
            retval += "Run time (seconds): " + mRunTimeSec + "\n";
            retval += "Wait time (seconds): " + mWaitTimeSec + "\n";
            retval += "Output percentage: " + mRunOutputPercentage + "\n";

            return retval;
        }

        public void print() {
            System.out.println(toString());
        }
    }

    private static class StoredTalonSRXConfiguration {
        public ControlMode mMode;
        public double mSetValue;
    }

    private static TalonSRXChecker mInstance = null;

    public static TalonSRXChecker getInstance() {
        if (mInstance == null)
            mInstance = new TalonSRXChecker();
        return mInstance;
    }

    private TalonSRXChecker() {
    }

    private Map<GZSubsystem, ArrayList<TalonGroup>> subsystemMap = new HashMap<GZSubsystem, ArrayList<TalonGroup>>();

    public void addTalonGroup(TalonGroup group) {
        if (!subsystemMap.containsKey(group.getSubsystem()))
            subsystemMap.put(group.getSubsystem(), new ArrayList<TalonGroup>());

        subsystemMap.get(group.getSubsystem()).add(group);
    }

    public void checkTalons() {
        boolean failure = false;

        ArrayList<TalonGroup> talonGroups;
        for (GZSubsystem s : subsystemMap.keySet()) {
            talonGroups = subsystemMap.get(s);

            for (TalonGroup g : talonGroups) {
                ArrayList<GZSRX> talonsToCheck = g.getTalons();

                System.out.println(
                        "Checking subsystem " + g.getSubsystemName() + " for " + talonsToCheck.size() + " talons.");
                ArrayList<StoredTalonSRXConfiguration> storedConfigurations = new ArrayList<>();

                // Dont allow other methods to control these talons
                for (GZSRX t : talonsToCheck)
                    t.setBeingChecked(true);

                // Record previous configuration for all talons.
                for (GZSRX t : talonsToCheck) {
                    StoredTalonSRXConfiguration configuration = new StoredTalonSRXConfiguration();
                    configuration.mMode = t.getControlMode();
                    configuration.mSetValue = t.getLastSetValue();
                    storedConfigurations.add(configuration);
                    // Now set to disabled.
                    t.set(ControlMode.PercentOutput, 0.0, true);
                }

                boolean forward = true;
                for (int i = 0; i < 2; i++) {
                    ArrayList<Current> currents;

                    for (GZSRX individualTalonToCheck : talonsToCheck) {
                        System.out.println("Checking: " + individualTalonToCheck.getGZName()
                                + (forward ? " forwards" : " reverse"));

                        currents = (forward ? g.getForwardCurrents() : g.getForwardCurrents());

                        individualTalonToCheck.set(ControlMode.PercentOutput,
                                g.getConfig().mRunOutputPercentage * (forward ? 1 : -1), true);
                        Timer.delay(g.getConfig().mRunTimeSec);

                        // Now poll the interesting information.
                        double current = individualTalonToCheck.getOutputCurrent();
                        currents.add(new Current(current));

                        individualTalonToCheck.set(ControlMode.PercentOutput, 0.0, true);

                        // Perform individual check if current too low
                        if (current < g.getConfig().mCurrentFloor) {
                            currents.get(currents.size() - 1).setFail();
                            System.out.println(individualTalonToCheck.getGZName()
                                    + " has failed current floor check vs " + g.getConfig().mCurrentFloor + "!!");
                            failure = true;
                            g.getSubsystem().setSubsystemHasTalonTestingFail();
                        }
                        Timer.delay(g.getConfig().mWaitTimeSec);

                        forward = !forward;
                    }
                }

                // Checked every current and recorded, run average checks

                // Now run aggregate checks.
                Double average = 0.0;
                for (int i = 0; i < 2; i++) {
                    ArrayList<Current> currents;

                    if (i == 0)
                        currents = g.getForwardCurrents();
                    else
                        currents = g.getReverseCurrents();

                    for (Current c : currents)
                        average += c.getCurrent();

                    average /= currents.size();

                    if (i == 0)
                        g.setAverageForwardAmperage(average);
                    else
                        g.setAverageReverseAmperage(average);

                    // Current is too far away from average
                    for (Current c : currents) {
                        if (!GZUtil.epsilonEquals(c.getCurrent(), average, g.getConfig().mCurrentEpsilon)) {
                            c.setFail();
                            failure = true;
                            g.getSubsystem().setSubsystemHasTalonTestingFail();
                        }
                    }
                }

                // Restore Talon configurations
                for (int i = 0; i < talonsToCheck.size(); ++i) {
                    talonsToCheck.get(i).set(storedConfigurations.get(i).mMode, storedConfigurations.get(i).mSetValue,
                            true);
                }
                for (GZSRX t : talonsToCheck)
                    t.setBeingChecked(false);
            }
        }

        System.out.println("Talon check " + (failure ? " failed" : " passed"));
        createHTMLFile();
    }

    private void createHTMLFile() {
        String body = "";
        body += GZFiles.paragraph("Created on " + GZUtil.dateTime(false));

        for (GZSubsystem s : subsystemMap.keySet()) {
            ArrayList<TalonGroup> talonGroups = subsystemMap.get(s);

            // header
            String subsystemColor = s.doesSubsystemHaveTalonTestingFail() ? "red" : "black";
            body += GZFiles.header(s.getClass().getSimpleName(), 1, subsystemColor);

            for (TalonGroup g : talonGroups) {

                // values for this group
                ArrayList<GZSRX> tln = g.getTalons();
                ArrayList<Current> fwd = g.getForwardCurrents();
                ArrayList<Current> rev = g.getReverseCurrents();

                int talonSize = tln.size() - 1;
                int fwdSize = fwd.size() - 1;
                int revSize = rev.size() - 1;

                // check size
                if (!((talonSize == fwdSize) && (fwdSize == revSize))) {
                    System.out.println("Talon size (" + talonSize + ") and forward currents size (" + fwdSize
                            + ") and reverse currents size (" + revSize + ") not equal!");
                    return;
                }

                String color = (g.hasFail() ? "red" : "black");
                body += GZFiles.header(g.getName(), 2, color);

                String[] config = g.getConfig().configAsString().split("\n");
                // Write config as individual lines
                for (String configLine : config)
                    body += GZFiles.paragraph(configLine);


                String table = "";

                // Average to table
                table += GZFiles.tableRow(
                        GZFiles.tableCell("Average") + GZFiles.tableCell(g.getAverageForwardAmperage().toString())
                                + GZFiles.tableCell(g.getAverageForwardAmperage().toString()));

                // Headers
                table += GZFiles.tableHeader(GZFiles.tableCell("Talon") + GZFiles.tableCell("Forward Amperage")
                        + GZFiles.tableCell("Reverse Amperage"));

                // Loop through every talon
                for (int i = 0; i < talonSize; i++) {
                    String row = "";

                    boolean fwdFail = fwd.get(i).getFail();
                    boolean revFail = rev.get(i).getFail();

                    String talonCell;
                    if (fwdFail || revFail)
                        talonCell = GZFiles.tableCell(tln.get(i).getGZName(), "yellow", false);
                    else
                        talonCell = GZFiles.tableCell(tln.get(i).getGZName());

                    row += talonCell;

                    // Populate forward cell
                    String fwdCell;
                    if (fwdFail)
                        fwdCell = GZFiles.tableCell(fwd.get(i).getCurrent().toString(), "red", false);
                    else
                        fwdCell = GZFiles.table(fwd.get(i).getCurrent().toString());

                    row += fwdCell;

                    // Populate reverse cell
                    String revCell;
                    if (revFail)
                        revCell = GZFiles.tableCell(rev.get(i).getCurrent().toString(), "red", false);
                    else
                        revCell = GZFiles.table(rev.get(i).getCurrent().toString());

                    row += revCell;

                    row = GZFiles.tableRow(row);

                    table += row;
                }

                // Finish table
                table = GZFiles.table(table);

                body += table;

                File f;
                try {
                    f = GZFileMaker.getFile("SRXReport-" + GZUtil.dateTime(false), "3452/SRXReports",
                            ValidFileExtensions.HTML, true, true);
                    GZFiles.createHTMLFile(f, body);
                } catch (Exception e) {
                    System.out.println("Could not write SRXReport to USB, writing to RIO...");
                    try {
                        f = GZFileMaker.getFile("SRXReport-" + GZUtil.dateTime(false), "3452/SRXReports",
                                ValidFileExtensions.HTML, false, true);
                        GZFiles.createHTMLFile(f, body);
                    } catch (Exception cannotWriteSRXReport) {
                        System.out.println("Could not write SRX report!");
                    }
                }
            }

        }
    }
}