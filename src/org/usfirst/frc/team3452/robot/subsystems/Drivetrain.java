package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTele;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

public class Drivetrain extends Subsystem {
	// PDP
	public static PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	// DRIVETRAIN
	public WPI_TalonSRX L1, R1;


	private static WPI_TalonSRX L2, L3, L4, R2, R3, R4;

	// ROBOT DRIVE OBJECT
	private DifferentialDrive robotDrive;

	// GYRO
	public AHRS Gyro;

	// variable init
	public double m_modify = 1, m_elev_modify = 1, l_pos = 0, r_pos = 0;

	public void initHardware() {
		L1 = new WPI_TalonSRX(Constants.DRIVE_L_1);
		L2 = new WPI_TalonSRX(Constants.DRIVE_L_2);
		L3 = new WPI_TalonSRX(Constants.DRIVE_L_3);
		L4 = new WPI_TalonSRX(Constants.DRIVE_L_4);
		R1 = new WPI_TalonSRX(Constants.DRIVE_R_1);
		R2 = new WPI_TalonSRX(Constants.DRIVE_R_2);
		R3 = new WPI_TalonSRX(Constants.DRIVE_R_3);
		R4 = new WPI_TalonSRX(Constants.DRIVE_R_4);

		robotDrive = new DifferentialDrive(L1, R1);

		Gyro = new AHRS(SPI.Port.kMXP);

		robotDrive.setDeadband(0.08);
		robotDrive.setSafetyEnabled(true);

		Gyro.reset();

		// follower mode
		L2.follow(L1);
		L3.follow(L1);
		L4.follow(L1);

		R2.follow(R1);
		R3.follow(R1);
		R4.follow(R1);

		// encoder init
		L1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
		R1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
		L1.setSensorPhase(true);
		R1.setSensorPhase(true);

		L1.config_kF(0, 0, 10); // @ 100%, 4240u/100ms = ((1 * 1023) / 4300) =
								// .2379
		L1.config_kP(0, 0.425, 10);
		L1.config_kI(0, 0.0000004, 10); // .0000005
		L1.config_kD(0, 4.25, 10);

		R1.config_kF(0, 0, 10);
		R1.config_kP(0, 0.425, 10);
		R1.config_kI(0, 0.0000004, 10);
		R1.config_kD(0, 4.25, 10);

		// NOMINAL OUTPUT
		L1.configNominalOutputForward(0, 10);
		L1.configNominalOutputReverse(0, 10);
		R1.configNominalOutputForward(0, 10);
		R1.configNominalOutputReverse(0, 10);

		// AMP LIMIT
		// OUTER TALONS IN BLOCK = 40amp, INNER TALONS IN BLOCK = 30amp
		L1.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		L1.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, 10);
		L1.configPeakCurrentDuration(Constants.DRIVE_40_AMP_TIME, 10);
		L1.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		L2.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		L2.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, 10);
		L2.configPeakCurrentDuration(Constants.DRIVE_40_AMP_TIME, 10);
		L2.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		R1.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		R1.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, 10);
		R1.configPeakCurrentDuration(Constants.DRIVE_40_AMP_TIME, 10);
		R1.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		R2.configContinuousCurrentLimit(Constants.DRIVE_40_AMP_LIMIT, 10);
		R2.configPeakCurrentLimit(Constants.DRIVE_40_AMP_TRIGGER, 10);
		R2.configPeakCurrentDuration(Constants.DRIVE_40_AMP_TIME, 10);
		R2.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		L3.configContinuousCurrentLimit(Constants.DRIVE_30_AMP_LIMIT, 10);
		L3.configPeakCurrentLimit(Constants.DRIVE_30_AMP_TRIGGER, 10);
		L3.configPeakCurrentDuration(Constants.DRIVE_30_AMP_TIME, 10);
		L3.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		L4.configContinuousCurrentLimit(Constants.DRIVE_30_AMP_LIMIT, 10);
		L4.configPeakCurrentLimit(Constants.DRIVE_30_AMP_TRIGGER, 10);
		L4.configPeakCurrentDuration(Constants.DRIVE_30_AMP_TIME, 10);
		L4.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		R3.configContinuousCurrentLimit(Constants.DRIVE_30_AMP_LIMIT, 10);
		R3.configPeakCurrentLimit(Constants.DRIVE_30_AMP_TRIGGER, 10);
		R3.configPeakCurrentDuration(Constants.DRIVE_30_AMP_TIME, 10);
		R3.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		R4.configContinuousCurrentLimit(Constants.DRIVE_30_AMP_LIMIT, 10);
		R4.configPeakCurrentLimit(Constants.DRIVE_30_AMP_TRIGGER, 10);
		R4.configPeakCurrentDuration(Constants.DRIVE_30_AMP_TIME, 10);
		R4.configOpenloopRamp(Constants.DRIVE_RAMP_TIME, 10);

		L1.enableCurrentLimit(true);
		L2.enableCurrentLimit(true);
		L3.enableCurrentLimit(true);
		L4.enableCurrentLimit(true);
		R1.enableCurrentLimit(true);
		R2.enableCurrentLimit(true);
		R3.enableCurrentLimit(true);
		R4.enableCurrentLimit(true);

		// COAST MODE
		BrakeCoast(NeutralMode.Coast);

		robotDrive.setSubsystem("Drive train");
		pdp.setSubsystem("Drive train");

		L1.setSubsystem("Drive train");
		L2.setSubsystem("Drive train");
		L3.setSubsystem("Drive train");
		L4.setSubsystem("Drive train");
		R1.setSubsystem("Drive train");
		R2.setSubsystem("Drive train");
		R3.setSubsystem("Drive train");
		R4.setSubsystem("Drive train");

		L1.setName("L1");
		L2.setName("L2");
		L3.setName("L3");
		L4.setName("L4");
		R1.setName("R1");
		R2.setName("R2");
		R3.setName("R3");
		R4.setName("R4");
	}

	public void initDefaultCommand() {
		setDefaultCommand(new DriveTele());
	}

	public void Arcade(Joystick joy) {
		Arcade(-joy.getRawAxis(1) * m_modify, ((joy.getRawAxis(3) - joy.getRawAxis(2)) * .6 * m_modify));
		Robot.elevator.setDriveLimit();
	}

	public void Arcade(double move, double rotate) {
		robotDrive.arcadeDrive(move * m_elev_modify, rotate * m_elev_modify);
	}

	public void Tank(double left, double right) {
		robotDrive.tankDrive(left * m_elev_modify, right * m_elev_modify);
	}

	public void Tank(Joystick joy) {
		robotDrive.tankDrive(joy.getRawAxis(1) * m_elev_modify, joy.getRawAxis(5) * m_elev_modify);
	}

	public void BrakeCoast(NeutralMode mode) {
		L1.setNeutralMode(mode);
		L2.setNeutralMode(mode);
		L3.setNeutralMode(mode);
		L4.setNeutralMode(mode);
		R1.setNeutralMode(mode);
		R2.setNeutralMode(mode);
		R3.setNeutralMode(mode);
		R4.setNeutralMode(mode);
	}

	public void MotionMagic(double leftpos, double rightpos, double leftaccel, double rightaccel, double leftspeed,
			double rightspeed) {
		robotDrive.setSafetyEnabled(false);

		l_pos = leftpos * 4096 * 1;
		r_pos = rightpos * 4096 * -1;

		L1.configMotionAcceleration((int) (4096 * leftaccel), 10);
		R1.configMotionAcceleration((int) (4096 * rightaccel), 10);

		L1.configMotionCruiseVelocity((int) (4240 * leftspeed), 10);
		R1.configMotionCruiseVelocity((int) (4240 * rightspeed), 10);

		L1.set(ControlMode.MotionMagic, l_pos);
		R1.set(ControlMode.MotionMagic, r_pos);
	}

	public void Encoder(double left, double right, double leftspeed, double rightspeed) {
		robotDrive.setSafetyEnabled(false);

		L1.configPeakOutputForward(leftspeed, 10);
		L1.configPeakOutputReverse(-leftspeed, 10);
		R1.configPeakOutputForward(rightspeed, 10);
		R1.configPeakOutputReverse(-rightspeed, 10);

		l_pos = left * 4096;
		r_pos = -right * 4096;

		L1.set(ControlMode.Position, l_pos);
		R1.set(ControlMode.Position, r_pos);
	}

	public void EncoderDone() {
		R1.configPeakOutputForward(1, 0);
		R1.configPeakOutputReverse(-1, 0);
		L1.configPeakOutputForward(1, 0);
		L1.configPeakOutputReverse(-1, 0);

		L1.set(ControlMode.PercentOutput, 0);
		R1.set(ControlMode.PercentOutput, 0);

		l_pos = 0;
		r_pos = 0;

		robotDrive.setSafetyEnabled(true);
	}

	public boolean isMove(double multiplier) {
		if ((L1.getSelectedSensorPosition(0) < (l_pos + (102 * multiplier))
				&& L1.getSelectedSensorPosition(0) > (l_pos - (102 * multiplier)))
				&& R1.getSelectedSensorPosition(0) < (r_pos + (102 * multiplier))
				&& R1.getSelectedSensorPosition(0) > (r_pos - (102 * multiplier))) {
			return true;
		} else {
			return false;
		}
	}

	public void EncoderReset() {
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
	}

	public void LoggerUpdate() {
		SmartDashboard.putNumber("NavX Angle", Gyro.getAngle());

		SmartDashboard.putNumber("L1", L1.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("R1", R1.getSelectedSensorPosition(0));

	}

	public static class Constants {
		public static final int DRIVE_L_1 = 1, DRIVE_L_2 = 2, DRIVE_L_3 = 3, DRIVE_L_4 = 4;
		public static final int DRIVE_R_1 = 5, DRIVE_R_2 = 6, DRIVE_R_3 = 7, DRIVE_R_4 = 8;

		public static final int DRIVE_40_AMP_TRIGGER = 60, DRIVE_40_AMP_LIMIT = 30, DRIVE_40_AMP_TIME = 4000;

		public static final int DRIVE_30_AMP_TRIGGER = 45, DRIVE_30_AMP_LIMIT = 25, DRIVE_30_AMP_TIME = 3000;

		public static final double DRIVE_RAMP_TIME = 0.125;

	}

}
