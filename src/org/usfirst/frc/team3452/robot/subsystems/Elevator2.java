package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator2 extends GZSubsystem {

	private static GZSRX elevator_1, elevator_2;

	private ElevatorState mState = ElevatorState.NEUTRAL;

	private double percentageModify = 0;
	private double target = 0;
	private boolean isOverriden = false;

	public Elevator2() {
		//TODO 2) EXPERIMENT WITH TALONSRX CONFIGS AND FINISH CONSTRUCTION
	}

	@Override
	public void stop() {
		mState = ElevatorState.NEUTRAL;
	}

	public void setState(ElevatorState wantedState) {
		if (this.isDisabed())
			mState = ElevatorState.NEUTRAL;
		else
			mState = wantedState;
	}

	public ElevatorState getState() {
		return mState;
	}

	@Override
	public void loop() {
		switch (mState) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		case POSITION:
			break;
		default:
			System.out.println("WARNING: Incorrect elevator state " + mState + " reached.");
			break;
		}
		
		this.inputOutput();
		speedLimiting();
	}

	public static class Values {
		// in
		static double encoder_ticks = 0;
		static double encoder_rotations = 0;
		static double encoder_vel = 0;

		static double elevator_1_amp = 0;
		static double elevator_2_amp = 0;

		// out
		static double output = 0;
		static double desired_output = 0;

		static ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected void in() {
		Values.encoder_ticks = elevator_1.getSelectedSensorPosition(0);
		Values.encoder_rotations = Units.ticks_to_rotations(Values.encoder_ticks);
		Values.encoder_vel = elevator_1.getSelectedSensorVelocity(0);

		// TODO STATE TESTING - GET CURRENT FROM PDP OR TALON? ASK AJ
		Values.elevator_1_amp = elevator_1.getOutputCurrent();
		Values.elevator_2_amp = elevator_2.getOutputCurrent();
	}

	@Override
	protected void out() {
		elevator_1.set(Values.control_mode, Values.output);
	}

	public enum ElevatorState {
		NEUTRAL, MANUAL, POSITION
	}

	public void outputSmartDashboard() {
		SmartDashboard.putNumber("Elevator Rotations", Values.encoder_rotations);
		SmartDashboard.putNumber("Elevator Vel", Values.encoder_vel);
	}

	@Override
	protected void initDefaultCommand() {
	}

	public void speedLimiting() {

		double pos = Values.encoder_rotations;

		if (!Robot.autonSelector.isSaftey()) {
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
		} else {
			percentageModify = 1;
		}
	}

	public void softLimits(boolean enableSoftLimit) {
		elevator_1.configForwardSoftLimitEnable(enableSoftLimit);
		elevator_1.configReverseSoftLimitEnable(enableSoftLimit);
	}
	
	public void encoder(double rotations) {
		elevator_1.configPeakOutputForward(kElevator.E_CLOSED_DOWN_SPEED_LIMIT, 10);
		elevator_1.configPeakOutputReverse(kElevator.E_CLOSED_UP_SPEED_LIMIT * -1, 10);
		
		Values.control_mode = ControlMode.Position;
		target = Values.desired_output = -Units.rotations_to_ticks(rotations);
	}
	
	public boolean isDone(double multiplier) {
		return (Values.encoder_ticks < (target + (102 * multiplier))
				&& Values.encoder_ticks > (target - (102 * multiplier)));
	}
	
	public void encoderDone() {
		Values.control_mode = ControlMode.PercentOutput;
		Values.desired_output = 0;
		
		elevator_1.configPeakOutputForward(1, 10);
		elevator_1.configPeakOutputReverse(-1, 10);
		
		target = 0;
	}
	
	public void manual(double percentage)
	{
		Values.control_mode = ControlMode.PercentOutput;
		Values.desired_output = percentage;
	}
	
	
	public double getPercentageModify() {
		return percentageModify;
	}

	public void setPercentageModify(double percentageModify) {
		this.percentageModify = percentageModify;
	}
	
	public enum ESO {
		TOGGLE, ON, OFF
	}

}
