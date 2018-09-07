package org.usfirst.frc.team3452.robot.util;

import edu.wpi.first.wpilibj.command.Subsystem;

//thx 254
public abstract class GZSubsystem extends Subsystem {

	// Set to neutral
	public abstract void stop();

	// IO class
	// enum State
	// void setWantedState (State s)
	// boolean currentStateIsNot(State s)
	// State getState
	// void onStateStart (State s)
	// void onStateExit (State s)

	// Saftey
	private boolean mIsDisabled = false;

	// Record subsystem locked out and stop subsystem.
	public void disable(boolean toDisable) {
		mIsDisabled = toDisable;
		if (toDisable)
			stop();
	}
	
//	public abstract void checkHealth();

	// Return if subsystem is disabled or not.
	public Boolean isDisabed() {
		return mIsDisabled;
	}
	
	public abstract void loop();

	public void enableFollower() {
	}

	// Each subsystem is able to report its current state as a string
	public abstract String getStateString();

	// Read all inputs
	protected abstract void in();

	// Write all outputs
	protected abstract void out();

	// Zero sensors
	public void zeroSensors() {
	}

	// Write values to smart dashboard
	public void outputSmartDashboard() {
	}
}
