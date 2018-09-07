package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kClimber;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;

public class Climber extends GZSubsystem {

	private Spark climber_1;

	private ClimberState mState = ClimberState.NEUTRAL;
	private ClimberState mWantedState = mState;
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
		handleStates();
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

	static class IO {
		// in
		double climber_1_amperage = Double.NaN;
		double climber_2_amperage = Double.NaN;

		// out
		private double climber_output = 0;
		double climber_desired_output = 0;
	}

	public void manual(double percentage) {
		setWantedState(ClimberState.MANUAL);
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
		setWantedState(ClimberState.NEUTRAL);
	}

	public synchronized void setWantedState(ClimberState wantedState) {
		this.mWantedState = wantedState;
	}
	
	public synchronized void checkHealth()
	{
		
	}

	private synchronized void handleStates() {
		//if trying to disable or run demo mode while not connected to field
		if (((this.isDisabed() || Robot.auton.isDemo()) && !Robot.gzOI.isFMS())
				|| mWantedState == ClimberState.NEUTRAL) {

			if (stateNot(ClimberState.NEUTRAL)) {
				onStateExit(mState);
				mState = ClimberState.NEUTRAL;
				onStateStart(mState);
			}

		} else if (mState != mWantedState) {
			onStateExit(mState);
			mState = mWantedState;
			onStateStart(mState);
		}
	}

	private synchronized boolean stateNot(ClimberState state) {
		return mState != state;
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
