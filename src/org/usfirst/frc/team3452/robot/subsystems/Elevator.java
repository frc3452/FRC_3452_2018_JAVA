package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.TALON;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteLimitSwitchSource;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <b>Elevator subsystem</b> Handles elevator open + closed loop and speed
 * control for drivetrain
 * 
 * @author max
 *
 */
public class Elevator extends Subsystem {
	public double m_pos = 0;
	public boolean m_overriden = false;

	public WPI_TalonSRX Elev_1;
	public WPI_TalonSRX Elev_2;

	public boolean isRemoteSensor = true;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public Elevator() {
		Elev_1 = new WPI_TalonSRX(Constants.Elevator.E_1);
		Elev_2 = new WPI_TalonSRX(Constants.Elevator.E_2);

		generalTalonInit(Elev_1);
		generalTalonInit(Elev_2);
		
		// FOLLOWER
		Elev_2.follow(Elev_1);

		// INVERT
		Elev_1.setInverted(Constants.Elevator.E_1_INVERT);
		Elev_2.setInverted(Constants.Elevator.E_2_INVERT);
		
		// PIDs
		Elev_1.config_kF(0, 0, 10);
		Elev_1.config_kP(0, 0.08, 10);
		Elev_1.config_kI(0, 0.000028, 10);
		Elev_1.config_kD(0, 2.5, 10);
		Elev_1.configOpenloopRamp(Constants.Elevator.E_OPEN_RAMP_TIME, 10);
		Elev_1.configClosedloopRamp(Constants.Elevator.E_CLOSED_RAMP_TIME, 10);
		
		// ENCODER
		Elev_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		Elev_1.setSelectedSensorPosition(0, 0, 10);
		Elev_1.setSensorPhase(Constants.Elevator.E_ENC_INVERT);

		//		 RESET ENCODER ON LIMIT SWITCH DOWN
		Elev_1.configSetParameter(ParamEnum.eClearPosOnLimitF, 1, 0, 0, 10);

		if (isRemoteSensor) {
			//		 REMOTE LIMIT SWITCHES
			Elev_1.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
					LimitSwitchNormal.NormallyOpen, Elev_2.getDeviceID(), 10);
			Elev_1.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
					LimitSwitchNormal.NormallyOpen, Elev_2.getDeviceID(), 10);
		} else {
			// PRACTICE LIMIT SWITCHES
			Elev_1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
					10);
			Elev_1.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
					10);
			Elev_2.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
					LimitSwitchNormal.NormallyOpen, Elev_1.getDeviceID(), 10);
			Elev_2.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
					LimitSwitchNormal.NormallyOpen, Elev_1.getDeviceID(), 10);

		}

		Elev_1.setName("Elev 1");
		Elev_2.setName("Elev 2");
	}

	public void generalTalonInit(WPI_TalonSRX talon) {
		// BRAKE
		Elev_1.setNeutralMode(NeutralMode.Brake);
		Elev_2.setNeutralMode(NeutralMode.Brake);

		//CURRENT LIMITING
		talon.configContinuousCurrentLimit(Constants.Elevator.AMP_LIMIT, 10);
		talon.configPeakCurrentLimit(Constants.Elevator.AMP_TRIGGER, 10);
		talon.configPeakCurrentDuration(Constants.Elevator.AMP_TIME, 10);

		talon.enableCurrentLimit(true);
		
		talon.setSubsystem("Elevator");
	}

	/**
	 * update elevator modifier
	 * 
	 * @author max
	 * @since
	 */
	public void setDriveLimit() {
		double pos = -Elev_1.getSelectedSensorPosition(0);

		if (m_overriden == false) {

			if (pos < 8500)
				Robot.drive.m_elev_modify = Constants.Elevator.SPEED_1;
			else if (pos < 12000 && pos > 8500)
				Robot.drive.m_elev_modify = Constants.Elevator.SPEED_2;
			else if (pos < 15000 && pos > 12000)
				Robot.drive.m_elev_modify = Constants.Elevator.SPEED_3;
			else if (pos < 25000 && pos > 15000)
				Robot.drive.m_elev_modify = Constants.Elevator.SPEED_4;
			else if (pos > 25000)
				Robot.drive.m_elev_modify = Constants.Elevator.SPEED_5;

		} else {
			Robot.drive.m_elev_modify = 1;
		}

	}

	/**
	 * run to position
	 * 
	 * @author max
	 * @param position
	 * @since
	 */
	public void encoder(double position) {
		Elev_1.configPeakOutputForward(.45, 10);
		Elev_1.configPeakOutputReverse(-.8, 10);
		m_pos = -position * 4096;
		Elev_1.set(ControlMode.Position, m_pos);
	}

	/**
	 * @author max
	 * @param multiplier
	 * @return boolean
	 * @since
	 */
	public boolean isDone(double multiplier) {
		if ((Elev_1.getSelectedSensorPosition(0) < (m_pos + (102 * multiplier))
				&& Elev_1.getSelectedSensorPosition(0) > (m_pos - (102 * multiplier)))) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * set output to default set position target default
	 * 
	 * @author max
	 * @since
	 */
	public void encoderDone() {
		Elev_1.set(ControlMode.PercentOutput, 0);
		Elev_1.configPeakOutputForward(1, 10);
		Elev_1.configPeakOutputReverse(-1, 10);
		m_pos = 0;
	}

	@Override
	public void initDefaultCommand() {
	}

	/**
	 * Elevator speed limiting override
	 * 
	 * @author max
	 *
	 */
	public static enum EO {
		TOGGLE, ON, OFF;
	}
}
