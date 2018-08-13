package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kClimber;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Climber2 extends GZSubsystem {

	private static Spark climber_1;

	private static ClimberState mState = ClimberState.NEUTRAL;

	// Construction
	public Climber2() {
		climber_1 = new Spark(kClimber.CLIMBER_1);
		climber_1.setInverted(kClimber.CLIMBER_1_INVERT);

		climber_1.setSubsystem(Climber2.class.getName());
		climber_1.setName("climber_1");
	}

	public void switchState(ClimberState wantedState) {
		if (this.isDisabed())
			mState = ClimberState.NEUTRAL;
		else if (wantedState != mState) {
			onStateExit(mState);
			onStateStart(wantedState);
			mState = wantedState;
		}
	}

	private void onStateStart(ClimberState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	private void onStateExit(ClimberState prevState) {
		switch (prevState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	public ClimberState getState() {
		return mState;
	}

	@Override
	public synchronized void loop() {
		switch (mState) {

		case MANUAL:
			Values.climber_output = Values.climber_desired_output;
			break;

		case NEUTRAL:
			Values.climber_output = 0;
			break;

		default:
			System.out.println("WARNING: Incorrect climber state " + mState + " reached.");
			break;
		}

		this.inputOutput();
	}

	@Override
	public void stop() {
		mState = ClimberState.NEUTRAL;
	}

	@Override
	protected void in() {
		Values.climber_1_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.CLIMBER_1);
		Values.climber_2_amperage = Robot.drive2.getPDPChannelCurrent(kPDP.CLIMBER_2);
	}

	@Override
	protected void out() {
		climber_1.set(Math.abs(Values.climber_output));
	}

	public enum ClimberState {
		NEUTRAL, MANUAL,
	}

	public static class Values {
		// in
		static double climber_1_amperage = 0;
		static double climber_2_amperage = 0;

		// out
		static double climber_output = 0;
		static double climber_desired_output = 0;
	}

	@Override
	protected void initDefaultCommand() {
	}

}
