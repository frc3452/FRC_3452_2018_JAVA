package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean mHasOneTimeStarted = false;
	private String mName = "";

	private boolean mTiming = false;

	public GZTimer(String name) {
		super();
		mName = name;
	}

	public GZTimer()
	{
		this("Unspecified");
	}

	public void startTimer()
	{
		if (!this.mHasOneTimeStarted)
		{
			super.stop();
			super.reset();
			super.start();
			this.mTiming = true;
		}
	}

	public void oneTimeStartTimer() {
		if (!mHasOneTimeStarted) {
			super.stop();
			super.reset();
			super.start();
			this.mHasOneTimeStarted = true;
			this.mTiming = true;
		} else {
			System.out.println(this.getClass().getSimpleName() + " [" + getName() + "] cannot be started.");
		}
	}

	public void stopTimer()
	{
		super.stop();
		this.mTiming = false;
	}

	public boolean isTiming()
	{
		return this.mTiming;
	}

	public String getName() {
		return this.mName;
	}

}
