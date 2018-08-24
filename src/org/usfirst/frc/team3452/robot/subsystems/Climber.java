package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kClimber;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Climber extends GZSubsystem {

	private Spark climber_1;

	private ClimberState mState = ClimberState.NEUTRAL;
	private ClimberState prevState = mState;
	public IO mIO = new IO();

	private int climbCounter = 0;

	// Construction
	public Climber() {
		climber_1 = new Spark(kClimber.CLIMBER_1);
		climber_1.setInverted(kClimber.CLIMBER_1_INVERT);

		climber_1.setSubsystem(Climber.class.getName());
		climber_1.setName("climber_1");
	}

	private synchronized void onStateStart(ClimberState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	private synchronized void onStateExit(ClimberState prevState) {
		switch (prevState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	@Override
	public synchronized void loop() {
		in();
		out();

		switch (mState) {

		case MANUAL:
			mIO.climber_output = mIO.climber_desired_output;
			break;

		case NEUTRAL:
			mIO.climber_output = 0;
			break;

		default:
			System.out.println("WARNING: Incorrect climber state " + mState + " reached.");
			break;
		}
	}

	public static class IO {
		// in
		double climber_1_amperage = Double.NaN;
		double climber_2_amperage = Double.NaN;

		// out
		private double climber_output = 0;
		double climber_desired_output = 0;
	}

	public void manual(double percentage) {
		setState(ClimberState.MANUAL);
		mIO.climber_desired_output = percentage;
	}

	@Override
	protected synchronized void in() {
		mIO.climber_1_amperage = Robot.drive.getPDPChannelCurrent(kPDP.CLIMBER_1);
		mIO.climber_2_amperage = Robot.drive.getPDPChannelCurrent(kPDP.CLIMBER_2);
	}

	@Override
	protected synchronized void out() {
		climber_1.set(Math.abs(mIO.climber_output));
	}

	public enum ClimberState {
		NEUTRAL, MANUAL,
	}

	@Override
	public synchronized void stop() {
		setState(ClimberState.NEUTRAL);
	}

	public synchronized void setState(ClimberState wantedState) {
		if (this.isDisabed() || Robot.autonSelector.isDemo()) {
			onStateExit(mState);
			mState = ClimberState.NEUTRAL;
			onStateStart(mState);
		} else {
			onStateExit(mState);
			mState = wantedState;
			onStateStart(mState);
		}
	}

	public synchronized void checkPrevState() {
		if (mState != prevState) {
			onStateExit(prevState);
			onStateStart(mState);
		}
		prevState = mState;
	}

	public String getStateString() {
		return mState.toString();
	}

	public ClimberState getState() {
		return mState;
	}

	@Override
	protected void initDefaultCommand() {
	}

	public int getClimbCounter() {
		return climbCounter;
	}

	public void addClimberCounter() {
		this.climbCounter += 1;
	}

}
