package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.kElevator;
import frc.robot.Constants.kFiles;
import frc.robot.Constants.kIntake;
import frc.robot.Constants.kOI;
import frc.robot.subsystems.Auton;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Drive.DriveState;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ESO;
import frc.robot.subsystems.Elevator.ElevatorState;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.util.GZFiles;
import frc.robot.util.GZFiles.TASK;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZJoystick.Buttons;
import frc.robot.util.GZLog.LogItem;
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
	private Elevator elev = Elevator.getInstance();
	private Intake intake = Intake.getInstance();
	private Climber climber = Climber.getInstance();

	private static GZOI mInstance = null;

	public static GZOI getInstance() {
		if (mInstance == null)
			mInstance = new GZOI();

		return mInstance;
	}

	private GZOI() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);
	}

	boolean recording = false;
	boolean prevRecording = recording;


	@Override
	public void loop() {
		outputSmartDashboard();

		if (isTele())
			mWasTele = true;
		if (isAuto())
			mWasAuto = true;
		if (isTest())
			mWasTest = true;

		if (isFMS())
			mSafteyDisable = false;
		else if (mUserButton.update(RobotController.getUserButton()))
			mSafteyDisable = !mSafteyDisable;

		Robot.allSubsystems.disable(mSafteyDisable);

		// if (driverJoy.areButtonsHeld(Arrays.asList(Buttons.A, Buttons.RB,
		// Buttons.LEFT_CLICK)))
		// Robot.auton.crash();

		if (driverJoy.isLClickPressed())
			recording = !recording;

		if (recording != prevRecording)
			GZFiles.getInstance().csvControl(kFiles.NAME, kFiles.FOLDER, kFiles.USB, TASK.Record, recording);

		if (isTele()) {
			Drive.getInstance().setWantedState(DriveState.OPEN_LOOP_DRIVER);

			// OVERRIDES, ETC.
			if (driverJoy.isAPressed())
				drive.slowSpeed(!drive.isSlow());
			if (driverJoy.isBackPressed())
				elev.setSpeedLimitingOverride(ESO.TOGGLE);
			if (opJoy.isRClickPressed())
				elev.overrideLimit(!elev.isLimitOverriden());

			// CLIMBER
			if (driverJoy.isYPressed())
				climber.addClimberCounter();
			if (driverJoy.getRawButton(Buttons.Y))
				climber.runClimber(1);
			else
				climber.stop();

			// ELEVATOR OPERATOR
			if (opJoy.getRawButton(Buttons.LB))
				elev.manualJoystick(opJoy);
			else if (driverJoy.getRawButton(Buttons.RB))
				elev.manualJoystick(driverJoy);
			else if (opJoy.isDDownPressed())
				elev.setHeight(kElevator.HeightsInches.Floor);
			else if (opJoy.isDRightPressed())
				elev.setHeight(kElevator.HeightsInches.Switch);
			// ELEVATOR DRIVER
			else if (driverJoy.isDDownPressed())
				elev.setHeight(kElevator.HeightsInches.Floor);
			else if (driverJoy.isDLeftPressed())
				elev.setHeight(kElevator.HeightsInches.Scale);
			else if (driverJoy.isDRightPressed())
				elev.setHeight(kElevator.HeightsInches.Switch);
			else if (elev.getState() != ElevatorState.POSITION)
				elev.stop();

			// INTAKE OPERATOR
			if (opJoy.getRawButton(Buttons.X))
				intake.manual(kIntake.Speeds.INTAKE);
			else if (opJoy.getRawButton(Buttons.Y))
				intake.manual(kIntake.Speeds.SLOW);
			else if (opJoy.getRawButton(Buttons.B))
				intake.manual(kIntake.Speeds.SHOOT);
			else if (opJoy.getRawButton(Buttons.A))
				intake.manual(kIntake.Speeds.PLACE);
			else if (opJoy.getRawButton(Buttons.BACK))
				intake.spin(false);
			else if (opJoy.getRawButton(Buttons.START))
				intake.spin(true);
			// INTAKE DRIVER
			else if (driverJoy.getRawButton(Buttons.X))
				intake.manual(kIntake.Speeds.INTAKE);
			else if (driverJoy.getRawButton(Buttons.B))
				intake.manual(kIntake.Speeds.SHOOT);
			else if (driverJoy.getDUp())
				intake.manual(kIntake.Speeds.SLOW);
			else if (driverJoy.getRawButton(Buttons.RIGHT_CLICK))
				intake.spin(true);
			else
				intake.stop();
		}

		// CONTROLLER RUMBLE

		if (GZUtil.between(getMatchTime(), 29.1, 30))
			// ENDGAME
			rumble(kOI.Rumble.ENDGAME);

		// LIMITING
		else if (elev.isSpeedOverriden() || elev.isLimitOverriden()) {

			if (elev.isSpeedOverriden()) {
				driverJoy.rumble(kOI.Rumble.ELEVATOR_SPEED_OVERRIDE_DRIVE);
			} else if (elev.isLimitOverriden())
				opJoy.rumble(kOI.Rumble.ELEVATOR_LIMIT_OVERRIDE);

			// INTAKE
		} else if (intake.stateNot(IntakeState.NEUTRAL) && !isAuto())
			rumble(kOI.Rumble.INTAKE);
		else
			rumble(0);

		prevRecording = recording;
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
				return Drive.getInstance().getPDPTemperature().toString();
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
				return Drive.getInstance().getPDPTotalCurrent().toString();
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
				return Drive.getInstance().getPDPVoltage().toString();
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

		new LogItem("ELEV-STATE") {
			@Override
			public String val() {
				return Elevator.getInstance().getStateString() + "-" + Elevator.getInstance().isDisabed();
			}
		};

		new LogItem("INTAKE-STATE") {
			@Override
			public String val() {
				return Intake.getInstance().getStateString() + "-" + Intake.getInstance().isDisabed();
			}
		};

		new LogItem("CLIMB-STATE") {
			@Override
			public String val() {
				return Climber.getInstance().getStateString() + "-" + Climber.getInstance().isDisabed();
			}
		};
	}

	public boolean isSafteyDisabled() {
		return this.mSafteyDisable;
	}

	public void setSafteyDisable(boolean disable)
	{
		this.mSafteyDisable = disable;
	}


	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putString("Selected Auton", Auton.getInstance().getAutonString());
		SmartDashboard.putString("FIELD DATA", Auton.getInstance().gsm());
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

	public boolean isEnabled()
	{
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