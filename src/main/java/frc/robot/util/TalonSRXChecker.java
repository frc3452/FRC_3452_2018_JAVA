package frc.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.GZFileMaker.ValidFileExtensions;
import frc.robot.util.GZFiles.HTML;

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

        private List<GZSRX> talons = null;
        private ArrayList<Current> forwardCurrents = null;
        private ArrayList<Current> reverseCurrents = null;

        private CheckerConfig mCheckerConfig;

        public TalonGroup(GZSubsystem subsystem, String name, List<GZSRX> talons, CheckerConfig config) {
            this.talons = talons;
            this.mName = name;
            this.mSubsystem = subsystem;
            this.mCheckerConfig = config;
        }

        public boolean hasFail() {
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

        public List<GZSRX> getTalons() {
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

        public boolean mReverseAfterGroup = true;

        public CheckerConfig(double currentFloor, double currentEpsilon, double runTimeSec, double waitTimeSec,
                double outputPercentage, boolean reverseAfterGroup) {
            this.mCurrentFloor = currentFloor;
            this.mCurrentEpsilon = currentEpsilon;
            this.mRunTimeSec = runTimeSec;
            this.mWaitTimeSec = waitTimeSec;
            this.mRunOutputPercentage = outputPercentage;
            this.mReverseAfterGroup = reverseAfterGroup;
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

    private double mTimeNeeded = 0;

    public void addTalonGroup(TalonGroup group) {
        if (!subsystemMap.containsKey(group.getSubsystem()))
            subsystemMap.put(group.getSubsystem(), new ArrayList<TalonGroup>());

        subsystemMap.get(group.getSubsystem()).add(group);
    }

    public void checkTalons() {
        boolean failure = false;

        mTimeNeeded = 0;
        for (ArrayList<TalonGroup> allGroups : subsystemMap.values()) {
            for (TalonGroup group : allGroups) {
                mTimeNeeded += group.getConfig().mRunTimeSec * 2 * group.talons.size();
                mTimeNeeded += group.getConfig().mWaitTimeSec * 2 * group.talons.size();
            }
        }

        System.out.println("Starting talon checker... estimated time needed: " + mTimeNeeded + " seconds");

        // will store the current talon groups
        ArrayList<TalonGroup> talonGroups;

        // loop through every subsystem
        for (GZSubsystem s : subsystemMap.keySet()) {
            // get current groups
            talonGroups = subsystemMap.get(s);

            // loop through each group in this subsystem
            for (TalonGroup group : talonGroups) {

                // Talons
                List<GZSRX> talonsToCheck = group.getTalons();

                System.out.println("Checking subsystem " + group.getSubsystemName() + " group " + group.getName()
                        + " for " + talonsToCheck.size() + " talons. Estimated total time left: " + mTimeNeeded + " seconds");

                // Store previous set values
                ArrayList<StoredTalonSRXConfiguration> storedConfigurations = new ArrayList<>();

                // Dont allow other methods to control these talons
                for (GZSRX t : talonsToCheck)
                    t.lockOutTalon(true);

                // Record previous configuration for all talons.
                for (GZSRX t : talonsToCheck) {
                    StoredTalonSRXConfiguration configuration = new StoredTalonSRXConfiguration();
                    configuration.mMode = t.getLastControlMode();
                    configuration.mSetValue = t.getLastSetValue();
                    storedConfigurations.add(configuration);
                    // Now set to disabled.
                    t.set(ControlMode.PercentOutput, 0.0, true);
                }

                // Loop through talons (running all talons in group forwards, then all
                // backwards)
                if (group.getConfig().mReverseAfterGroup) {
                    // start forward
                    boolean forward = true;

                    // do this twice
                    for (int i = 0; i < 2; i++) {

                        // Check each talon
                        for (GZSRX individualTalonToCheck : talonsToCheck) {
                            failure |= checkTalon(individualTalonToCheck, group, forward);
                        }
                        // once we've checked them all, do it again but the other way
                        forward = !forward;
                    }
                } else { // test each talon forwards, then test it backwards, then move onto the next
                         // talon
                    for (GZSRX individualTalonToCheck : talonsToCheck) {
                        // Test twice
                        for (int i = 0; i < 2; i++)
                            failure |= checkTalon(individualTalonToCheck, group, (i == 0));
                        // on the first loop when i == 0, go forwards, then go backwards
                    }
                }


                // We've now checked every current and recorded, run average checks

                // Now run aggregate checks.
                Double average = 0.0;
                for (int i = 0; i < 2; i++) {
                    ArrayList<Current> currents;

                    // First loop, test forward currents
                    if (i == 0)
                        currents = group.getForwardCurrents();
                    else
                        currents = group.getReverseCurrents();

                    // Accumulate average
                    for (Current c : currents)
                        average += c.getCurrent();
                    average /= currents.size();

                    // Set average for groups
                    if (i == 0)
                        group.setAverageForwardAmperage(average);
                    else
                        group.setAverageReverseAmperage(average);

                    // Current is too far away from average
                    for (Current c : currents) {
                        if (!GZUtil.epsilonEquals(c.getCurrent(), average, group.getConfig().mCurrentEpsilon)) {
                            failure = true;
                            c.setFail();
                            group.getSubsystem().setTalonTestingFail();
                        }
                    }
                }

                // Restore Talon configurations
                for (int i = 0; i < talonsToCheck.size(); ++i) {
                    talonsToCheck.get(i).set(storedConfigurations.get(i).mMode, storedConfigurations.get(i).mSetValue,
                            true);
                }

                //Unlock talons so another method can control them
                for (GZSRX t : talonsToCheck)
                    t.lockOutTalon(false);
            }

        }

        System.out.println("Talon check " + (failure ? " failed" : " passed"));
        createHTMLFile();
    }

    private boolean checkTalon(GZSRX individualTalonToCheck, TalonGroup group, boolean forward) {
        boolean failure = false;
        System.out.println("Checking: " + individualTalonToCheck.getGZName() + (forward ? " forwards" : " reverse"));

        ArrayList<Current> currents = (forward ? group.getForwardCurrents() : group.getReverseCurrents());

        individualTalonToCheck.set(ControlMode.PercentOutput,
                group.getConfig().mRunOutputPercentage * (forward ? 1 : -1), true);
        Timer.delay(group.getConfig().mRunTimeSec);
        mTimeNeeded -= group.getConfig().mRunTimeSec;

        // Now poll the interesting information.
        double current = individualTalonToCheck.getOutputCurrent();
        currents.add(new Current(current));

        individualTalonToCheck.set(ControlMode.PercentOutput, 0.0, true);

        // Perform individual check if current too low
        if (current < group.getConfig().mCurrentFloor) {
            currents.get(currents.size() - 1).setFail();
            // System.out.println(individualTalonToCheck.getGZName() + " has failed current floor check vs "
            //         + group.getConfig().mCurrentFloor + "!!");
            failure = true;
            group.getSubsystem().setTalonTestingFail();
        }
        Timer.delay(group.getConfig().mWaitTimeSec);
        mTimeNeeded -= group.getConfig().mWaitTimeSec;

        return failure;
    }

    private void createHTMLFile() {
        String body = "";

        //Time at top of file
        body += HTML.paragraph("Created on " + GZUtil.dateTime(false));

        //Loop through every subsystem
        for (GZSubsystem subsystem : subsystemMap.keySet()) {

            //groups for subsystem
            ArrayList<TalonGroup> talonGroups = subsystemMap.get(subsystem);

            // header (write subsystem color in red if has error)
            String subsystemColor = subsystem.hasTalonTestingFail() ? "red" : "black";
            body += HTML.header(subsystem.getClass().getSimpleName(), 1, subsystemColor);

            //Content of entire subsystem
            String subsystemContent = "";

            for (TalonGroup talonGroup : talonGroups) {

                // values for the group we are currently creating
                List<GZSRX> tln = talonGroup.getTalons();
                ArrayList<Current> fwd = talonGroup.getForwardCurrents();
                ArrayList<Current> rev = talonGroup.getReverseCurrents();

                //Sizes
                int talonSize = tln.size() - 1;
                int fwdSize = fwd.size() - 1;
                int revSize = rev.size() - 1;

                // check size
                if (!((talonSize == fwdSize) && (fwdSize == revSize))) {
                    System.out.println("Talon size (" + talonSize + ") and forward currents size (" + fwdSize
                            + ") and reverse currents size (" + revSize + ") not equal!");
                    return;
                }

                // Write each group in red or black if it has a fail
                String color = (talonGroup.hasFail() ? "red" : "black");
                subsystemContent += HTML.header(talonGroup.getName(), 2, color);

                // Write config as individual lines
                String[] config = talonGroup.getConfig().configAsString().split("\n");
                for (String configLine : config)
                    subsystemContent += HTML.paragraph(configLine);

                String table = "";

                // Write Average to table
                table += HTML.tableRow(
                        HTML.tableCell("Average") + HTML.tableCell(talonGroup.getAverageForwardAmperage().toString())
                                + HTML.tableCell(talonGroup.getAverageForwardAmperage().toString()));

                // Headers
                table += HTML.tableHeader(HTML.tableCell("Talon") + HTML.tableCell("Forward Amperage")
                        + HTML.tableCell("Reverse Amperage"));

                // Loop through every talon
                for (int talon = 0; talon < talonSize; talon++) {
                    String row = "";

                    boolean fwdFail = fwd.get(talon).getFail();
                    boolean revFail = rev.get(talon).getFail();

                    //Put talon cell
                    String talonCell;
                    if (fwdFail || revFail)
                        talonCell = HTML.tableCell(tln.get(talon).getGZName(), "yellow", false);
                    else
                        talonCell = HTML.tableCell(tln.get(talon).getGZName());

                    row += talonCell;

                    // Populate forward cell
                    String fwdCell;
                    if (fwdFail)
                        fwdCell = HTML.tableCell(fwd.get(talon).getCurrent().toString(), "red", false);
                    else
                        fwdCell = HTML.table(fwd.get(talon).getCurrent().toString());
                    row += fwdCell;

                    // Populate reverse cell
                    String revCell;
                    if (revFail)
                        revCell = HTML.tableCell(rev.get(talon).getCurrent().toString(), "red", false);
                    else
                        revCell = HTML.table(rev.get(talon).getCurrent().toString());
                    row += revCell;

                    //Format as row
                    row = HTML.tableRow(row);

                    //add to table
                    table += row;

                } // end of Loop through every talon

                // group has all talons written to table
                // format as table and add to subsystem
                table = HTML.table(table);
                subsystemContent += table;

            } // end of all groups in subsystem loop

            //if the subsystem doesn't have any fails, wrap in a button so we can hide it easily
            if (!subsystem.hasTalonTestingFail())
                subsystemContent = HTML.button(subsystem.getClass().getSimpleName(), subsystemContent);

            body += subsystemContent;
        } // end of all subsystems

        File f;
        try {
            //Ideally write to RIO and then USB as well
            f = GZFileMaker.getFile("SRXReport-" + GZUtil.dateTime(false), "3452/SRXReports", ValidFileExtensions.HTML,
                    true, true);
            HTML.createHTMLFile(f, body);

            //TODO BACKUP TO USB, MAKE FOLDER CLASS FOR IF ON USB OR IF ON RIO
        } catch (Exception e) {
            System.out.println("Could not write SRXReport to USB, writing to RIO...");
            try {
                f = GZFileMaker.getFile("SRXReport-" + GZUtil.dateTime(false), "3452/SRXReports",
                        ValidFileExtensions.HTML, false, true);
                HTML.createHTMLFile(f, body);
            } catch (Exception cannotWriteSRXReport) {
                System.out.println("Could not write SRX report!");
            }
        }

    }
}