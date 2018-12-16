package frc.robot.util;

import java.util.HashMap;
import java.util.Map;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Constants.kLoop;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Health.AlertLevel;

//thx 254
public abstract class GZSubsystem extends Subsystem {

	// Set to neutral
	public abstract void stop();

	private AlertLevel mHighestAlert = AlertLevel.NONE;

	protected Map<Integer, GZSRX> mTalons = new HashMap<Integer, GZSRX>();
	protected Map<Integer, GZSpark> mSparks = new HashMap<Integer, GZSpark>();

	/**
	 * Disabling each subsystem
	 */
	private boolean mIsDisabled = false;

	public void disable(boolean toDisable) {
		mIsDisabled = toDisable;
		if (mIsDisabled)
			stop();
	}

	public abstract void addLoggingValues();

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
