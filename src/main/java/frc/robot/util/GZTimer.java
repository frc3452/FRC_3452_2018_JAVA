package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean mHasOneTimeStarted = false;
	private String mName = "";

	public GZTimer(String name) {
		super();
		mName = name;
	}

	public void startTimer()
	{
		if (!mHasOneTimeStarted)
		{
			super.stop();
			super.reset();
			super.start();
		}
	}

	public void oneTimeStartTimer() {
		if (!mHasOneTimeStarted) {
			super.stop();
			super.reset();
			super.start();
			mHasOneTimeStarted = true;
		} else {
			System.out.println(this.getClass().getSimpleName() + " [" + getName() + "] cannot be started.");
		}
	}

	public String getName() {
		return mName;
	}

}
