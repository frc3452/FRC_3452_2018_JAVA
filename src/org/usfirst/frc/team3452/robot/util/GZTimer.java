package org.usfirst.frc.team3452.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean hasStarted = false;

	public GZTimer() {
		super();
	}

	public void start() {
		if (!hasStarted) {
			this.stop();
			this.reset();
			this.start();
		} else
			System.out.println("Cannot start " + this.getClass().getName() + ".");
	}
	
	public void onlyStart()
	{
		hasStarted = true;
		this.stop();
		this.reset();
		this.start();
	}

	public double get() {
		return this.get();
	}

}
