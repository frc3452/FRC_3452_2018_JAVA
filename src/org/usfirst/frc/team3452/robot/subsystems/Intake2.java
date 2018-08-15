package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kIntake;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain2.Values;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Intake2 extends GZSubsystem {

	private static Spark left_intake, right_intake;

	private IntakeState mState = IntakeState.NEUTRAL;

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

	public void setState(IntakeState wantedState) {
		if (this.isDisabed())
			mState = IntakeState.NEUTRAL;
		else if (wantedState != mState) {
			onStateExit(mState);
			onStateStart(wantedState);
			mState = wantedState;
		}
	}

	private void onStateStart(IntakeState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	private void onStateExit(IntakeState prevState) {
		switch (prevState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;

		}
	}

	public String getStateString()
	{
		return mState.toString();
	}
	
	public IntakeState getState() {
		return mState;
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

		this.inputOutput();
	}

	@Override
	public void in() {
		Values.left_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.INTAKE_L);
		Values.right_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.INTAKE_R);
	}

	@Override
	public void out() {
		left_intake.set(Values.left_output);
		right_intake.set(Values.right_output);
	}
	
	public void manual(double percentage) {
		setState(IntakeState.MANUAL);
		
		Values.left_desired_output = Values.right_desired_output = percentage;
	}
	
	public void spin(double percentage, boolean clockwise)
	{
		Values.left_desired_output = percentage * (clockwise ? -1 : 1);
		Values.right_desired_output = percentage  * (clockwise ? 1 : -1);
	}
	

	@Override
	public void stop() {
		mState = IntakeState.NEUTRAL;
	}

	public static class Values {
		// in
		static double left_amperage = -1;
		static double right_amperage = -1;

		// out
		static double left_output = 0;
		static double left_desired_output = 0;

		static double right_output = 0;
		static double right_desired_output = 0;
	}

	public enum IntakeState {
		NEUTRAL, MANUAL
	}

	@Override
	protected void initDefaultCommand() {
	}

}
