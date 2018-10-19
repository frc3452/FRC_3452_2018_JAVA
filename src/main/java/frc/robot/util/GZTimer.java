package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class GZTimer extends Timer {

	private boolean mHasOneTimeStarted = false;
	private String mName = "";

	public GZTimer(String name) {
		super();
		mName = name;
	}

	public void startTimer() {
		if (!mHasOneTimeStarted)
		{
			this.stop();
			this.reset();
			this.start();
		}
	}

	public void oneTimeStartTimer() {
		if (!mHasOneTimeStarted) {
			this.stop();
			this.reset();
			this.startTimer();
			mHasOneTimeStarted = true;
		} else {
			System.out.println(this.getClass().getSimpleName() + " [" + getName() + "] cannot be started.");
		}
	}

	public double get() {
		return this.get();
	}

	public String getName() {
		return mName;
	}

}
