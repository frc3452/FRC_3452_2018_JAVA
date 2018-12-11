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
        public void update() {
            this.setValue(GZOI.getInstance().isSafteyDisabled() ? 0.0 : 3452.0);
        }

        public void readOnStartup() {
            GZOI.getInstance().setSafteyDisable(this.getValue() == 3452);
        }
    };

    private Flag mFailed = new Flag();

    private Map<String, PersistentInfo> mSettingsMap = new HashMap<String, PersistentInfo>();

    private final GZTimer mOnTimeTimer = new GZTimer("OnTime");
    private double mPreviousOnTime = 0;
    private final GZTimer mEnabledTimer = new GZTimer("EnabledTimer");
    private double mPreviousEnabledTime = 0;

    private Notifier mUpdateNotifier;

    private static PersistentInfoManager mInstance = null;

    public static PersistentInfoManager getInstance() {
        if (mInstance == null)
            mInstance = new PersistentInfoManager();

        return mInstance;
    }

    private PersistentInfoManager() {
        mSettingsMap.put("EnabledTime", mEnabledTime);
        mSettingsMap.put("OnTime", mOnTime);
        mSettingsMap.put("LeftEncoderRot", mLeftEncoderRotations);
        mSettingsMap.put("RightEncoderRot", mRightEncoderRotations);
        mSettingsMap.put("Disabled", mDisabled);
        mOnTimeTimer.oneTimeStartTimer();
    }

    public void readOnStartup(String fileName, String folder) {
        readOnStartup(fileName, folder, false);
    }

    /**
     * Update map with values from file
     */
    public void readOnStartup(String fileName, String folder, boolean usb) {
        try {

            // SET UP FILE READING
            File f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");
            Scanner scnr = new Scanner(new FileReader(f));

            // loop through lines
            while (scnr.hasNext() && !mFailed.isFlagTripped()) {
                String t = scnr.nextLine();
                String split[] = t.split(",");

                // if map contains that setting
                if (mSettingsMap.containsKey(split[0])) {
                    // Get setting
                    mSettingsMap.get(split[0]).setValue(Double.valueOf(split[1]));
                } else {

                    System.out.println("ERROR Could not read setting " + split[0] + ".");
                    fail();
                }

            }

            for (PersistentInfo p : mSettingsMap.values())
                p.readSetting();

        } catch (Exception e) {
            System.out.println("ERROR Could not read persistent settings!");
            fail();
        }

    }

    private void fail() {
        mFailed.tripFlag();
        mUpdateNotifier.stop();
        printMapWhenDisabled();
    }

    private void printMapWhenDisabled() {
        new Notifier(new Runnable() {
            public void run() {
                if (GZOI.getInstance().isDisabled()) {
                    System.out.println(
                            "ERROR Will not write persistent settings! Make sure to manually update the file!");
                    System.out.println(mSettingsMap);
                }
            }
        }).startPeriodic(5);
    }

    public void updateFile(String fileName, String folder, double seconds) {
        updateFile(fileName, folder, seconds, false);
    }

    public void updateFile(String fileName, String folder, double seconds, boolean usb) {
        mUpdateNotifier = new Notifier(new Runnable() {
            public void run() {

                if (mFailed.isFlagTripped())
                    fail();

                for (PersistentInfo p : mSettingsMap.values())
                    p.update();

                try {
                    // SETUP FILE WRITING
                    File f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder);
                    f.mkdirs();
                    f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");

                    // if it isn't there, create it
                    if (!f.exists())
                        f.createNewFile();

                    // create file writing vars
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));

                    // write values
                    for (String v : mSettingsMap.keySet()) {
                        bw.write(v + "," + mSettingsMap.get(v).getValue());
                        bw.write("\r\n");
                    }

                    bw.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR Could not update long term stats file!");
                }
            }
        });

    }

    public void robotEnabled() {
        mEnabledTimer.start();
    }

    public void robotDisabled() {
        mEnabledTimer.stopTimer();
    }

}