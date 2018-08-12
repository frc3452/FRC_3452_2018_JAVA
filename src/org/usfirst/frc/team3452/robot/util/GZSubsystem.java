package org.usfirst.frc.team3452.robot.util;

import edu.wpi.first.wpilibj.command.Subsystem;

//thx 254
public abstract class GZSubsystem extends Subsystem {
	// Stopping all motors
	public abstract void stop();
	
	//Saftey
	private boolean isDisabled = false;
	public void disable(boolean toDisable) { isDisabled = toDisable; }
	public boolean isDisabed() { return isDisabled; }
	
	//Main loop
	public abstract void loop();

	protected void inputOutput()
	{
		in();
		out();
	}
	
	// Read all inputs
	protected void in() {
	}
	
	// Write all outputs
	protected void out() {
	}

	// Zero sensors
	public void zeroSensors() {
	}
	
	//Write values to smart dashboard
	public abstract void outputSmartDashboard();

	
	
	
	
}
