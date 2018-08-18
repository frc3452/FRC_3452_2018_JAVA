package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kIntake;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Intake2 extends GZSubsystem {

	private static Spark left_intake, right_intake;

	private IntakeState mState = IntakeState.NEUTRAL;
	private IntakeState prevState = mState;

	// Construction
	public Intake2() {
		left_intake = new Spark(kIntake.INTAKE_L);
		right_intake = new Spark(kIntake.INTAKE_R);

		left_intake.setInverted(kIntake.INTAKE_L_INVERT);
		right_intake.setInverted(kIntake.INTAKE_R_INVERT);

		left_intake.setSubsystem(Intake2.class.getName());
		right_intake.setSubsystem(Intake2.class.getName());

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
		switch (mState) {
		case MANUAL:

			Values.left_output = Values.left_desired_output;
			Values.right_output = Values.right_desired_output;

			break;
		case NEUTRAL:

			Values.left_output = 0;
			Values.right_output = 0;

			break;
		default:
			System.out.println("WARNING: Incorrect intake state " + mState + " reached.");
			break;
		}
	}

	public static class Values {
		// in
		static double left_amperage = -1;
		static double right_amperage = -1;

		// out
		static private double left_output = 0;
		static double left_desired_output = 0;

		static private double right_output = 0;
		static double right_desired_output = 0;
	}

	@Override
	public synchronized void in() {
		Values.left_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.INTAKE_L);
		Values.right_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.INTAKE_R);
	}

	public void manual(double percentage) {
		setState(IntakeState.MANUAL);

		Values.left_desired_output = Values.right_desired_output = percentage;
	}

	public void spin(double percentage, boolean clockwise) {
		setState(IntakeState.MANUAL);
		Values.left_desired_output = percentage * (clockwise ? -1 : 1);
		Values.right_desired_output = percentage * (clockwise ? 1 : -1);
	}

	@Override
	public synchronized void out() {
		left_intake.set(Values.left_output);
		right_intake.set(Values.right_output);
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
		if (this.isDisabed())
			mState = IntakeState.NEUTRAL;
		else
			mState = wantedState;
	}
	
	public synchronized void checkPrevState()
	{
		if (mState != prevState)
		{
			onStateExit(prevState);
			onStateStart(mState);
		}
		prevState = mState;
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
