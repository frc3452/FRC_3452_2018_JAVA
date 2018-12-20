package frc.robot.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.util.GZFileMaker.ValidFileExtensions;
import frc.robot.util.GZFiles.Folder;
import frc.robot.util.GZFiles.HTML;

public class MotorChecker {

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

    public static class MotorTestingGroup {
        private String mName;
        private double averageForwardAmperage;
        private double averageReverseAmperage;

        private GZSubsystem mSubsystem;

        private List<GZSpeedController> controllers = null;
        private ArrayList<Current> forwardCurrents = null;
        private ArrayList<Current> reverseCurrents = null;

        private CheckerConfig mCheckerConfig;

        public MotorTestingGroup(GZSubsystem subsystem, String name, List<GZSpeedController> talons,
                CheckerConfig config) {
            this.controllers = talons;
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

        public List<GZSpeedController> getControllers() {
            return controllers;
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
            retval += "Reverse after group: " + mReverseAfterGroup + "\n";

            return retval;
        }

        public void print() {
            System.out.println(toString());
        }
    }

    private static MotorChecker mInstance = null;

    public static MotorChecker getInstance() {
        if (mInstance == null)
            mInstance = new MotorChecker();
        return mInstance;
    }

    private MotorChecker() {
        clearGroups();
    }

    private Map<GZSubsystem, ArrayList<MotorTestingGroup>> subsystemMap;

    private double mTimeNeeded = 0;

    public void clearGroups()
    {
        subsystemMap = new HashMap<GZSubsystem, ArrayList<MotorTestingGroup>>();
    }

    public void addTalonGroup(MotorTestingGroup group) {
        if (!subsystemMap.containsKey(group.getSubsystem()))
            subsystemMap.put(group.getSubsystem(), new ArrayList<MotorTestingGroup>());

        subsystemMap.get(group.getSubsystem()).add(group);
    }

    public void checkTalons() {
        boolean failure = false;

        //Clear all fails
        for (GZSubsystem s : subsystemMap.keySet())
            s.clearMotorTestingFails();

        mTimeNeeded = 0;
        for (ArrayList<MotorTestingGroup> allGroups : subsystemMap.values()) {
            for (MotorTestingGroup group : allGroups) {
                mTimeNeeded += group.getConfig().mRunTimeSec * 2 * group.controllers.size();
                mTimeNeeded += group.getConfig().mWaitTimeSec * 2 * group.controllers.size();
            }
        }

        System.out.println("Starting motor checker... estimated time needed: " + mTimeNeeded + " seconds");

        // will store the current motor groups
        ArrayList<MotorTestingGroup> talonGroups;

        // loop through every subsystem
        for (GZSubsystem s : subsystemMap.keySet()) {
            // get current groups
            talonGroups = subsystemMap.get(s);

            // loop through each group in this subsystem
            for (MotorTestingGroup group : talonGroups) {

                // Controllers
                List<GZSpeedController> controllersToCheck = group.getControllers();

                System.out.println("Checking subsystem " + group.getSubsystemName() + " group " + group.getName()
                        + " for " + controllersToCheck.size() + " talons. Estimated total time left: " + mTimeNeeded
                        + " seconds");

                // Dont allow other methods to control these controllers
                for (GZSpeedController t : controllersToCheck)
                    t.lockOutController(true);

                // Loop through controllers (running all controllers in group forwards, then all
                // backwards)
                if (group.getConfig().mReverseAfterGroup) {
                    // start forward
                    boolean forward = true;

                    // do this twice
                    for (int i = 0; i < 2; i++) {

                        // Check each talon
                        for (GZSpeedController individualTalonToCheck : controllersToCheck) {
                            failure |= checkController(individualTalonToCheck, group, forward);
                        }
                        // once we've checked them all, do it again but the other way
                        forward = !forward;
                    }
                } else { // test each talon forwards, then test it backwards, then move onto the next
                         // talon
                    for (GZSpeedController individualControllerToCheck : controllersToCheck) {
                        // Test twice
                        for (int i = 0; i < 2; i++)
                            failure |= checkController(individualControllerToCheck, group, (i == 0));
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
                            group.getSubsystem().setMotorTestingFail();
                        }
                    }
                }

                // Unlock talons so another method can control them
                for (GZSpeedController t : controllersToCheck)
                    t.lockOutController(false);
            }

        }

        System.out.println("Motor check " + (failure ? " failed" : " passed"));
        createHTMLFile();
    }

    private boolean checkController(GZSpeedController individualTalonToCheck, MotorTestingGroup group,
            boolean forward) {
        boolean failure = false;
        System.out.println("Checking: " + individualTalonToCheck.getGZName() + (forward ? " forwards" : " reverse"));

        ArrayList<Current> currents = (forward ? group.getForwardCurrents() : group.getReverseCurrents());

        individualTalonToCheck.set(group.getConfig().mRunOutputPercentage * (forward ? 1 : -1), true);
        Timer.delay(group.getConfig().mRunTimeSec);
        mTimeNeeded -= group.getConfig().mRunTimeSec;

        // Now poll the interesting information.
        double current = individualTalonToCheck.getAmperage();
        currents.add(new Current(current));

        individualTalonToCheck.set(0.0, true);

        // Perform individual check if current too low
        if (current < group.getConfig().mCurrentFloor) {
            currents.get(currents.size() - 1).setFail();
            // System.out.println(individualTalonToCheck.getGZName() + " has failed current
            // floor check vs "
            // + group.getConfig().mCurrentFloor + "!!");
            failure = true;
            group.getSubsystem().setMotorTestingFail();
        }
        Timer.delay(group.getConfig().mWaitTimeSec);
        mTimeNeeded -= group.getConfig().mWaitTimeSec;

        return failure;
    }

    private void createHTMLFile() {
        String body = "";

        // Time at top of file
        body += HTML.paragraph("Created on " + GZUtil.dateTime(false));

        // Loop through every subsystem
        for (GZSubsystem subsystem : subsystemMap.keySet()) {

            // groups for subsystem
            ArrayList<MotorTestingGroup> talonGroups = subsystemMap.get(subsystem);

            // header (write subsystem color in red if has error)
            String subsystemColor = subsystem.hasMotorTestingFail() ? "red" : "black";
            body += HTML.header(subsystem.getClass().getSimpleName(), 1, subsystemColor);

            // Content of entire subsystem
            String subsystemContent = "";

            for (MotorTestingGroup talonGroup : talonGroups) {

                // values for the group we are currently creating
                List<GZSpeedController> mtr = talonGroup.getControllers();
                ArrayList<Current> fwd = talonGroup.getForwardCurrents();
                ArrayList<Current> rev = talonGroup.getReverseCurrents();

                // Sizes
                int talonSize = mtr.size() - 1;
                int fwdSize = fwd.size() - 1;
                int revSize = rev.size() - 1;

                // check size
                if (!((talonSize == fwdSize) && (fwdSize == revSize))) {
                    System.out.println("Motor size (" + talonSize + ") and forward currents size (" + fwdSize
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

                    // Put talon cell
                    String talonCell;
                    if (fwdFail || revFail)
                        talonCell = HTML.tableCell(mtr.get(talon).getGZName(), "yellow", false);
                    else
                        talonCell = HTML.tableCell(mtr.get(talon).getGZName());

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

                    // Format as row
                    row = HTML.tableRow(row);

                    // add to table
                    table += row;

                } // end of Loop through every talon

                // group has all talons written to table
                // format as table and add to subsystem
                table = HTML.table(table);
                subsystemContent += table;

            } // end of all groups in subsystem loop

            // if the subsystem doesn't have any fails, wrap in a button so we can hide it
            // easily
            if (!subsystem.hasMotorTestingFail())
                subsystemContent = HTML.button("Open " + subsystem.getClass().getSimpleName(), subsystemContent);

            body += subsystemContent;
        } // end of all subsystems

        try {
            // if on rio, folder will be SRXReports/SRXReport-2018.12.20.09.06
            // if on usb, folder will be 3452/SRXReports/SRXReport-2018.12.20.09.06
            Folder srxReportFolder = new Folder("SRXReports");

            // write to rio
            GZFile file = GZFileMaker.getFile("SRXReport-" + GZUtil.dateTime(false), srxReportFolder,
                    ValidFileExtensions.HTML, true, true);

            HTML.createHTMLFile(file, body);

            try {
                GZFiles.copyFile(file.getFile(), GZFileMaker.getFile(file, true).getFile());
            } catch (Exception copyFile) {
                System.out.println("Could not copy SRXReport to USB!");
            }
        } catch (Exception writingSRXReport) {
            System.out.println("Could not write SRXReport!");
        }

    }
}