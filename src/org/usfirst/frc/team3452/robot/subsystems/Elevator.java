package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Elevator extends Subsystem {
	public double m_pos = 0;
	public boolean m_overriden = false;

	public WPI_TalonSRX Elev_1;
	private WPI_TalonSRX Elev_2;

	public void initHardware() {
		Elev_1 = new WPI_TalonSRX(Constants.ELEVATOR_1);
		Elev_2 = new WPI_TalonSRX(Constants.ELEVATOR_2);

		// FOLLOWER
		Elev_2.follow(Elev_1);

		// INVERT
		Elev_1.setInverted(Constants.ELEVATOR_1_INVERT);
		Elev_2.setInverted(Constants.ELEVATOR_2_INVERT);

		// ENCODER
		Elev_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
		Elev_1.setSelectedSensorPosition(0, 0, 10);
		Elev_1.setSelectedSensorPosition(0, 0, 10);
		Elev_1.setSensorPhase(Constants.ELEVATOR_ENC_INVERT);

		// PIDs
		Elev_1.config_kF(0, 0, 10);
		Elev_1.config_kP(0, 0.08, 10);
		Elev_1.config_kI(0, 0.000028, 10);
		Elev_1.config_kD(0, 2.5, 10);
		// was 0, .09, .000075, 2.5
		//TODO retune PID

		// RESET ENCODER ON LIMIT SWITCH DOWN
		Elev_1.configSetParameter(ParamEnum.eClearPosOnLimitF, 1, 0, 0, 10);

		// LIMIT SWITCHES
		Elev_1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 10);
		Elev_1.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen, 10);

		// BRAKE
		Elev_1.setNeutralMode(NeutralMode.Brake);
		Elev_2.setNeutralMode(NeutralMode.Brake);

		//CURRENT LIMITING
		Elev_1.configContinuousCurrentLimit(Constants.ELEVATOR_AMP_LIMIT, 10);
		Elev_1.configPeakCurrentLimit(Constants.ELEVATOR_AMP_TRIGGER, 10);
		Elev_1.configPeakCurrentDuration(Constants.ELEVATOR_AMP_TIME, 10);

		Elev_2.configContinuousCurrentLimit(Constants.ELEVATOR_AMP_LIMIT, 10);
		Elev_2.configPeakCurrentLimit(Constants.ELEVATOR_AMP_TRIGGER, 10);
		Elev_2.configPeakCurrentDuration(Constants.ELEVATOR_AMP_TIME, 10);

		Elev_1.enableCurrentLimit(true);
		Elev_2.enableCurrentLimit(true);

		Elev_1.configOpenloopRamp(Constants.ELEVATOR_OPEN_RAMP_TIME, 10);
		Elev_1.configClosedloopRamp(Constants.ELEVATOR_CLOSED_RAMP_TIME, 10);

		//SHUFFLEBOARD
		Elev_1.setSubsystem("Elevator");
		Elev_2.setSubsystem("Elevator");

		Elev_1.setName("Elev 1");
		Elev_2.setName("Elev 2");
	}

	public void setDriveLimit() {
		double pos = -Elev_1.getSelectedSensorPosition(0);

		if (m_overriden == false) {

			if (pos < 8500)
				Robot.drive.m_elev_modify = Constants.ELEVATOR_SPEED_1;
			else if (pos < 12000 && pos > 8500)
				Robot.drive.m_elev_modify = Constants.ELEVATOR_SPEED_2;
			else if (pos < 15000 && pos > 12000)
				Robot.drive.m_elev_modify = Constants.ELEVATOR_SPEED_3;
			else if (pos < 25000 && pos > 15000)
				Robot.drive.m_elev_modify = Constants.ELEVATOR_SPEED_4;
			else if (pos > 25000)
				Robot.drive.m_elev_modify = Constants.ELEVATOR_SPEED_5;

		} else {
			Robot.drive.m_elev_modify = 1;
		}

		//TODO Change to ramping instead of staging?

	}

	public void Encoder(double position) {
		Elev_1.configPeakOutputForward(.45, 10);
		Elev_1.configPeakOutputReverse(-.8, 10);
		m_pos = -position * 4096;
		Elev_1.set(ControlMode.Position, m_pos);
	}

	public boolean isDone(double multiplier) {
		if ((Elev_1.getSelectedSensorPosition(0) < (m_pos + (102 * multiplier))
				&& Elev_1.getSelectedSensorPosition(0) > (m_pos - (102 * multiplier)))) {
			return true;
		} else {
			return false;
		}
	}

	public void EncoderDone() {
		Elev_1.set(ControlMode.PercentOutput, 0);
		Elev_1.configPeakOutputForward(1, 10);
		Elev_1.configPeakOutputReverse(-1, 10);
		m_pos = 0;
	}

	public void initDefaultCommand() {
		setDefaultCommand(new ElevatorTime(0, 0));
	}

	public static class Constants {
		public static final int ELEVATOR_1 = 9;
		public static final int ELEVATOR_2 = 10;

		public static final boolean ELEVATOR_1_INVERT = true;
		public static final boolean ELEVATOR_2_INVERT = true;

		public static final boolean ELEVATOR_ENC_INVERT = false;

		public static final double ELEVATOR_OPEN_RAMP_TIME = .5;
		public static final double ELEVATOR_CLOSED_RAMP_TIME = .25;

		public static final double ELEVATOR_SPEED_1 = 1;
		public static final double ELEVATOR_SPEED_2 = .9;
		public static final double ELEVATOR_SPEED_3 = .65;
		public static final double ELEVATOR_SPEED_4 = .55;
		public static final double ELEVATOR_SPEED_5 = .45;

		public static int ELEVATOR_AMP_TRIGGER = 50;
		public static int ELEVATOR_AMP_LIMIT = 40;
		public static int ELEVATOR_AMP_TIME = 1000;
	}

}
