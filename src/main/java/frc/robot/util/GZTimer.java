package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean mHasOneTimeStarted = false;
	private String mName = "";
	
	public GZTimer(String name) {
		super();
		mName = name;
	}

	public void start() {
		if (!startTimer()) 
			System.out.println(this.getClass().getSimpleName() + " [" + getName() + "] cannot be started.");
	}
	
	public void oneTimeStart()
	{
		if (startTimer()) {
			mHasOneTimeStarted = true;
		}
	}
	
	private boolean startTimer()
	{
		if (!mHasOneTimeStarted)
		{
			this.stop();
			this.reset();
			this.start();
			return true;
		}
		return false;
	}

	public double get() {
		return this.get();
	}
	
	public String getName()
	{
		return mName;
	}

}
