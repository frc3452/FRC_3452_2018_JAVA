package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZJoystick;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator2 extends GZSubsystem {

	private static GZSRX elevator_1, elevator_2;

	private ElevatorState mState = ElevatorState.NEUTRAL;
	private ElevatorState prevState = mState;

	private double percentageModify = 0;
	private double target = 0;
	private boolean isOverriden = false;

	public Elevator2() {
		// TODO 3) A - EXPERIMENT WITH TALONSRX CONFIGS
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
		switch (mState) {
		case MANUAL:

			IO.control_mode = ControlMode.PercentOutput;
			IO.output = IO.desired_output;

			break;
		case NEUTRAL:

			IO.control_mode = ControlMode.Disabled;
			IO.output = 0;

			break;
		case POSITION:

			IO.control_mode = ControlMode.Position;
			IO.output = IO.desired_output;
			break;
		case DEMO:
			IO.control_mode = ControlMode.PercentOutput;
			IO.output = IO.desired_output;

		default:
			System.out.println("WARNING: Incorrect elevator state " + mState + " reached.");
			break;
		}

		handleSpeedLimiting();
	}

	public static class IO {
		// in
		static double encoder_ticks = -1;
		static double encoder_rotations = -1;
		static double encoder_vel = -1;

		static double elevator_1_amp = -1;
		static double elevator_2_amp = -1;

		static boolean elevator_1_fwd_lmt = false;
		static boolean elevator_1_rev_lmt = false;

		static boolean elevator_2_fwd_lmt = false;
		static boolean elevator_2_rev_lmt = false;

		// out
		static private double output = 0;
		static double desired_output = 0;

		static ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected synchronized void in() {
		IO.encoder_ticks = elevator_1.getSelectedSensorPosition(0);
		IO.encoder_rotations = Units.ticks_to_rotations(IO.encoder_ticks);
		IO.encoder_vel = elevator_1.getSelectedSensorVelocity(0);

		IO.elevator_1_amp = elevator_1.getOutputCurrent();
		IO.elevator_2_amp = elevator_2.getOutputCurrent();

		IO.elevator_1_fwd_lmt = elevator_1.getSensorCollection().isFwdLimitSwitchClosed();
		IO.elevator_1_rev_lmt = elevator_1.getSensorCollection().isRevLimitSwitchClosed();

		IO.elevator_2_fwd_lmt = elevator_2.getSensorCollection().isFwdLimitSwitchClosed();
		IO.elevator_2_rev_lmt = elevator_2.getSensorCollection().isRevLimitSwitchClosed();
	}

	public synchronized void outputSmartDashboard() {
		SmartDashboard.putNumber("Elevator Rotations", IO.encoder_rotations);
		SmartDashboard.putNumber("Elevator Vel", IO.encoder_vel);
	}

	@Override
	protected void initDefaultCommand() {
	}

	private synchronized void handleSpeedLimiting() {
		speedLimiting();

		if (isOverriden)
			OI.rumble(CONTROLLER.DRIVER, .45);
		else
			OI.rumble(CONTROLLER.DRIVER, 0);
	}

	public synchronized void speedLimiting() {
		double pos = IO.encoder_rotations;

		if (mState == ElevatorState.DEMO) {
			if (isOverriden == false) {
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
	
	public synchronized void enableFollower()
	{
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

		target = IO.desired_output = -Units.rotations_to_ticks(rotations);
	}

	public synchronized boolean isDone(double multiplier) {
		return (IO.encoder_ticks < (target + (102 * multiplier))
				&& IO.encoder_ticks > (target - (102 * multiplier)));
	}

	public synchronized void encoderDone() {
		IO.desired_output = 0;

		elevator_1.configPeakOutputForward(1, 10);
		elevator_1.configPeakOutputReverse(-1, 10);

		target = 0;
	}

	public synchronized void manualJoystick(GZJoystick joy) {
		double up, down;

		if (getState() == ElevatorState.DEMO) {

		}
		up = kElevator.JOYSTICK_MODIFIER_UP;
		down = kElevator.JOYSTICK_MODIFIER_DOWN;

		if (joy == OI.opJoy)
			manual(joy.getLeftAnalogY() * ((joy.getLeftAnalogY() > 0) ? up : down));
		else if (joy == OI.driverJoy)
			manual(joy.getRightAnalogY() * ((joy.getRightAnalogY() > 0) ? up : down));
		else
			System.out.println("WARNING Incorrect joystick given to manualJoystick of Elevator");

	}

	public synchronized void manual(double percentage) {
		setState(ElevatorState.MANUAL);

		IO.desired_output = percentage;
	}

	public synchronized double getPercentageModify() {
		return percentageModify;
	}

	public synchronized void setPercentageModify(double percentageModify) {
		this.percentageModify = percentageModify;
	}

	public synchronized void setSpeedLimitingOverride(ESO override) {
		switch (override) {
		case OFF:
			isOverriden = false;
			break;
		case ON:
			isOverriden = true;
			break;
		case TOGGLE:
			isOverriden = !isOverriden;
			break;
		}
	}

	public enum ESO {
		TOGGLE, ON, OFF
	}

	@Override
	protected synchronized void out() {
		elevator_1.set(IO.control_mode, IO.output);
	}

	public enum ElevatorState {
		NEUTRAL, MANUAL, DEMO, POSITION
	}

	@Override
	public synchronized void stop() {
		setState(ElevatorState.NEUTRAL);
	}

	public synchronized void setState(ElevatorState wantedState) {
		if (this.isDisabed())
			mState = ElevatorState.NEUTRAL;
		else if (Robot.autonSelector.isDemo())
			mState = ElevatorState.DEMO;
		else
			mState = wantedState;
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

}
