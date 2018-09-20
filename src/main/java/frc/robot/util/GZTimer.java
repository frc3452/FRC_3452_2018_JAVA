package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean mHasStarted = false;
	private String mName = "";
	
	public GZTimer(String name) {
		super();
		mName = name;
	}

	public void start() {
		if (!mHasStarted) {
			this.stop();
			this.reset();
			this.start();
		} else {
			System.out.println(this.getClass().getName() + " " + getName() + " cannot be started.");
		}
	}
	
	public void oneTimeStart()
	{
		mHasStarted = true;
		this.stop();
		this.reset();
		this.start();
	}
	
	public double get() {
		return this.get();
	}
	
	public String getName()
	{
		return mName;
	}

}
