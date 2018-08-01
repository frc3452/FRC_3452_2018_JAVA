package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kElevator;

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
		Elev_1 = new WPI_TalonSRX(Constants.kElevator.E_1);
		Elev_2 = new WPI_TalonSRX(Constants.kElevator.E_2);

		generalTalonInit(Elev_1);
		generalTalonInit(Elev_2);

		// FOLLOWER
		Elev_2.follow(Elev_1);

		// INVERT
		Elev_1.setInverted(Constants.kElevator.E_1_INVERT);
		Elev_2.setInverted(Constants.kElevator.E_2_INVERT);

		// F 0
		// P .08
		// I .000028
		// D 2.5
		// PIDs
		Elev_1.config_kF(0, 0, 10);
		Elev_1.config_kP(0, 0.2, 10);
		Elev_1.config_kI(0, 0.000028, 10);
		Elev_1.config_kD(0, 2.5, 10);
		Elev_1.configOpenloopRamp(Constants.kElevator.E_OPEN_RAMP_TIME, 10);
		Elev_1.configClosedloopRamp(Constants.kElevator.E_CLOSED_RAMP_TIME, 10);

		// ENCODER
		Elev_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
		Elev_1.setSelectedSensorPosition(0, 0, 10);
		Elev_1.setSensorPhase(Constants.kElevator.E_ENC_INVERT);

		// RESET ENCODER ON LIMIT SWITCH DOWN
		Elev_1.configSetParameter(ParamEnum.eClearPosOnLimitF, 1, 0, 0, 10);

		if (isRemoteSensor) {
			// REMOTE LIMIT SWITCHES
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

	/**
	 * @return elevator rotations (negative is down, positive is up)
	 */
	public double getElevatorHeight() {
		return (double) -Elev_1.getSelectedSensorPosition(0) / 4096;
	}
	
	/**
	 * @return elevator speed (negative is down, positive is up)
	 */
	public double getElevatorSpeed()
	{
		return (double) -Robot.elevator.Elev_1.getSelectedSensorVelocity(0) / 4096;
	}

	public void generalTalonInit(WPI_TalonSRX talon) {
		// BRAKE
		Elev_1.setNeutralMode(NeutralMode.Brake);
		Elev_2.setNeutralMode(NeutralMode.Brake);

		// CURRENT LIMITING
		talon.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10);
		talon.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10);
		talon.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10);

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

		double pos = getElevatorHeight();

		if (!Robot.autonSelector.isSaftey()) {
			if (m_overriden == false) {

				if (pos < 2.08)
					Robot.drive.m_elev_modify = Constants.kElevator.SPEED_1;
				else if (pos < 2.93 && pos > 2.08)
					Robot.drive.m_elev_modify = Constants.kElevator.SPEED_2;
				else if (pos < 3.66 && pos > 2.93)
					Robot.drive.m_elev_modify = Constants.kElevator.SPEED_3;
				else if (pos < 6.1 && pos > 3.66)
					Robot.drive.m_elev_modify = Constants.kElevator.SPEED_4;
				else if (pos > 6.1)
					Robot.drive.m_elev_modify = Constants.kElevator.SPEED_5;

			} else {
				Robot.drive.m_elev_modify = 1;
			}
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
		Elev_1.configPeakOutputForward(kElevator.E_CLOSED_DOWN_SPEED_LIMIT, 10);
		Elev_1.configPeakOutputReverse(kElevator.E_CLOSED_UP_SPEED_LIMIT * -1, 10);
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
		return (Elev_1.getSelectedSensorPosition(0) < (m_pos + (102 * multiplier))
				&& Elev_1.getSelectedSensorPosition(0) > (m_pos - (102 * multiplier)));
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
	public enum ESO {
		TOGGLE, ON, OFF
	}
}
