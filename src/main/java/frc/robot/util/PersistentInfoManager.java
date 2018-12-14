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

    // flag to trip if we cannot read the settings in.
    private Flag mFailed = new Flag();

    // Map linking Name of setting to value
    private Map<String, PersistentInfo> mSettingsMap = new HashMap<String, PersistentInfo>();

    // timers and prev vals
    private final GZTimer mOnTimeTimer = new GZTimer("OnTime");
    private double mPreviousOnTime = 0;
    private final GZTimer mEnabledTimer = new GZTimer("EnabledTimer");
    private double mPreviousEnabledTime = 0;

    private Notifier mUpdateNotifier;

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

            // SET UP FILE READING
            File f = GZFileMaker.getFile(fileName, folder, usb, false);
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

    // If we haven't done it already, start up the printing map runnable (notify the
    // user every
    // 5 seconds while disabled that values not being recorded)
    // Stop the updating notifier,
    // trip flag
    private void fail() {
        if (!mFailed.isFlagTripped()) {
            printMapWhenDisabled();
            mUpdateNotifier.stop();
            mFailed.tripFlag();
        }
    }

    private void updateValues()
    {
        for (PersistentInfo p : mSettingsMap.values())
            p.update();
    }

    private void printMapWhenDisabled() {
        new Notifier(new Runnable() {
            public void run() {
                if (GZOI.getInstance().isDisabled()) {
                    updateValues();

                    System.out.println(
                            "ERROR Will not write persistent settings! Make sure to manually update the file!");
                    for (String s : mSettingsMap.keySet())
                    {
                        System.out.println(s + "\t\t\t" + mSettingsMap.get(s).getValue());
                    }
                }
            }
        }).startPeriodic(5);
    }

    // Update file every __ seconds @ file and folder on RIO
    public void updateFile(final String fileName, final String folder, final double seconds) {
        updateFile(fileName, folder, seconds, false);
    }

    // Update file every __ seconds @ file and folder on RIO or USB
    public void updateFile(final String fileName, final String folder, final double seconds, final boolean usb) {

        //Define notifier and runnable
        mUpdateNotifier = new Notifier(new Runnable() {
            public void run() {

                //If flag tripped, stop
                if (mFailed.isFlagTripped()){
                    return;
                }

                //get new values
                updateValues();
                
                try {
                    // SETUP FILE WRITING
                    File f = GZFileMaker.getFile(fileName,folder, usb, true);
                    // create file writing vars
                    
                    BufferedWriter bw = new BufferedWriter(new FileWriter(f));

                    // write values 
                    // LeftEncoderTicks,1024
                    for (String v : mSettingsMap.keySet()) {
                        bw.write(v + "," + mSettingsMap.get(v).getValue());
                        bw.write("\r\n");
                    }

                    bw.close();

                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("ERROR Could not update long term stats file!");
                    fail();
                }
            }
        });

        //start notifier
        if (!mFailed.isFlagTripped())
            mUpdateNotifier.startPeriodic(seconds);

    }

    public void robotEnabled() {
        mEnabledTimer.start();
    }

    public void robotDisabled() {
        mEnabledTimer.stopTimer();
    }

}