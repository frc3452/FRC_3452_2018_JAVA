package frc.robot.util;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.subsystems.Health.AlertLevel;
import frc.robot.Constants.*;

//thx 254
public abstract class GZSubsystem extends Subsystem {

	// Set to neutral
	public abstract void stop();

	private AlertLevel mHighestAlert = AlertLevel.NONE;

	// IO class
	// enum State
	// void setWantedState (State s)
	// State getState
	// void onStateStart (State s)
	// void onStateExit (State s)
	// void switchToState (State s)

	/**
	 * Disabling each subsystem
	 */
	private boolean mIsDisabled = false;

	public void disable(boolean toDisable) {
		mIsDisabled = toDisable;
		if (mIsDisabled)
			stop();
	}

	public Boolean isDisabed() {
		return mIsDisabled;
	}

	/**
	 * Looping
	 */
	private Notifier loopNotifier = new Notifier(new Runnable() {
		public void run() {
			loop();
		}
	});

	public abstract void loop();

	public void startLooping() {
		loopNotifier.startPeriodic(kLoop.LOOP_SPEED);
	}

	// Each subsystem is able to report its current state as a string
	public abstract String getStateString();

	// For Health generater
	public void setHighestAlert(AlertLevel level) {
		mHighestAlert = level;
	}
	public AlertLevel getHighestAlert() {
		return mHighestAlert;
	}

	// Read all inputs
	protected abstract void in();

	// Write all outputs
	protected abstract void out();

	public void enableFollower() {
	}

	// Zero sensors
	public void zeroSensors() {
	}

	// Write values to smart dashboard
	public void outputSmartDashboard() {
	}
}
