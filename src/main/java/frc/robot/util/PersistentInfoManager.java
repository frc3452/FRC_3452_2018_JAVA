package frc.robot.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import edu.wpi.first.wpilibj.Notifier;
import frc.robot.GZOI;

public class PersistentInfoManager {

    // Map linking Name of setting to value
    private Map<String, PersistentInfo> mSettingsMap = new HashMap<String, PersistentInfo>();

    private static final double PRINT_TIME = 5;

    // timers and prev vals
    private final GZTimer mOnTimeTimer = new GZTimer("OnTime");
    private double mPreviousOnTime = 0;
    private final GZTimer mEnabledTimer = new GZTimer("EnabledTimer");
    private double mPreviousEnabledTime = 0;

    private GZNotifier mUpdateNotifier;
    private GZNotifier mFailedNotifier;

    private Flag mReadFailed = new Flag();

    private static PersistentInfoManager mInstance = null;

    private PersistentInfo mEnabledTime = new PersistentInfo() {
        public void update() {
            this.addToValue(mEnabledTimer.get() - mPreviousEnabledTime);
            mPreviousEnabledTime = mEnabledTimer.get();
        }
    };

    private PersistentInfo mOnTime = new PersistentInfo() {
        public void update() {
            this.addToValue(mOnTimeTimer.get() - mPreviousOnTime);
            mPreviousOnTime = mOnTimeTimer.get();
        }
    };

    private PersistentInfo mLeftEncoderRotations = new PersistentInfo() {
        public void update() {
            // TODO
        }
    };
    private PersistentInfo mRightEncoderRotations = new PersistentInfo() {
        public void update() {
            // TODO
        }
    };

    private PersistentInfo mDisabled = new PersistentInfo(0.0) {
        Double c = 0.0;

        public void update() {
            this.setValue(GZOI.getInstance().isSafteyDisabled() ? 3452.0 : 0.0);
        }

        public void readOnStartup() {
            // if this
            GZOI.getInstance().setSafteyDisable(this.getValue() == 3452);
        }
    };

    public static PersistentInfoManager getInstance() {
        if (mInstance == null)
            mInstance = new PersistentInfoManager();

        return mInstance;
    }

    // On startup put values in map and start timer
    private PersistentInfoManager() {
        mSettingsMap.put("EnabledTime", mEnabledTime);
        mSettingsMap.put("OnTime", mOnTime);
        mSettingsMap.put("LeftEncoderRot", mLeftEncoderRotations);
        mSettingsMap.put("RightEncoderRot", mRightEncoderRotations);
        mSettingsMap.put("Disabled", mDisabled);

        mOnTimeTimer.oneTimeStartTimer();

        mFailedNotifier = new GZNotifier(new Runnable() {
            public void run() {
                if (GZOI.getInstance().isDisabled()) {
                    updateValues();
                    System.out.println(
                            "ERROR Will not write persistent settings! Make sure to manually update the file!");
                    for (String s : mSettingsMap.keySet()) {
                        System.out.println(s + "\t\t\t" + mSettingsMap.get(s).getValue());
                    }
                }
            }
        });
    }

    // Read from file and folder on RIO
    public void readOnStartup(String fileName, String folder) {
        readOnStartup(fileName, folder, false);
    }

    /**
     * Read from file and folder on RIO or USB Store values into map Call read
     * setting, which (if defined) could update a robot variable with setting
     */
    public void readOnStartup(String fileName, String folder, boolean usb) {
        try {
            // Set up fille reading
            File f = GZFileMaker.getFile(fileName, folder, usb, false);
            Scanner scnr = new Scanner(new FileReader(f));

            // loop through lines but drop out if it fails
            while (scnr.hasNext() && !mReadFailed.isFlagTripped()) {
                String t = scnr.nextLine();
                String split[] = t.split(",");

                // if map contains that setting
                if (mSettingsMap.containsKey(split[0])) {
                    // Get setting and read in value
                    mSettingsMap.get(split[0]).setValue(Double.valueOf(split[1]));
                } else {
                    // Map doesn't have setting
                    System.out.println("ERROR Could not read setting " + split[0] + ".");
                    mReadFailed.tripFlag();
                }
            }

            // Close scanner
            scnr.close();

            // If any PersistentInfos do anything on startup like refresh variables in other files, have
            // them do it now
            if (!mReadFailed.isFlagTripped())
                readSettings();

        } catch (Exception e) {
            // Couldn't read persistent settings
            mReadFailed.tripFlag();
            System.out.println("ERROR Could not read persistent settings!");
        }

        if (mReadFailed.isFlagTripped())
            startPrintMap();
    }

    private void readSettings()
    {
        for (PersistentInfo p : mSettingsMap.values())
            p.readSetting();
    }

    private void updateValues() {
        for (PersistentInfo p : mSettingsMap.values())
            p.update();
    }

    private void startPrintMap() {
        if (!mFailedNotifier.isRunning())
            mFailedNotifier.startPeriodic(PRINT_TIME);
    }

    private void stopPrintMap() {
        mFailedNotifier.stop();
    }

    // Update file every __ seconds @ file and folder on RIO
    public void updateFile(final String fileName, final String folder) {
        updateFile(fileName, folder, false);
    }

    // Update file every __ seconds @ file and folder on RIO or USB
    public void updateFile(final String fileName, final String folder, final boolean usb) {

        // Define notifier and runnable
        mUpdateNotifier = new GZNotifier(new Runnable() {
            public void run() {

                // get new values
                updateValues();

                try {
                    // SETUP FILE WRITING
                    File f = GZFileMaker.getFile(fileName, folder, usb, true);
                    // create file writing vars

                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));

                    // write values
                    // LeftEncoderTicks,1024
                    for (String v : mSettingsMap.keySet()) {
                        bw.write(v + "," + mSettingsMap.get(v).getValue());
                        bw.write("\r\n");
                    }

                    bw.close();

                    // Don't print map
                    stopPrintMap();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR Could not update long term stats file!");

                    startPrintMap();
                }
            }
        });

        // only start once and if read didn't work
        if (!mUpdateNotifier.hasStarted() && !mReadFailed.isFlagTripped())
            mUpdateNotifier.startPeriodic(PRINT_TIME);
    }

    public void robotEnabled() {
        mEnabledTimer.startTimer();
    }

    public void robotDisabled() {
        mEnabledTimer.stopTimer();
    }

}