package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants.kElevator;
import frc.robot.Constants.kIntake;
import frc.robot.Constants.kOI;
import frc.robot.subsystems.Drive.DriveState;
import frc.robot.subsystems.Elevator.ESO;
import frc.robot.subsystems.Elevator.ElevatorState;
import frc.robot.subsystems.Intake.IntakeState;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZJoystick.Buttons;
import frc.robot.util.GZSubsystem;
import frc.robot.util.LatchedBoolean;
import frc.robot.util.Util;

public class GZOI extends GZSubsystem {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	private boolean mWasTele = false, mWasAuto = false, mWasTest = false;

	private LatchedBoolean mUserButton = new LatchedBoolean();
	private boolean mSafteyDisable = false;

	public GZOI() {

	}

	public synchronized void construct() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);
	}

	@Override
	public void loop() {
		outputSmartDashboard();

		if (isTele())
			mWasTele = true;
		if (isAuto())
			mWasAuto = true;
		if (isTest())
			mWasTest = true;

		if (isFms())
			mSafteyDisable = false;
		else if (mUserButton.update(RobotController.getUserButton()))
			mSafteyDisable = !mSafteyDisable;
		
		Robot.allSubsystems.disable(mSafteyDisable);
		
		// if (driverJoy.areButtonsHeld(Arrays.asList(Buttons.A, Buttons.RB,
		// Buttons.LEFT_CLICK)))
		// Robot.auton.crash();

		if (isTele()) {
			Robot.drive.setWantedState(DriveState.OPEN_LOOP_DRIVER);

			// OVERRIDES, ETC.
			if (driverJoy.isAPressed())
				Robot.drive.slowSpeed(!Robot.drive.isSlow());
			if (driverJoy.isBackPressed())
				Robot.elevator.setSpeedLimitingOverride(ESO.TOGGLE);
			if (opJoy.isRClickPressed())
				Robot.elevator.overrideLimit(!Robot.elevator.isLimitOverriden());

			// CLIMBER
			if (driverJoy.isYPressed())
				Robot.climber.addClimberCounter();
			if (driverJoy.getRawButton(Buttons.Y))
				Robot.climber.runClimber(1);
			else
				Robot.climber.stop();

			// ELEVATOR OPERATOR
			if (opJoy.getRawButton(Buttons.LB))
				Robot.elevator.manualJoystick(opJoy);
			else if (driverJoy.getRawButton(Buttons.RB))
				Robot.elevator.manualJoystick(driverJoy);
			else if (opJoy.isDDownPressed())
				Robot.elevator.setHeight(kElevator.HeightsInches.Floor);
			else if (opJoy.isDRightPressed())
				Robot.elevator.setHeight(kElevator.HeightsInches.Switch);
			// ELEVATOR DRIVER
			else if (driverJoy.isDDownPressed())
				Robot.elevator.setHeight(kElevator.HeightsInches.Floor);
			else if (driverJoy.isDLeftPressed())
				Robot.elevator.setHeight(kElevator.HeightsInches.Scale);
			else if (driverJoy.isDRightPressed())
				Robot.elevator.setHeight(kElevator.HeightsInches.Switch);
			else if (Robot.elevator.getState() != ElevatorState.POSITION)
				Robot.elevator.stop();

			// INTAKE OPERATOR
			if (opJoy.getRawButton(Buttons.X))
				Robot.intake.manual(kIntake.Speeds.INTAKE);
			else if (opJoy.getRawButton(Buttons.Y))
				Robot.intake.manual(kIntake.Speeds.SLOW);
			else if (opJoy.getRawButton(Buttons.B))
				Robot.intake.manual(kIntake.Speeds.SHOOT);
			else if (opJoy.getRawButton(Buttons.A))
				Robot.intake.manual(kIntake.Speeds.PLACE);
			else if (opJoy.getRawButton(Buttons.BACK))
				Robot.intake.spin(false);
			else if (opJoy.getRawButton(Buttons.START))
				Robot.intake.spin(true);
			// INTAKE DRIVER
			else if (driverJoy.getRawButton(Buttons.X))
				Robot.intake.manual(kIntake.Speeds.INTAKE);
			else if (driverJoy.getRawButton(Buttons.B))
				Robot.intake.manual(kIntake.Speeds.SHOOT);
			else if (driverJoy.getDUp())
				Robot.intake.manual(kIntake.Speeds.SLOW);
			else if (driverJoy.getRawButton(Buttons.RIGHT_CLICK))
				Robot.intake.spin(true);
			else
				Robot.intake.stop();
		}

		// CONTROLLER RUMBLE

		if (Util.between(getMatchTime(), 29.1, 30))
			// ENDGAME
			rumble(kOI.Rumble.ENDGAME);

		// LIMITING
		else if (Robot.elevator.isSpeedOverriden() || Robot.elevator.isLimitOverriden()) {

			if (Robot.elevator.isSpeedOverriden()) {
				driverJoy.rumble(kOI.Rumble.ELEVATOR_SPEED_OVERRIDE_DRIVE);
			} else if (Robot.elevator.isLimitOverriden())
				opJoy.rumble(kOI.Rumble.ELEVATOR_LIMIT_OVERRIDE);

			// INTAKE
		} else if (Robot.intake.stateNot(IntakeState.NEUTRAL) && !isAuto())
			rumble(kOI.Rumble.INTAKE);
		else
			rumble(0);
	}

	public boolean isSafteyDisabled() {
		return mSafteyDisable;
	}

	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putString("Selected Auton", Robot.auton.getAutonString());
		SmartDashboard.putString("FIELD DATA", Robot.auton.gsm());
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