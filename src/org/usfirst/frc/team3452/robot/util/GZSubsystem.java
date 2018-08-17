package org.usfirst.frc.team3452.robot.util;

import edu.wpi.first.wpilibj.command.Subsystem;

//thx 254
public abstract class GZSubsystem extends Subsystem {
	
	
	// Set to neutral
	public abstract void stop();

	// Values class
	// State
	// void setState (State s)
	// State getState
	// void onStateStart (State s)
	// void onStateExit (State s)

	// Saftey
	private boolean isDisabled = false;

	//Record subsystem locked out and stop subsystem.
	public void disable(boolean toDisable) {
		isDisabled = toDisable;
		if (toDisable)
			stop();
	}

	//Return if subsystem is disabled or not.
	public boolean isDisabed() {
		return isDisabled;
	}

	//Each subsystem is able to report its current state as a string
	public abstract String getStateString();
	
	// Main loop
	public abstract void loop();

	//Calls input and output lines
	protected synchronized void inputOutput() {
		in();
		out();
	}

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
