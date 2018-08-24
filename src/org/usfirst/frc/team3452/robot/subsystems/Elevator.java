
package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kDemoMode;
import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZJoystick;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSRX.Breaker;
import org.usfirst.frc.team3452.robot.util.GZSRX.Master;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends GZSubsystem {

	private static GZSRX elevator_1, elevator_2;

	private ElevatorState mState = ElevatorState.NEUTRAL;
	private ElevatorState prevState = mState;
	public IO mIO = new IO();

	private double percentageModify = 0;
	private double target = 0;
	private boolean isOverriden = false;

	public Elevator() {
		elevator_1 = new GZSRX(Constants.kElevator.E_1, Breaker.AMP_40, Master.MASTER);
		elevator_2 = new GZSRX(Constants.kElevator.E_2, Breaker.AMP_40, Master.FOLLOWER);

		elevator_1.configFactoryDefault();
		elevator_2.configFactoryDefault();

		elevator_1.setNeutralMode(NeutralMode.Brake);
		elevator_2.setNeutralMode(NeutralMode.Brake);

		elevator_1.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10);
		elevator_1.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10);
		elevator_1.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10);

		elevator_2.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10);
		elevator_2.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10);
		elevator_2.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10);

		elevator_1.enableCurrentLimit(true);
		elevator_2.enableCurrentLimit(true);

		elevator_1.setSubsystem("Elevator");
		elevator_2.setSubsystem("Elevator");

		// FOLLOWER
		elevator_2.follow(elevator_1);

		// INVERT
		elevator_1.setInverted(Constants.kElevator.E_1_INVERT);
		elevator_2.setInverted(Constants.kElevator.E_2_INVERT);

		// F 0
		// P .08
		// I .000028
		// D 2.5
		// PIDs
		elevator_1.config_kF(0, 0, 10);
		elevator_1.config_kP(0, 0.2, 10);
		elevator_1.config_kI(0, 0.000028, 10);
		elevator_1.config_kD(0, 2.5, 10);
		elevator_1.configOpenloopRamp(Constants.kElevator.OPEN_RAMP_TIME, 10);
		elevator_1.configClosedloopRamp(Constants.kElevator.CLOSED_RAMP_TIME, 10);

		// ENCODER
		elevator_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		elevator_1.setSelectedSensorPosition(0, 0, 10);
		elevator_1.setSensorPhase(Constants.kElevator.ENC_INVERT);

		// SOFT LIMITS FOR DEMO MODE
		// TODO 1) CHECK POLARITY
		// TODO 1) WRITE PHASE TO CONSTANT
		// TODO 1) CREATE BOOLEAN FOR USE OUTSIDE LIMITS
		elevator_1.configForwardSoftLimitThreshold(Units.rotations_to_ticks(1));
		elevator_1.configReverseSoftLimitThreshold(Units.rotations_to_ticks(-5));

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

	private synchronized void onStateStart(ElevatorState wantedState) {
		switch (wantedState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		case POSITION:
			break;
		case DEMO:

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

		handleSpeedLimiting();
	}

	public void runElevatorManual(GZJoystick joy) {
		double up = 0, down = 0, value;
		if (mState == ElevatorState.DEMO) {
			up = kDemoMode.ELEVATOR_JOYSTICK_MODIFIER_UP;
			down = kDemoMode.ELEVATOR_JOYSTICK_MODIFIER_DOWN;
		} else {
			up = kElevator.JOYSTICK_MODIFIER_UP;
			down = kElevator.JOYSTICK_MODIFIER_DOWN;
		}

		if (joy == OI.opJoy)
			value = joy.getLeftAnalogY() * (joy.getLeftAnalogY() > 0 ? up : down);
		else
			value = joy.getRightAnalogY() * (joy.getRightAnalogY() > 0 ? up : down);

		mIO.desired_output = value;
	}

	public static class IO {
		// in
		double encoder_ticks = Double.NaN;
		double encoder_rotations = Double.NaN;
		double encoder_vel = Double.NaN;

		public double elevator_1_amp = Double.NaN;
		public double elevator_2_amp = Double.NaN;

		public double elevator_1_volt = Double.NaN;
		public double elevator_2_volt = Double.NaN;

		public boolean elevator_fwd_lmt = false;
		public boolean elevator_rev_lmt = false;

		// out
		private double output = 0;
		private double desired_output = 0;

		ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected synchronized void in() {
		mIO.encoder_ticks = elevator_1.getSelectedSensorPosition(0);
		mIO.encoder_rotations = Units.ticks_to_rotations(mIO.encoder_ticks);
		mIO.encoder_vel = elevator_1.getSelectedSensorVelocity(0);

		mIO.elevator_1_amp = elevator_1.getOutputCurrent();
		mIO.elevator_2_amp = elevator_2.getOutputCurrent();

		mIO.elevator_1_volt = elevator_1.getMotorOutputVoltage();
		mIO.elevator_2_volt = elevator_2.getMotorOutputVoltage();

		mIO.elevator_fwd_lmt = elevator_2.getSensorCollection().isFwdLimitSwitchClosed();
		mIO.elevator_rev_lmt = elevator_2.getSensorCollection().isRevLimitSwitchClosed();
	}

	public synchronized void outputSmartDashboard() {
		SmartDashboard.putNumber("Elevator RotatmIOns", mIO.encoder_rotations);
		SmartDashboard.putNumber("Elevator Vel", mIO.encoder_vel);
	}

	@Override
	protected void initDefaultCommand() {
	}

	private synchronized void handleSpeedLimiting() {
		speedLimiting();

		if (isOverriden())
			OI.rumble(CONTROLLER.DRIVER, .45);
		else
			OI.rumble(CONTROLLER.DRIVER, 0);
	}

	public synchronized void speedLimiting() {
		double pos = mIO.encoder_rotations;

		if (mState == ElevatorState.DEMO) {
			if (isOverriden() == false) {
				if (pos < 2.08)
					percentageModify = Constants.kElevator.SPEED_1;
				else if (pos < 2.93 && pos > 2.08)
					percentageModify = Constants.kElevator.SPEED_2;
				else if (pos < 3.66 && pos > 2.93)
					percentageModify = Constants.kElevator.SPEED_3;
				else if (pos < 6.1 && pos > 3.66)
					percentageModify = Constants.kElevator.SPEED_4;
				else if (pos > 6.1)
					percentageModify = Constants.kElevator.SPEED_5;
			} else {
				percentageModify = 1;
			}

			// If not in demo, we are already slowing the drivetrain down somewhere else
		} else {
			percentageModify = 1;
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
		setState(ElevatorState.POSITION);

		elevator_1.configPeakOutputForward(kElevator.CLOSED_DOWN_SPEED_LIMIT, 10);
		elevator_1.configPeakOutputReverse(kElevator.CLOSED_UP_SPEED_LIMIT * -1, 10);

		if (mState == ElevatorState.DEMO)
			mIO.desired_output = 0;
		else
			setTarget(mIO.desired_output = -Units.rotations_to_ticks(rotations));
	}

	public synchronized boolean isDone(double multiplier) {
		return (mIO.encoder_ticks < (getTarget() + (102 * multiplier))
				&& mIO.encoder_ticks > (getTarget() - (102 * multiplier)));
	}

	public synchronized void encoderDone() {
		stop();
		mIO.desired_output = 0;

		elevator_1.configPeakOutputForward(1, 10);
		elevator_1.configPeakOutputReverse(-1, 10);

		setTarget(0);
	}

	public synchronized void manualJoystick(GZJoystick joy) {
		double up, down;

		if (getState() != ElevatorState.DEMO) {
			up = kElevator.JOYSTICK_MODIFIER_UP;
			down = kElevator.JOYSTICK_MODIFIER_DOWN;
		} else {
			up = kDemoMode.ELEVATOR_JOYSTICK_MODIFIER_UP;
			down = kDemoMode.ELEVATOR_JOYSTICK_MODIFIER_DOWN;
		}

		if (joy == OI.opJoy)
			manual(joy.getLeftAnalogY() * ((joy.getLeftAnalogY() > 0) ? up : down));
		else if (joy == OI.driverJoy)
			manual(joy.getRightAnalogY() * ((joy.getRightAnalogY() > 0) ? up : down));
		else
			System.out.println("WARNING Incorrect joystick given to manualJoystick of Elevator");

	}

	public synchronized void manual(double percentage) {
		setState(ElevatorState.MANUAL);
		mIO.desired_output = -percentage;
	}

	public synchronized double getPercentageModify() {
		return percentageModify;
	}

	public synchronized void setSpeedLimitingOverride(ESO override) {
		switch (override) {
		case OFF:
			setOverriden(false);
			break;
		case ON:
			setOverriden(true);
			break;
		case TOGGLE:
			setOverriden(!isOverriden());
			break;
		}
	}

	public enum ESO {
		TOGGLE, ON, OFF
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
		setState(ElevatorState.NEUTRAL);
	}

	public synchronized void setState(ElevatorState wantedState) {
		if (this.isDisabed()) {
			onStateStart(mState);
			mState = ElevatorState.NEUTRAL;
			onStateExit(mState);
		} else if (Robot.autonSelector.isDemo()) {
			onStateStart(mState);
			mState = ElevatorState.DEMO;
			onStateExit(mState);
		} else {
			onStateStart(mState);
			mState = wantedState;
			onStateExit(mState);
		}
	}

	public boolean isOverriden() {
		return isOverriden;
	}

	public void setOverriden(boolean isOverriden) {
		this.isOverriden = isOverriden;
	}

	public synchronized void checkPrevState() {
		if (mState != prevState) {
			onStateExit(prevState);
			onStateStart(mState);
		}
		prevState = mState;
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
