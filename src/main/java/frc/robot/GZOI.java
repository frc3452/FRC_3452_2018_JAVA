package frc.robot;

import java.util.ArrayList;
import java.util.Arrays;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants.kFiles;
import frc.robot.Constants.kOI;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Drive.DriveState;
import frc.robot.util.GZFiles;
import frc.robot.util.GZFiles.TASK;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZJoystick.Buttons;
import frc.robot.util.GZLog.LogItem;
import frc.robot.util.GZPDP;
import frc.robot.util.GZSubsystem;
import frc.robot.util.GZUtil;
import frc.robot.util.LatchedBoolean;

public class GZOI extends GZSubsystem {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	private boolean mWasTele = false, mWasAuto = false, mWasTest = false;

	private LatchedBoolean mUserButton = new LatchedBoolean();
	private boolean mSafteyDisable = false;

	private Drive drive = Drive.getInstance();

	private PowerDistributionPanel pdp = GZPDP.getInstance().getPDP();

	private ArrayList<String> mDriveModes = new ArrayList<String>();
	private int mDriveMode = 0;
	private int mPrevDriveMode = -1;

	private static GZOI mInstance = null;

	public static GZOI getInstance() {
		if (mInstance == null)
			mInstance = new GZOI();

		return mInstance;
	}

	private GZOI() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);

		mDriveModes.add("Standard (FWD on Left Thumb Stick, Turning on triggers)");
		mDriveModes.add("Alternate (FWD on Left Thumb Stick, Turning on Right Thumb Stick)");
	}

	boolean recording = false;
	boolean prevRecording = recording;

	public int getDriverMode() {
		return this.mDriveMode;
	}

	public void setDriverMode(int drivemode) {
		this.mDriveMode = drivemode;
		driveModeSanityCheck();
	}

	public void driveModeSanityCheck() {
		if (mDriveMode < 0)
			mDriveMode = mDriveModes.size() - 1;
		else if (mDriveMode > mDriveModes.size() - 1)
			mDriveMode = 0;
	}

	@Override
	public void loop() {
		outputSmartDashboard();

		if (driverJoy.areButtonsHeld(Arrays.asList(Buttons.LB, Buttons.RB))) {
			if (driverJoy.isAPressed())
				mDriveMode++;

			if (driverJoy.isBPressed())
				mDriveMode--;
		}

		driveModeSanityCheck();

		if (mDriveMode != mPrevDriveMode)
			System.out.println("Selected drive mode: " + mDriveModes.get(mDriveMode));
		mPrevDriveMode = mDriveMode;

		// FLAGS
		if (isTele())
			mWasTele = true;
		if (isAuto())
			mWasAuto = true;
		if (isTest())
			mWasTest = true;

		// SAFTEY DISABLE WITH USERBUTTON
		if (isFMS())
			mSafteyDisable = false;
		else if (mUserButton.update(RobotController.getUserButton()))
			mSafteyDisable = !mSafteyDisable;

		Robot.allSubsystems.disable(mSafteyDisable);

		if (isTele()) {
			Drive.getInstance().setWantedState(DriveState.OPEN_LOOP_DRIVER);

			// OVERRIDES, ETC.
			if (driverJoy.isAPressed())
				drive.slowSpeed(!drive.isSlow());
		}

		// CONTROLLER RUMBLE

		if (GZUtil.between(getMatchTime(), 29.1, 30))
			// ENDGAME
			rumble(kOI.Rumble.ENDGAME);
		else
			rumble(0);

		prevRecording = recording;
	}

	private void recordingUpdates() {
		if (driverJoy.isLClickPressed())
			recording = !recording;
		if (recording != prevRecording)
			GZFiles.getInstance().csvControl(kFiles.MP_NAME, kFiles.MP_FOLDER, kFiles.MP_USB, TASK.Record, recording);
	}

	public void addLoggingValues() {
		new LogItem("BATTERY-VOLTAGE") {
			@Override
			public String val() {
				return String.valueOf(RobotController.getBatteryVoltage());
			}
		};

		new LogItem("BROWNED-OUT") {
			@Override
			public String val() {
				return String.valueOf(RobotController.isBrownedOut());
			}
		};

		new LogItem("PDP-TEMP") {
			@Override
			public String val() {
				return String.valueOf(pdp.getTemperature());
			}
		};

		new LogItem("PDP-TEMP-AVG", true) {
			@Override
			public String val() {
				return LogItem.Average_Left_Formula;
			}
		};

		new LogItem("PDP-AMP") {
			@Override
			public String val() {
				return String.valueOf(pdp.getTotalCurrent());
			}
		};

		new LogItem("PDP-AMP-AVG", true) {
			@Override
			public String val() {
				return LogItem.Average_Left_Formula;
			}
		};

		new LogItem("PDP-VOLT") {

			@Override
			public String val() {
				return String.valueOf(pdp.getVoltage());
			}
		};

		new LogItem("PDP-VOLT-AVG", true) {
			@Override
			public String val() {
				return LogItem.Average_Left_Formula;
			}
		};

		new LogItem("DRIVE-STATE") {

			@Override
			public String val() {
				return Drive.getInstance().getStateString() + "-" + Drive.getInstance().isDisabed();
			}
		};
	}

	public boolean isSafteyDisabled() {
		return this.mSafteyDisable;
	}

	public void setSafteyDisable(boolean disable) {
		this.mSafteyDisable = disable;
	}

	@Override
	public void outputSmartDashboard() {
		// SmartDashboard.putString("Selected Auton",
		// Auton.getInstance().getAutonString());
		// SmartDashboard.putString("FIELD DATA", Auton.getInstance().gsm());
	}

	private static void rumble(double intensity) {
		driverJoy.rumble(intensity);
		opJoy.rumble(intensity);
	}

	public boolean isFMS() {
		return DriverStation.getInstance().isFMSAttached();
	}

	public boolean isRed() {
		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			return true;

		return false;
	}

	public double getMatchTime() {
		return DriverStation.getInstance().getMatchTime();
	}

	public boolean isAuto() {
		return DriverStation.getInstance().isAutonomous() && DriverStation.getInstance().isEnabled();
	}

	public boolean isDisabled() {
		return DriverStation.getInstance().isDisabled();
	}

	public boolean isEnabled() {
		return DriverStation.getInstance().isEnabled();
	}

	public boolean isTele() {
		return DriverStation.getInstance().isEnabled() && !isAuto() && !isTest();
	}

	public boolean isTest() {
		return DriverStation.getInstance().isTest();
	}

	public boolean wasTele() {
		return mWasTele;
	}

	public boolean wasAuto() {
		return mWasAuto;
	}

	public boolean wasTest() {
		return mWasTest;
	}

	@Override
	public String getStateString() {
		return "NA";
	}

	public void stop() {
	}

	protected void in() {
	}

	protected void out() {
	}

	protected void initDefaultCommand() {
	}
}