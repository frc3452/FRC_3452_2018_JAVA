package frc.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import frc.robot.Constants;
import frc.robot.Constants.kWrist;
import frc.robot.Robot;
import frc.robot.subsystems.Health.AlertLevel;
import frc.robot.util.GZSRX;
import frc.robot.util.GZSRX.Breaker;
import frc.robot.util.GZSRX.Master;
import frc.robot.util.GZSubsystem;

public class Wrist extends GZSubsystem {

	private GZSRX wrist_1, wrist_2;

	private List<GZSRX> controllers;

	private WristState mState = WristState.MANUAL;
	private WristState mWantedState = WristState.NEUTRAL;

	public IO mIO = new IO();

	public Wrist() {
	}

	public void construct() {
		wrist_1 = new GZSRX(kWrist.W_1, Breaker.AMP_40, Master.MASTER);
		wrist_2 = new GZSRX(kWrist.W_2, Breaker.AMP_40, Master.FOLLOWER);

		controllers = Arrays.asList(wrist_1, wrist_2);

		talonInit(controllers);

		// FOLLOWER
		enableFollower();

		// INVERT
		wrist_1.setInverted(Constants.kElevator.E_1_INVERT);
		wrist_2.setInverted(Constants.kElevator.E_2_INVERT);

		GZSRX.logError(wrist_1.config_kF(0, kWrist.PID.F, 10), this, AlertLevel.WARNING, "Could not set 'F' gain");
		GZSRX.logError(wrist_1.config_kP(0, kWrist.PID.P, 10), this, AlertLevel.WARNING, "Could not set 'P' gain");
		GZSRX.logError(wrist_1.config_kI(0, kWrist.PID.I, 10), this, AlertLevel.WARNING, "Could not set 'I' gain");
		GZSRX.logError(wrist_1.config_kD(0, kWrist.PID.D, 10), this, AlertLevel.WARNING, "Could not set 'D' gain");
		GZSRX.logError(wrist_1.configOpenloopRamp(kWrist.OPEN_RAMP_TIME, 10), this, AlertLevel.WARNING,
				"Could not set open loop ramp");
		GZSRX.logError(wrist_1.configClosedloopRamp(kWrist.CLOSED_RAMP_TIME, 10), this, AlertLevel.WARNING,
				"Could not set closed loop ramp");

		// ENCODER
		GZSRX.logError(wrist_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10), this,
				AlertLevel.ERROR, "Could not setup encoder");

		GZSRX.logError(wrist_1.configForwardSoftLimitThreshold(angleToTicks(90)), this, AlertLevel.ERROR, "Could not setup encoder soft limit forward");
		GZSRX.logError(wrist_1.configReverseSoftLimitThreshold(angleToTicks(0)), this, AlertLevel.ERROR, "Could not setup encoder soft limit forward");

		GZSRX.logError(wrist_1.setSelectedSensorPosition(0, 0, 10), this, AlertLevel.WARNING, "Could not zero encoder");
		wrist_1.setSensorPhase(kWrist.ENC_INVERT);


		wrist_1.setName("Elev 1");
		wrist_2.setName("Elev 2");

		wrist_1.checkFirmware(this);
		wrist_1.checkFirmware(this);
	}

	private synchronized void talonInit(List<GZSRX> srx) {
		for (GZSRX s : srx) {
			GZSRX.logError(s.configFactoryDefault(), this, AlertLevel.ERROR,
					"Could not factory reset " + s.getMaster());

			s.setNeutralMode(NeutralMode.Brake);

			GZSRX.logError(s.configContinuousCurrentLimit(Constants.kWrist.AMP_LIMIT, 10), this, AlertLevel.WARNING,
					"Could not set current-limit limit for " + s.getMaster());
			GZSRX.logError(s.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10), this, AlertLevel.WARNING,
					"Could not set current-limit trigger for " + s.getMaster());
			GZSRX.logError(s.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10), this, AlertLevel.WARNING,
					"Could not set current-limit duration for " + s.getMaster());
			s.enableCurrentLimit(true);

			s.setSubsystem("Wrist");
		}
	}

	public enum WristState {
		NEUTRAL(false), MANUAL(false), POSITION(true);

		private final boolean usesClosedLoop;

		WristState(final boolean s) {
			usesClosedLoop = s;
		}
	}

	public void onStateStart(WristState s) {
		switch (s) {
		case NEUTRAL:
			break;
		case MANUAL:
			break;
		case POSITION:
			break;
		}
	}

	public void onStateExit(WristState s) {
		switch (s) {
		case NEUTRAL:
			break;
		case MANUAL:
			break;
		case POSITION:
			break;
		}
	}

	private void switchToState(WristState s) {
		if (mState != s) {
			onStateExit(mState);
			mState = s;
			onStateStart(mState);
		}
	}

	public void setPercentage(double power)
	{
		setWantedState(WristState.NEUTRAL);
		mIO.wrist_desired_output = power;
	}

	public void setAngle(double degrees)
	{
		setWantedState(WristState.POSITION);
		mIO.wrist_desired_output = (double) angleToTicks(degrees);
	}
	
	public Double getAngle()
	{
		return mIO.enc_ticks / kWrist.TICKS_PER_DEGREE;
	}


	public int angleToTicks(double angle)
	{
		return (int) (angle * kWrist.TICKS_PER_DEGREE);
	}

	public void enableSoftLimits(boolean enable)
	{
		GZSRX.logError(wrist_1.configForwardSoftLimitEnable(enable, GZSRX.TIMEOUT), this, AlertLevel.ERROR, "Could not " + ((enable) ? "enable" : "disable") +  " soft limits.");
		GZSRX.logError(wrist_1.configReverseSoftLimitEnable(enable, GZSRX.TIMEOUT), this, AlertLevel.ERROR, "Could not " + ((enable) ? "enable" : "disable") +  " soft limits.");
	}

	public void loop() {
		in();
		handleStates();
		out();
	}

	public void in() {
		mIO.wrist_1_amp = wrist_1.getOutputCurrent();
		mIO.wrist_2_amp = wrist_2.getOutputCurrent();

		mIO.wrist_1_volt = wrist_1.getMotorOutputVoltage();
		mIO.wrist_2_volt = wrist_2.getMotorOutputVoltage();

		mIO.encoderValid = wrist_1.isEncoderValid();
		if (mIO.encoderValid) {
			mIO.enc_ticks = (double) wrist_1.getSelectedSensorPosition(0);
			mIO.enc_vel = (double) wrist_1.getSelectedSensorVelocity(0);
		} else {
			mIO.enc_ticks = Double.NaN;
			mIO.enc_vel = Double.NaN;
		}
	}

	public void out() {
		switch(mState)
		{
			case MANUAL:
				mIO.control_mode = ControlMode.PercentOutput;
				mIO.wrist_output = mIO.wrist_desired_output;
			break;
			case POSITION:
				mIO.control_mode = ControlMode.Position;
				mIO.wrist_output = mIO.wrist_desired_output;
			break;
			case NEUTRAL:
				mIO.control_mode = ControlMode.PercentOutput;
				mIO.wrist_output = 0.0;
			break;
		}

		wrist_1.set(mIO.control_mode, mIO.wrist_output);
	}

	public void handleStates() {
		boolean neutral = false;
		neutral |= this.isDisabed() && !Robot.gzOI.isFMS();
		neutral |= mWantedState == WristState.NEUTRAL;
		neutral |= ((mState.usesClosedLoop || mWantedState.usesClosedLoop) && !mIO.encoderValid);

		if (neutral) {
			switchToState(WristState.NEUTRAL);
		} else {
			switchToState(mWantedState);
		}
	}

	public synchronized void setWantedState(WristState wantedState) {
		this.mWantedState = wantedState;
	}

	public String getStateString() {
		return mState.toString();
	}

	public class IO {
		// in
		public Double wrist_1_amp = Double.NaN, wrist_2_amp = Double.NaN;
		public Double wrist_1_volt = Double.NaN, wrist_2_volt = Double.NaN;

		public Double enc_ticks = Double.NaN, enc_vel = Double.NaN;
		public Boolean encoderValid = false;

		// out
		private double wrist_output = 0;
		public Double wrist_desired_output = 0.0;

		ControlMode control_mode = ControlMode.PercentOutput;
	}

	public void enableFollower() {
		wrist_2.follow(wrist_1);
	}

	public void stop() {
		setWantedState(WristState.NEUTRAL);
	}

	public void initDefaultCommand() {
	}
}