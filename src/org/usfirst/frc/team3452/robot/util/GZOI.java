package org.usfirst.frc.team3452.robot.util;

import org.usfirst.frc.team3452.robot.Constants.kOI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Intake.IntakeState;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class GZOI extends GZSubsystem {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	public GZOI() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);
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

	@Override
	public void loop() {
		if (Robot.auton.matchTimer.get() >= 29 && Robot.auton.matchTimer.get() <= 30)
			rumble(Controller.BOTH, kOI.ENDGAME);
		else if (Robot.elevator.isOverriden()) {
			rumble(Controller.DRIVE, kOI.ELEVATOR_OVERRIDE_DRIVE);
			rumble(Controller.OP, kOI.ELEVATOR_OVERRIDE_OP);
		} else if (Robot.intake.stateNot(IntakeState.NEUTRAL))
			rumble(Controller.BOTH, kOI.INTAKE);
		else
			rumble(Controller.BOTH, 0);
	}

	@Override
	public String getStateString() {
		return super.getName() + " no state.";
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