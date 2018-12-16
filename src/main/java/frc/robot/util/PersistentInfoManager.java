package frc.robot.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import frc.robot.Constants;
import frc.robot.Constants.kFiles;
import frc.robot.GZOI;

public class PersistentInfoManager {

    // Map linking Name of setting to value
    private Map<String, PersistentInfo> mSettingsMap = new HashMap<String, PersistentInfo>();

    // timers and prev vals
    private final GZTimer mOnTimeTimer = new GZTimer("OnTime");
    private double mPreviousOnTime = 0;
    private final GZTimer mEnabledTimer = new GZTimer("EnabledTimer");
    private double mPreviousEnabledTime = 0;

    private GZNotifier mUpdateNotifier;

    private Flag mReadFailed = new Flag();

    private static PersistentInfoManager mInstance = null;

    private PersistentInfo mEnabledTime = new PersistentInfo(0.0) {
        public void update() {
            this.addToValue(mEnabledTimer.get() - mPreviousEnabledTime);
            mPreviousEnabledTime = mEnabledTimer.get();
        }
    };

    private PersistentInfo mOnTime = new PersistentInfo(0.0) {
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
        public void update() {
            this.setValue(GZOI.getInstance().isSafteyDisabled() ? 3452.0 : 0.0);
        }

        public void readSetting() {
            GZOI.getInstance().setSafteyDisable(this.getValue() == 3452.0);
        }
    };

    private PersistentInfo mDriverMode = new PersistentInfo(0.0) {

        @Override
        public void update() {
            this.setValue((double) GZOI.getInstance().getDriverMode());
        }

        public void readSetting() {
            double temp = this.getValue();
            GZOI.getInstance().setDriverMode((int) temp);
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
        mSettingsMap.put("DriverMode", mDriverMode);

        mOnTimeTimer.oneTimeStartTimer();
    }

    // Read from file and folder on RIO
    public void readOnStartup(String fileName, String folder) {
        readOnStartup(fileName, folder, false);
    }

    public void initialize(String fileName, String folder, boolean usb) {
        readOnStartup(fileName, folder, usb);
        updateFile(fileName, folder, usb);
    }

    private void backupFile() {
        boolean fail = false;

        try {
            File source = GZFileMaker.getFile(kFiles.STATS_FILE_NAME, kFiles.STATS_FILE_FOLDER,
                    kFiles.STATS_FILE_ON_USB, false);

            File dest = GZFileMaker.getFile("StatsBackup-" + GZUtil.dateTime(true), "3452/GZStatsBackup", true, true);

            // GZFileMaker will create the file, delete and then copy it
            Files.deleteIfExists(dest.toPath());

            Files.copy(source.toPath(), dest.toPath());
        } catch (Exception e) {
            fail = true;
            // e.printStackTrace();
        }
        System.out.println("Stats file backed up " + (fail ? "unsuccsessfully" : "succsessfully"));
    }

    public void initialize() {
        backupFile();
        readOnStartup(kFiles.STATS_FILE_NAME, kFiles.STATS_FILE_FOLDER, kFiles.STATS_FILE_ON_USB);
        updateFile(kFiles.STATS_FILE_NAME, kFiles.STATS_FILE_FOLDER, kFiles.STATS_FILE_ON_USB);
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
                    // mReadFailed.tripFlag();
                }
            }

            // Close scanner
            scnr.close();

            // If any PersistentInfos do anything on startup like refresh variables in other
            // files, have
            // them do it now
            if (!mReadFailed.isFlagTripped())
                readSettings();

        } catch (Exception e) {
            // Couldn't read persistent settings
            mReadFailed.tripFlag();
            System.out.println("ERROR Could not read persistent settings!");
        }

        if (!mReadFailed.isFlagTripped())
            System.out.println("Persistent settings read correctly.");
    }

    private void readSettings() {
        for (PersistentInfo p : mSettingsMap.values())
            p.readSetting();
    }

    private void updateValues() {
        for (PersistentInfo p : mSettingsMap.values())
            p.update();
    }

    public void printPersistentSettings() {
        System.out.println("~~~Persistent settings" + (mReadFailed.isFlagTripped() ? " in temp folder" : "") + "~~~");
        for (String s : mSettingsMap.keySet())
            System.out.println(s + "\t\t\t" + mSettingsMap.get(s).getValue());
    }

    // Update file every __ seconds @ file and folder on RIO
    public void updateFile(final String fileName, final String folder) {
        updateFile(fileName, folder, false);
    }

    public void updateFile(String fileName, String folder, final boolean usb) {

        final String mFileName;
        final String mFolder;
        final boolean mUsb;

        if (mReadFailed.isFlagTripped()) {
            mFileName = GZUtil.dateTime(false);
            mFolder = Constants.kFiles.STATS_FILE_FOLDER;
            mUsb = Constants.kFiles.STATS_FILE_ON_USB;
            System.out.println("ERROR Could not read startup file! Writing new file to " + mFolder + "/" + mFileName
                    + ".csv" + " on RIO");

        } else {
            mFileName = fileName;
            mFolder = folder;
            mUsb = usb;
        }

        // Define notifier and runnable
        mUpdateNotifier = new GZNotifier(new Runnable() {
            public void run() {

                // get new values
                updateValues();

                try {
                    // SETUP FILE WRITING

                    File f = GZFileMaker.getFile(mFileName, mFolder, mUsb, true);

                    // create file writing vars
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));

                    // write values
                    // LeftEncoderTicks,1024
                    // Disabled,0
                    for (String v : mSettingsMap.keySet()) {
                        bw.write(v + "," + mSettingsMap.get(v).getValue());
                        bw.write("\r\n");
                    }

                    bw.close();

                } catch (Exception e) {
                    // e.printStackTrace();
                    System.out.println("ERROR Could not update long term stats file!");
                }
            }
        });

        // only start once and if read didn't work
        if (!mUpdateNotifier.hasStarted())
            mUpdateNotifier.startPeriodic(kFiles.DEFAULT_STATS_RECORD_TIME);
    }

    public void robotEnabled() {
        mEnabledTimer.startTimer();
    }

    public void robotDisabled() {
        mEnabledTimer.stopTimer();
    }

}