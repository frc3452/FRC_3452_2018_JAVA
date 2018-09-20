package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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
import frc.robot.util.Util;

public class GZOI extends GZSubsystem {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	private boolean mWasTele = false, mWasAuto = false, mWasTest = false;

	public GZOI() {

	}

	public synchronized void construct() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);
	}

	@Override
	public void loop() {
		if (isTele())
			mWasTele = true;
		if (isAuto())
			mWasAuto = true;
		if (isTest())
			mWasTest = true;

		// TODO ISSUE #19

		Robot.drive.setWantedState(DriveState.OPEN_LOOP_DRIVER);

		if (isTele()) {

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

			// ELEVATOR
			if (opJoy.getRawButton(Buttons.LB))
				Robot.elevator.manualJoystick(opJoy);
			else if (driverJoy.getRawButton(Buttons.RB))
				Robot.elevator.manualJoystick(driverJoy);
			else if (opJoy.isDDownPressed())
				Robot.elevator.encoder(kElevator.Heights.Floor);
			else if (opJoy.isDRightPressed())
				Robot.elevator.encoder(kElevator.Heights.Switch);
			else if (driverJoy.isDDownPressed())
				Robot.elevator.encoder(kElevator.Heights.Floor);
			else if (driverJoy.isDLeftPressed())
				Robot.elevator.encoder(kElevator.Heights.Scale);
			else if (driverJoy.isDRightPressed())
				Robot.elevator.encoder(kElevator.Heights.Switch);
			else if (Robot.elevator.getState() != ElevatorState.POSITION)
				Robot.elevator.stop();

			// INTAKE
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
			else if (driverJoy.getRawButton(Buttons.X))
				Robot.intake.manual(kIntake.Speeds.INTAKE);
			else if (driverJoy.getRawButton(Buttons.B))
				Robot.intake.manual(kIntake.Speeds.SHOOT);
			else if (driverJoy.getDUp())
				Robot.intake.manual(kIntake.Speeds.SLOW);
			else
				Robot.intake.stop();
		}

		// controller rumble
		if (Util.between(getMatchTime(), 29.1, 30))

			rumble(Controller.BOTH, kOI.Rumble.ENDGAME);

		else if (Robot.elevator.isSpeedOverriden() || Robot.elevator.isLimitOverriden()) {

			if (Robot.elevator.isSpeedOverriden()) {
				rumble(Controller.DRIVE, kOI.Rumble.ELEVATOR_SPEED_OVERRIDE_DRIVE);
			} else if (Robot.elevator.isLimitOverriden())
				rumble(Controller.OP, kOI.Rumble.ELEVATOR_LIMIT_OVERRIDE);

		} else if (Robot.intake.stateNot(IntakeState.NEUTRAL))
			rumble(Controller.BOTH, kOI.Rumble.INTAKE);
		else
			rumble(Controller.BOTH, 0);
	}

	private static void rumble(Controller joy, double intensity) {
		switch (joy) {
		case DRIVE:
			driverJoy.setRumble(RumbleType.kLeftRumble, intensity);
			driverJoy.setRumble(RumbleType.kRightRumble, intensity);
			break;
		case OP:
			opJoy.setRumble(RumbleType.kLeftRumble, intensity);
			opJoy.setRumble(RumbleType.kRightRumble, intensity);
			break;
		case BOTH:
			rumble(Controller.DRIVE, intensity);
			rumble(Controller.OP, intensity);
			break;
		}
	}

	private static enum Controller {
		DRIVE, OP, BOTH
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
		return DriverStation.getInstance().isAutonomous();
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