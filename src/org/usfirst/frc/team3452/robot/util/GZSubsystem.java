package org.usfirst.frc.team3452.robot.util;

import edu.wpi.first.wpilibj.command.Subsystem;

//thx 254
public abstract class GZSubsystem extends Subsystem {
	// Stopping all motors
	public abstract void stop();

	// Values class
	// State
	// void setState (State s)
	// State getState
	// void onStateStart (State s)
	// void onStateExit (State s)

	// Saftey
	private boolean isDisabled = false;

	public void disable(boolean toDisable) {
		isDisabled = toDisable;
		if (toDisable)
			stop();
	}

	public boolean isDisabed() {
		//TODO 2) TEST IF ALL SUBSYSTEMS ARE USING THESE VARIABLES INDEPENDTLY
		return isDisabled;
	}

	// Main loop
	public abstract void loop();

	protected void inputOutput() {
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
