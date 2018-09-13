package frc.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import frc.robot.Constants;
import frc.robot.Constants.kElevator;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.Health.AlertLevel;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZSRX;
import frc.robot.util.GZSRX.Breaker;
import frc.robot.util.GZSRX.Master;
import frc.robot.util.GZSubsystem;
import frc.robot.util.Units;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends GZSubsystem {

	private static GZSRX elevator_1, elevator_2;
	private List<GZSRX> controllers;

	// Force switch state to neutral on start up
	private ElevatorState mState = ElevatorState.MANUAL;
	private ElevatorState mWantedState = ElevatorState.NEUTRAL;
	public IO mIO = new IO();

	private double driveModifier = 0;
	private double target = 0;
	private boolean mIsOverriden = false;

	public Elevator() {
	}

	public synchronized void construct() {
		elevator_1 = new GZSRX(kElevator.E_1, Breaker.AMP_40, Master.MASTER);
		elevator_2 = new GZSRX(kElevator.E_2, Breaker.AMP_40, Master.FOLLOWER);

		controllers = Arrays.asList(elevator_1, elevator_2);

		talonInit(controllers);

		// FOLLOWER
		elevator_2.follow(elevator_1);

		// INVERT
		elevator_1.setInverted(Constants.kElevator.E_1_INVERT);
		elevator_2.setInverted(Constants.kElevator.E_2_INVERT);

		GZSRX.logError(elevator_1.config_kF(0, kElevator.PID.F, 10), this,
				AlertLevel.WARNING, "Could not set 'F' gain");
		GZSRX.logError(elevator_1.config_kP(0, kElevator.PID.P, 10), this,
				AlertLevel.WARNING, "Could not set 'P' gain");
		GZSRX.logError(elevator_1.config_kI(0, kElevator.PID.I, 10), this,
				AlertLevel.WARNING, "Could not set 'I' gain");
		GZSRX.logError(elevator_1.config_kD(0, kElevator.PID.D, 10), this,
				AlertLevel.WARNING, "Could not set 'D' gain");
		GZSRX.logError(elevator_1.configOpenloopRamp(Constants.kElevator.OPEN_RAMP_TIME, 10), Robot.elevator,
				AlertLevel.WARNING, "Could not set open loop ramp");
		GZSRX.logError(elevator_1.configClosedloopRamp(Constants.kElevator.CLOSED_RAMP_TIME, 10), this,
				AlertLevel.WARNING, "Could not set closed loop ramp");

		// TODO ISSUE #11
		// ENCODER
		GZSRX.logError(elevator_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10), this,
				AlertLevel.ERROR, "Could not find encoder");

		GZSRX.logError(elevator_1.setSelectedSensorPosition(0, 0, 10), this,
				AlertLevel.WARNING, "Could not zero encoder");
		elevator_1.setSensorPhase(Constants.kElevator.ENC_INVERT);

		// SOFT LIMITS FOR DEMO MODE
		GZSRX.logError(
				elevator_1.configForwardSoftLimitThreshold(Units.rotations_to_ticks(-kElevator.LOWER_SOFT_LIMIT)), this,
				AlertLevel.WARNING, "Could not set lower soft limit");
		GZSRX.logError(
				elevator_1.configReverseSoftLimitThreshold(Units.rotations_to_ticks(-kElevator.UPPER_SOFT_LIMIT)), this,
				AlertLevel.WARNING, "Could not set upper soft limit");

		// RESET ENCODER ON LIMIT SWITCH DOWN
		GZSRX.logError(elevator_1.configClearPositionOnLimitF(true, 10), this,
				AlertLevel.ERROR, "Could not set encoder zero on bottom limit");

		// REMOTE LIMIT SWITCHES
		GZSRX.logError(
				elevator_1.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
						LimitSwitchNormal.NormallyClosed, elevator_2.getDeviceID(), 10),
				this, AlertLevel.ERROR, "Could not set forward limit switch");
		GZSRX.logError(
				elevator_1.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
						LimitSwitchNormal.NormallyClosed, elevator_2.getDeviceID(), 10),
				this, AlertLevel.ERROR, "Could not set reverse limit switch");
		
		in();
		if (getUpLmtLimit() && getDownLmtSwitch())
			Robot.health.addAlert(this, AlertLevel.ERROR, "Both limit switches tripped");
		if (!getDownLmtLimit())
			Robot.health.addAlert(this, AlertLevel.WARNING, "Bottom limit not tripped.")
		
		softLimits(false);

		elevator_1.setName("Elev 1");
		elevator_2.setName("Elev 2");

	}

	private synchronized void talonInit(List<GZSRX> srx) {
		for (GZSRX s : srx) {
			GZSRX.logError(s.configFactoryDefault(), this, AlertLevel.ERROR,
					"Could not factory reset " + s.getMaster());

			s.setNeutralMode(NeutralMode.Brake);

			GZSRX.logError(s.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10), Robot.elevator,
					AlertLevel.WARNING, "Could not set current-limit limit for " + s.getMaster());
			GZSRX.logError(s.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10), Robot.elevator,
					AlertLevel.WARNING, "Could not set current-limit trigger for " + s.getMaster());
			GZSRX.logError(s.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10), Robot.elevator,
					AlertLevel.WARNING, "Could not set current-limit duration for " + s.getMaster());
			s.enableCurrentLimit(true);

			s.setSubsystem("Elevator");
		}
	}

	private synchronized void onStateStart(ElevatorState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		case POSITION:
			break;
		case DEMO:

			if (kElevator.USE_SOFT_LIMITS)
				softLimits(true);

			break;
		default:
			break;
		}
	}

	private synchronized void onStateExit(ElevatorState prevState) {
		switch (prevState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		case POSITION:
			encoderDone();
			break;
		case DEMO:
			softLimits(false);
			break;
		default:
			break;
		}
	}

	@Override
	public synchronized void loop() {
		handleStates();
		in();
		out();

		switch (mState) {
		case MANUAL:

			mIO.control_mode = ControlMode.PercentOutput;
			mIO.output = mIO.desired_output;

			break;
		case NEUTRAL:

			mIO.control_mode = ControlMode.Disabled;
			mIO.output = 0;

			break;
		case POSITION:
			mIO.control_mode = ControlMode.Position;
			mIO.output = mIO.desired_output;
			break;
		case DEMO:
			mIO.control_mode = ControlMode.PercentOutput;
			mIO.output = mIO.desired_output;
			break;

		default:
			System.out.println("WARNING: Incorrect elevator state " + mState + " reached.");
			break;
		}

		speedLimiting();
	}

	public static class IO {
		// in
		public Double encoder_ticks = Double.NaN, encoder_vel = Double.NaN;

		public Double elevator_1_amp = Double.NaN, elevator_2_amp = Double.NaN;

		public Double elevator_1_volt = Double.NaN, elevator_2_volt = Double.NaN;

		public Boolean elevator_fwd_lmt = false, elevator_rev_lmt = false;

		// out
		private double output = 0;
		public double desired_output = 0;

		ControlMode control_mode = ControlMode.PercentOutput;
	}

	public Double getRotations() {
		return Units.ticks_to_rotations(mIO.encoder_ticks);
	}

	public Double getSpeed() {
		return Units.ticks_to_rotations(mIO.encoder_vel);
	}

	@Override
	protected synchronized void in() {
		mIO.encoder_ticks = (double) elevator_1.getSelectedSensorPosition(0);
		mIO.encoder_vel = (double) elevator_1.getSelectedSensorVelocity(0);

		mIO.elevator_1_amp = elevator_1.getOutputCurrent();
		mIO.elevator_2_amp = elevator_2.getOutputCurrent();

		mIO.elevator_1_volt = elevator_1.getMotorOutputVoltage();
		mIO.elevator_2_volt = elevator_2.getMotorOutputVoltage();

		mIO.elevator_fwd_lmt = elevator_2.getSensorCollection().isFwdLimitSwitchClosed();
		mIO.elevator_rev_lmt = elevator_2.getSensorCollection().isRevLimitSwitchClosed();
	}

	public synchronized Boolean getUpLmtLimit()
	{
		return mIO.elevator_fwd_lmt;
	}

	public synchronized Boolean getDownLmtSwitch()
	{
		return mIO.elevator_rev_lmt;
	}

	@Override
	protected synchronized void out() {
		elevator_1.set(mIO.control_mode, mIO.output);
	}

	public enum ElevatorState {
		NEUTRAL, MANUAL, DEMO, POSITION
	}

	@Override
	public synchronized void stop() {
		setWantedState(ElevatorState.NEUTRAL);
	}

	public synchronized void setWantedState(ElevatorState wantedState) {
		this.mWantedState = wantedState;
	}

	private synchronized void handleStates() {
		// Dont allow Disabled or Demo while on the field

		if (((this.isDisabed() && !Robot.gzOI.isFMS()) || mWantedState == ElevatorState.NEUTRAL)) {

			if (stateNot(ElevatorState.NEUTRAL)) {
				onStateExit(mState);
				mState = ElevatorState.NEUTRAL;
				onStateStart(mState);
			}

		} else if (Robot.auton.isDemo() && !Robot.gzOI.isFMS()) {

			if (stateNot(ElevatorState.DEMO)) {
				onStateExit(mState);
				mState = ElevatorState.DEMO;
				onStateStart(mState);
			}

		} else if (mState != mWantedState) {
			onStateExit(mState);
			mState = mWantedState;
			onStateStart(mState);
		}
	}

	public synchronized void outputSmartDashboard() {
		SmartDashboard.putNumber("Elevator Rotations", getRotations());
		SmartDashboard.putNumber("Elevator Vel", mIO.encoder_vel);
	}

	@Override
	protected void initDefaultCommand() {
	}

	public synchronized void speedLimiting() {
		double pos = getRotations();

		// if demo, dont limit
		// if not in demo and not overriding, limit
		if (getState() != ElevatorState.DEMO && (getState() != ElevatorState.DEMO && !isOverriden())) {
			if (pos < 2.08)
				driveModifier = Constants.kElevator.SPEED_1;
			else if (pos < 2.93 && pos > 2.08)
				driveModifier = Constants.kElevator.SPEED_2;
			else if (pos < 3.66 && pos > 2.93)
				driveModifier = Constants.kElevator.SPEED_3;
			else if (pos < 6.1 && pos > 3.66)
				driveModifier = Constants.kElevator.SPEED_4;
			else if (pos > 6.1)
				driveModifier = Constants.kElevator.SPEED_5;
			// If not in demo, and not overriden
		} else {
			driveModifier = 1;
		}
	}

	public synchronized void enableFollower() {
		elevator_2.follow(elevator_1);
	}

	public synchronized void softLimits(boolean enableSoftLimit) {
		GZSRX.logError(elevator_1.configForwardSoftLimitEnable(enableSoftLimit), this,
				(enableSoftLimit) ? AlertLevel.WARNING : AlertLevel.ERROR,
				"Could not " + (enableSoftLimit ? "enable" : "disable") + " forward soft limit");

		GZSRX.logError(elevator_1.configReverseSoftLimitEnable(enableSoftLimit), this,
				(enableSoftLimit) ? AlertLevel.WARNING : AlertLevel.ERROR,
				"Could not " + (enableSoftLimit ? "enable" : "disable") + " reverse soft limit");
	}

	public synchronized void encoder(double rotations) {
		if (mState != ElevatorState.DEMO) {
			setWantedState(ElevatorState.POSITION);
			elevator_1.configPeakOutputForward(kElevator.CLOSED_DOWN_SPEED_LIMIT, 10);
			elevator_1.configPeakOutputReverse(kElevator.CLOSED_UP_SPEED_LIMIT * -1, 10);
			setTarget(mIO.desired_output = -Units.rotations_to_ticks(rotations));
		} else
			stop();
	}

	public synchronized boolean isDone(double multiplier) {
		return (mIO.encoder_ticks < (getTarget() + (102 * multiplier))
				&& mIO.encoder_ticks > (getTarget() - (102 * multiplier)));
	}

	public synchronized void encoderDone() {
		mIO.desired_output = 0;

		elevator_1.configPeakOutputForward(1, 10);
		elevator_1.configPeakOutputReverse(-1, 10);

		setTarget(0);
	}

	public synchronized void manualJoystick(GZJoystick joy) {
		double up, down;

		if (getState() == ElevatorState.DEMO) {
			up = kElevator.DEMO_JOYSTICK_MODIFIER_UP;
			down = kElevator.DEMO_JOYSTICK_MODIFIER_DOWN;
		} else {
			up = kElevator.JOYSTICK_MODIFIER_UP;
			down = kElevator.JOYSTICK_MODIFIER_DOWN;
		}

		if (joy == OI.opJoy)
			manual(joy.getLeftAnalogY() * ((joy.getLeftAnalogY() > 0) ? up : down));
		else if (joy == OI.driverJoy)
			manual(joy.getRightAnalogY() * ((joy.getRightAnalogY() > 0) ? up : down));
		else
			System.out.println("WARNING Incorrect joystick given to manualJoystick of Elevator");

	}

	public synchronized void manual(double percentage) {
		setWantedState(ElevatorState.MANUAL);
		mIO.desired_output = -percentage;
	}

	public synchronized double getPercentageModify() {
		return driveModifier;
	}

	public synchronized void setSpeedLimitingOverride(ESO override) {
		switch (override) {
		case OFF:
			this.mIsOverriden = false;
			break;
		case ON:
			this.mIsOverriden = true;
			break;
		case TOGGLE:
			this.mIsOverriden = !isOverriden();
			break;
		}
	}

	public enum ESO {
		TOGGLE, ON, OFF
	}

	private synchronized boolean stateNot(ElevatorState state) {
		return mState != state;
	}

	public boolean isOverriden() {
		return mIsOverriden;
	}

	public synchronized String getStateString() {
		return mState.toString();
	}

	public synchronized ElevatorState getState() {
		return mState;
	}

	public double getTarget() {
		return target;
	}

	public void setTarget(double target) {
		this.target = target;
	}

}
