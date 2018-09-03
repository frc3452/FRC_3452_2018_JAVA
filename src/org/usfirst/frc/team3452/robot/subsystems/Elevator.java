package org.usfirst.frc.team3452.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZJoystick;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSRX.Breaker;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends GZSubsystem {

	private static GZSRX elevator_1, elevator_2;
	private List<GZSRX> controllers;

	private ElevatorState mState = ElevatorState.NEUTRAL;
	private ElevatorState mWantedState = mState;
	public IO mIO = new IO();

	private double driveModifier = 0;
	private double target = 0;
	private boolean mIsOverriden = false;

	public Elevator() {
		elevator_1 = new GZSRX(kElevator.E_1, Breaker.AMP_40);
		elevator_2 = new GZSRX(kElevator.E_2, Breaker.AMP_40);

		controllers = Arrays.asList(elevator_1, elevator_2);

		talonInit(controllers);

		// FOLLOWER
		elevator_2.follow(elevator_1);

		// INVERT
		elevator_1.setInverted(Constants.kElevator.E_1_INVERT);
		elevator_2.setInverted(Constants.kElevator.E_2_INVERT);

		// F 0 P .08 I .000028 D 2.5
		// PIDs
		elevator_1.config_kF(0, 0, 10);
		elevator_1.config_kP(0, 0.2, 10);
		elevator_1.config_kI(0, 0.000028, 10);
		elevator_1.config_kD(0, 2.5, 10);
		elevator_1.configOpenloopRamp(Constants.kElevator.OPEN_RAMP_TIME, 10);
		elevator_1.configClosedloopRamp(Constants.kElevator.CLOSED_RAMP_TIME, 10);

		// TODO ISSUE #11

		// ENCODER
		final ErrorCode encoderPresent = elevator_1
				.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		if (encoderPresent != ErrorCode.OK)
			System.out.println("ERROR ELEVATOR ENCODER NOT FOUND!");

		elevator_1.setSelectedSensorPosition(0, 0, 10);
		elevator_1.setSensorPhase(Constants.kElevator.ENC_INVERT);

		// SOFT LIMITS FOR DEMO MODE
		elevator_1.configForwardSoftLimitThreshold(Units.rotations_to_ticks(-kElevator.LOWER_SOFT_LIMIT));
		elevator_1.configReverseSoftLimitThreshold(Units.rotations_to_ticks(-kElevator.UPPER_SOFT_LIMIT));

		// RESET ENCODER ON LIMIT SWITCH DOWN
		elevator_1.configClearPositionOnLimitF(true, 10);

		// REMOTE LIMIT SWITCHES
		elevator_1.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
				LimitSwitchNormal.NormallyOpen, elevator_2.getDeviceID(), 10);
		elevator_1.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
				LimitSwitchNormal.NormallyOpen, elevator_2.getDeviceID(), 10);

		elevator_1.setName("Elev 1");
		elevator_2.setName("Elev 2");

	}

	private synchronized void talonInit(List<GZSRX> srx) {
		for (GZSRX s : srx) {
			s.configFactoryDefault();
			s.setNeutralMode(NeutralMode.Brake);

			s.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10);
			s.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10);
			s.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10);
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
		public Double encoder_ticks = Double.NaN, encoder_rotations = Double.NaN, encoder_vel = Double.NaN,
				encoder_speed = Double.NaN;

		public Double elevator_1_amp = Double.NaN, elevator_2_amp = Double.NaN;

		public Double elevator_1_volt = Double.NaN, elevator_2_volt = Double.NaN;

		public Boolean elevator_fwd_lmt = false, elevator_rev_lmt = false;

		// out
		private double output = 0;
		public double desired_output = 0;

		ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected synchronized void in() {
		mIO.encoder_ticks = (double) elevator_1.getSelectedSensorPosition(0);
		mIO.encoder_rotations = Units.ticks_to_rotations(mIO.encoder_ticks);
		mIO.encoder_vel = (double) elevator_1.getSelectedSensorVelocity(0);
		mIO.encoder_speed = Units.ticks_to_rotations(mIO.encoder_vel);

		mIO.elevator_1_amp = elevator_1.getOutputCurrent();
		mIO.elevator_2_amp = elevator_2.getOutputCurrent();

		mIO.elevator_1_volt = elevator_1.getMotorOutputVoltage();
		mIO.elevator_2_volt = elevator_2.getMotorOutputVoltage();

		mIO.elevator_fwd_lmt = elevator_2.getSensorCollection().isFwdLimitSwitchClosed();
		mIO.elevator_rev_lmt = elevator_2.getSensorCollection().isRevLimitSwitchClosed();
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
		//Dont allow Disabled or Demo while on the field
		
		if (((this.isDisabed() && !Robot.auton.isFMS()) || mWantedState == ElevatorState.NEUTRAL)) {

			if (stateNot(ElevatorState.NEUTRAL)) {
				onStateExit(mState);
				mState = ElevatorState.NEUTRAL;
				onStateStart(mState);
			}

		} else if (Robot.auton.isDemo() && !Robot.auton.isFMS()) {

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
		SmartDashboard.putNumber("Elevator Rotations", mIO.encoder_rotations);
		SmartDashboard.putNumber("Elevator Vel", mIO.encoder_vel);
	}

	@Override
	protected void initDefaultCommand() {
	}

	public synchronized void speedLimiting() {
		double pos = mIO.encoder_rotations;

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
		elevator_1.configForwardSoftLimitEnable(enableSoftLimit);
		elevator_1.configReverseSoftLimitEnable(enableSoftLimit);
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
