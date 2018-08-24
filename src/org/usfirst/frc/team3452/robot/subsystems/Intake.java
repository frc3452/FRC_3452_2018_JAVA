package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kIntake;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Intake extends GZSubsystem {

	private static Spark left_intake, right_intake;

	private IntakeState mState = IntakeState.NEUTRAL;
	public IO mIO = new IO();

	// Construction
	public Intake() {
		left_intake = new Spark(kIntake.INTAKE_L);
		right_intake = new Spark(kIntake.INTAKE_R);

		left_intake.setInverted(kIntake.INTAKE_L_INVERT);
		right_intake.setInverted(kIntake.INTAKE_R_INVERT);

		left_intake.setSubsystem(Intake.class.getName());
		right_intake.setSubsystem(Intake.class.getName());

		left_intake.setName("left_intake");
		right_intake.setName("right_intake");
	}

	private synchronized void onStateStart(IntakeState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	private synchronized void onStateExit(IntakeState prevState) {
		switch (prevState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;

		}
	}

	public synchronized void loop() {
		in();
		out();

		switch (mState) {
		case MANUAL:

			IO.left_output = IO.left_desired_output;
			IO.right_output = IO.right_desired_output;

			break;
		case NEUTRAL:

			IO.left_output = 0;
			IO.right_output = 0;

			break;
		default:
			System.out.println("WARNING: Incorrect intake state " + mState + " reached.");
			break;
		}
	}

	public static class IO {
		// in
		static double left_amperage = Double.NaN;
		static double right_amperage = Double.NaN;

		// out
		static private double left_output = 0;
		static double left_desired_output = 0;

		static private double right_output = 0;
		static double right_desired_output = 0;
	}

	@Override
	public synchronized void in() {
		IO.left_amperage = Robot.drive.getPDPChannelCurrent(kPDP.INTAKE_L);
		IO.right_amperage = Robot.drive.getPDPChannelCurrent(kPDP.INTAKE_R);
	}

	public void manual(double percentage) {
		setState(IntakeState.MANUAL);
		IO.left_desired_output = IO.right_desired_output = percentage;
	}

	public void spin(double percentage, boolean clockwise) {
		setState(IntakeState.MANUAL);
		IO.left_desired_output = percentage * (clockwise ? -1 : 1);
		IO.right_desired_output = percentage * (clockwise ? 1 : -1);
	}

	@Override
	public synchronized void out() {
		left_intake.set(IO.left_output);
		right_intake.set(IO.right_output);
	}

	public enum IntakeState {
		NEUTRAL, MANUAL
	}

	@Override
	public synchronized void stop() {
		setState(IntakeState.NEUTRAL);
	}

	public synchronized void setState(IntakeState wantedState) {
		// we dont need to worry about isDemo() here, we don't change anything
		if (this.isDisabed()) {
			onStateStart(mState);
			mState = IntakeState.NEUTRAL;
			onStateExit(mState);
		} else {
			onStateStart(mState);
			mState = wantedState;
			onStateExit(mState);

		}
	}

	public String getStateString() {
		return mState.toString();
	}

	public IntakeState getState() {
		return mState;
	}

	@Override
	protected void initDefaultCommand() {
	}

}
