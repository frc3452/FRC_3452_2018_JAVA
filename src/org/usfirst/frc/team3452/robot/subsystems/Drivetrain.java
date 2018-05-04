package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTele;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <h1>Drivetrain subsystem</h1> Handles drive train, smartdashboard updating,
 * PDP slots
 * 
 * @author max
 *
 */
public class Drivetrain extends Subsystem {
	// PDP
	public PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	// DRIVETRAIN
	public WPI_TalonSRX L1, L2, L3, L4, R1, R2, R3, R4;

	// ROBOT DRIVE OBJECT
	private DifferentialDrive robotDrive;

	// GYRO
	public AHRS Gyro;

	// variable init
	public double m_modify = 1, m_elev_modify = 1;
	public double l_pos = 0, r_pos = 0, p_pos;

	//init timer
	public Timer timer = new Timer();

	/**
	 * Smartdashboard logging
	 * 
	 * @author max
	 * @since
	 */
	public void loggerUpdate() {
		SmartDashboard.putNumber("NavX Angle", Gyro.getAngle());

		SmartDashboard.putNumber("L1", (double) L1.getSelectedSensorPosition(0) / 4096);
		SmartDashboard.putNumber("R1", (double) -R1.getSelectedSensorPosition(0) / 4096);

		SmartDashboard.putNumber("L1 S", ((double) L1.getSelectedSensorVelocity(0)) / 4096);
		SmartDashboard.putNumber("R1 S", -((double) R1.getSelectedSensorVelocity(0)) / 4096);

		SmartDashboard.putNumber("% Complete", p_pos);

		SmartDashboard.putNumber("Elevator Enc", -Robot.elevator.Elev_1.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elev Velocity", Robot.elevator.Elev_1.getSelectedSensorVelocity(0));

		SmartDashboard.putString("Selected auton", Robot.autonSelector.autonString);
		SmartDashboard.putString("Override String", Robot.autonSelector.overrideString);

		SmartDashboard.putString("FIELD DATA", Robot.lights.gsm());

		SmartDashboard.putNumber("Selector A", Robot.autonSelector.as_A.getValue());
		SmartDashboard.putNumber("Selector B", Robot.autonSelector.as_B.getValue());
	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public void initHardware() {
		timer.stop();
		timer.reset();
		timer.start();

		L1 = new WPI_TalonSRX(Constants.Drivetrain.L_1);
		L2 = new WPI_TalonSRX(Constants.Drivetrain.L_2);
		L3 = new WPI_TalonSRX(Constants.Drivetrain.L_3);
		L4 = new WPI_TalonSRX(Constants.Drivetrain.L_4);

		R1 = new WPI_TalonSRX(Constants.Drivetrain.R_1);
		R2 = new WPI_TalonSRX(Constants.Drivetrain.R_2);
		R3 = new WPI_TalonSRX(Constants.Drivetrain.R_3);
		R4 = new WPI_TalonSRX(Constants.Drivetrain.R_4);

		L1.setInverted(Constants.Drivetrain.L_INVERT);
		L2.setInverted(Constants.Drivetrain.L_INVERT);
		L3.setInverted(Constants.Drivetrain.L_INVERT);
		L4.setInverted(Constants.Drivetrain.L_INVERT);

		R1.setInverted(Constants.Drivetrain.R_INVERT);
		R2.setInverted(Constants.Drivetrain.R_INVERT);
		R3.setInverted(Constants.Drivetrain.R_INVERT);
		R4.setInverted(Constants.Drivetrain.R_INVERT);

		Gyro = new AHRS(SPI.Port.kMXP);

		robotDrive = new DifferentialDrive(L1, R1);

		robotDrive.setDeadband(0.045); //.08
		robotDrive.setSafetyEnabled(false);

		// follower mode
		L2.follow(L1);
		L3.follow(L1);
		L4.follow(L1);

		R2.follow(R1);
		R3.follow(R1);
		R4.follow(R1);

		//MOTION PROFILING
		L1.configMotionProfileTrajectoryPeriod(10, 10);
		R1.configMotionProfileTrajectoryPeriod(10, 10);

		L1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);
		R1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, 10);

		// encoder init
		L1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
		R1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
		L1.setSensorPhase(true);
		R1.setSensorPhase(true);

		L1.config_kF(0, 0, 10);
		L1.config_kP(0, 0.425, 10);
		L1.config_kI(0, 0.0000004, 10);
		L1.config_kD(0, 4.25, 10);

		R1.config_kF(0, 0, 10);
		R1.config_kP(0, 0.8, 10); //.8
		R1.config_kI(0, 0.0000004, 10);
		R1.config_kD(0, 4.25, 10);

		L1.config_kP(0, 0.06, 10);
		R1.config_kP(0, 0.06, 10);

		L1.config_kI(0, 0, 10);
		R1.config_kI(0, 0, 10);

		L1.config_kD(0, 0, 10);
		R1.config_kD(0, 0, 10);

		// NOMINAL OUTPUT
		L1.configNominalOutputForward(0, 10);
		L1.configNominalOutputReverse(0, 10);
		R1.configNominalOutputForward(0, 10);
		R1.configNominalOutputReverse(0, 10);

		// AMP LIMIT
		// OUTER TALONS IN BLOCK = 40amp, INNER TALONS IN BLOCK = 30amp
		L1.configContinuousCurrentLimit(Constants.Drivetrain.AMP_40_LIMIT, 10);
		L1.configPeakCurrentLimit(Constants.Drivetrain.AMP_40_TRIGGER, 10);
		L1.configPeakCurrentDuration(Constants.Drivetrain.AMP_40_TIME, 10);
		L1.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		L2.configContinuousCurrentLimit(Constants.Drivetrain.AMP_40_LIMIT, 10);
		L2.configPeakCurrentLimit(Constants.Drivetrain.AMP_40_TRIGGER, 10);
		L2.configPeakCurrentDuration(Constants.Drivetrain.AMP_40_TIME, 10);
		L2.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		R1.configContinuousCurrentLimit(Constants.Drivetrain.AMP_40_LIMIT, 10);
		R1.configPeakCurrentLimit(Constants.Drivetrain.AMP_40_TRIGGER, 10);
		R1.configPeakCurrentDuration(Constants.Drivetrain.AMP_40_TIME, 10);
		R1.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		R2.configContinuousCurrentLimit(Constants.Drivetrain.AMP_40_LIMIT, 10);
		R2.configPeakCurrentLimit(Constants.Drivetrain.AMP_40_TRIGGER, 10);
		R2.configPeakCurrentDuration(Constants.Drivetrain.AMP_40_TIME, 10);
		R2.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		L3.configContinuousCurrentLimit(Constants.Drivetrain.AMP_30_LIMIT, 10);
		L3.configPeakCurrentLimit(Constants.Drivetrain.AMP_30_TRIGGER, 10);
		L3.configPeakCurrentDuration(Constants.Drivetrain.AMP_30_TIME, 10);
		L3.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		L4.configContinuousCurrentLimit(Constants.Drivetrain.AMP_30_LIMIT, 10);
		L4.configPeakCurrentLimit(Constants.Drivetrain.AMP_30_TRIGGER, 10);
		L4.configPeakCurrentDuration(Constants.Drivetrain.AMP_30_TIME, 10);
		L4.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		R3.configContinuousCurrentLimit(Constants.Drivetrain.AMP_30_LIMIT, 10);
		R3.configPeakCurrentLimit(Constants.Drivetrain.AMP_30_TRIGGER, 10);
		R3.configPeakCurrentDuration(Constants.Drivetrain.AMP_30_TIME, 10);
		R3.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		R4.configContinuousCurrentLimit(Constants.Drivetrain.AMP_30_LIMIT, 10);
		R4.configPeakCurrentLimit(Constants.Drivetrain.AMP_30_TRIGGER, 10);
		R4.configPeakCurrentDuration(Constants.Drivetrain.AMP_30_TIME, 10);
		R4.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		L1.enableCurrentLimit(true);
		L2.enableCurrentLimit(true);
		L3.enableCurrentLimit(true);
		L4.enableCurrentLimit(true);
		R1.enableCurrentLimit(true);
		R2.enableCurrentLimit(true);
		R3.enableCurrentLimit(true);
		R4.enableCurrentLimit(true);

		// COAST MODE
		brake(NeutralMode.Coast);

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

		//MUST BE AT END
		Gyro.reset();
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveTele());
	}

	/**
	 * @author max
	 * @param joy
	 * @since
	 */
	public void arcade(Joystick joy) {
		//		Arcade((joy.getRawAxis(3) - joy.getRawAxis(2) * m_modify), joy.getRawAxis(4) * m_modify);
		arcade(-joy.getRawAxis(1) * m_modify, ((joy.getRawAxis(3) - joy.getRawAxis(2)) * .635 * m_modify));
		Robot.elevator.setDriveLimit();
	}

	/**
	 * @author max
	 * @param move
	 * @param rotate
	 * @since
	 */
	public void arcade(double move, double rotate) {
		robotDrive.arcadeDrive(move * m_elev_modify, rotate * (m_elev_modify + .2));
	}

	/**
	 * @author max
	 * @param left
	 * @param right
	 * @since
	 */
	public void tank(double left, double right) {
		robotDrive.tankDrive(left * m_elev_modify, right * m_elev_modify);
	}

	/**
	 * @author max
	 * @param joy
	 * @since
	 */
	public void tank(Joystick joy) {
		robotDrive.tankDrive(-joy.getRawAxis(1) * m_elev_modify, -joy.getRawAxis(5) * m_elev_modify);
	}

	/**
	 * @author max
	 * @param mode
	 */
	public void brake(NeutralMode mode) {
		L1.setNeutralMode(mode);
		L2.setNeutralMode(mode);
		L3.setNeutralMode(mode);
		L4.setNeutralMode(mode);
		R1.setNeutralMode(mode);
		R2.setNeutralMode(mode);
		R3.setNeutralMode(mode);
		R4.setNeutralMode(mode);
	}

	/**
	 * Used to process and push motion profiles to drivetrain talons
	 * 
	 * @author max
	 * @since 4-22-2018
	 */
	public void parsedFileToTalons() {
		TrajectoryPoint rightPoint = new TrajectoryPoint();
		TrajectoryPoint leftPoint = new TrajectoryPoint();

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		L1.configMotionProfileTrajectoryPeriod(10, 10);
		R1.configMotionProfileTrajectoryPeriod(10, 10);

		//MOTION PROFILE
		for (int i = 0; i < Robot.playback.mpLP.size(); i++) {

			leftPoint.position = Robot.playback.mpLP.get(i);
			leftPoint.velocity = Robot.playback.mpLS.get(i);

			rightPoint.position = Robot.playback.mpRP.get(i);
			rightPoint.velocity = Robot.playback.mpRS.get(i);

			leftPoint.headingDeg = 0;
			rightPoint.headingDeg = 0;

			leftPoint.profileSlotSelect0 = 0;
			rightPoint.profileSlotSelect0 = 0;

			leftPoint.profileSlotSelect1 = 0;
			rightPoint.profileSlotSelect1 = 0;

			leftPoint.timeDur = GetTrajectoryDuration(Robot.playback.mpDur);
			rightPoint.timeDur = GetTrajectoryDuration(Robot.playback.mpDur);

			leftPoint.zeroPos = false;
			rightPoint.zeroPos = false;

			if (i == 0) {
				leftPoint.zeroPos = true;
				rightPoint.zeroPos = true;
			}

			leftPoint.isLastPoint = false;
			rightPoint.isLastPoint = false;

			if ((i + 1) == Robot.playback.mpLP.size()) {
				leftPoint.isLastPoint = true;
				rightPoint.isLastPoint = true;
			}

			System.out.println(leftPoint.position + "\t\t" + leftPoint.velocity + "\t\t" + rightPoint.position + "\t\t"
					+ rightPoint.velocity);

			L1.pushMotionProfileTrajectory(leftPoint);
			R1.pushMotionProfileTrajectory(rightPoint);
		}

		System.out.println("Motion profile pushed to Talons");

	}

	private TrajectoryDuration GetTrajectoryDuration(int durationMs) {
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;

		retval = retval.valueOf(durationMs);

		if (retval.value != durationMs)
			DriverStation.reportError("Trajectory duration not supported", false);

		return retval;
	}

	/**
	 * @author max
	 * @param leftpos
	 *            rotations
	 * @param rightpos
	 *            rotations
	 * @param leftaccel
	 *            rotations per second (accel)
	 * @param rightaccel
	 *            rotations per second (accel)
	 * @param leftspeed
	 *            rotations per second (top speed)
	 * @param rightspeed
	 *            rotations per second (top speed)
	 * @since
	 */
	public void motionMagic(double leftpos, double rightpos, double leftaccel, double rightaccel, double leftspeed,
			double rightspeed) {

		robotDrive.setSafetyEnabled(false);

		l_pos = leftpos * 4096 * 1;
		r_pos = rightpos * 4096 * -1;

		double lp_pos = Math.abs(L1.getSelectedSensorPosition(0) / (4096 * leftpos));
		double rp_pos = Math.abs(R1.getSelectedSensorPosition(0) / (4096 * rightpos));

		p_pos = (lp_pos + rp_pos) / 2;

		L1.configMotionAcceleration((int) (4096 * leftaccel), 10);
		R1.configMotionAcceleration((int) (4096 * rightaccel), 10);

		L1.configMotionCruiseVelocity((int) (4240 * leftspeed), 10);
		R1.configMotionCruiseVelocity((int) (4240 * rightspeed), 10);

		L1.set(ControlMode.MotionMagic, l_pos);
		R1.set(ControlMode.MotionMagic, r_pos);
	}

	/**
	 * @author max
	 * @param leftpos
	 *            rotations
	 * @param rightpos
	 *            rotations
	 * @param leftspeed
	 *            speed (percentage)
	 * @param rightspeed
	 *            speed (percentage)
	 * @since
	 * @deprecated
	 */
	@Deprecated
	public void encoder(double leftpos, double rightpos, double leftspeed, double rightspeed) {
		L1.configPeakOutputForward(leftspeed, 10);
		L1.configPeakOutputReverse(-leftspeed, 10);
		R1.configPeakOutputForward(rightspeed, 10);
		R1.configPeakOutputReverse(-rightspeed, 10);

		l_pos = leftpos * 4096;
		r_pos = -rightpos * 4096;

		p_pos = Math.abs(((L1.getSelectedSensorPosition(0) / (4096 * leftpos))
				+ (R1.getSelectedSensorPosition(0) / (4096 * rightpos))) / 2);

		L1.set(ControlMode.Position, l_pos);
		R1.set(ControlMode.Position, r_pos);
	}

	/**
	 * @author max
	 * @param value
	 * @return
	 * @since boolean
	 */
	public boolean encoderSpeedIsUnder(double value) {

		double l = Math.abs(L1.getSelectedSensorVelocity(0));
		double r = Math.abs(R1.getSelectedSensorVelocity(0));

		if (l < value && r < value)
			return true;
		else
			return false;
	}

	/**
	 * Set drive train masters peak outputs to full. Set control mode to
	 * PercentOutput. Percentage trackers to default
	 * 
	 * @author max
	 * @since
	 */
	public void encoderDone() {
		R1.configPeakOutputForward(1, 0);
		R1.configPeakOutputReverse(-1, 0);
		L1.configPeakOutputForward(1, 0);
		L1.configPeakOutputReverse(-1, 0);

		L1.set(ControlMode.PercentOutput, 0);
		R1.set(ControlMode.PercentOutput, 0);

		l_pos = 0;
		r_pos = 0;

		p_pos = 3452;

	}

	/**
	 * If L and R are within 102*multiplier of target positions, return true
	 * 
	 * @author max
	 * @param multiplier
	 * @return boolean
	 * @since
	 */
	public boolean encoderIsDone(double multiplier) {
		if ((L1.getSelectedSensorPosition(0) < (l_pos + (102 * multiplier))
				&& L1.getSelectedSensorPosition(0) > (l_pos - (102 * multiplier)))
				&& R1.getSelectedSensorPosition(0) < (r_pos + (102 * multiplier))
				&& R1.getSelectedSensorPosition(0) > (r_pos - (102 * multiplier)))
			return true;
		else
			return false;
	}

	/**
	 * If L or R are within 102*multiplier of target positions, return true
	 * 
	 * @author max
	 * @param multiplier
	 * @return
	 * @since
	 */
	public boolean encoderIsDoneEither(double multiplier) {
		if (((L1.getSelectedSensorPosition(0) < (l_pos + (102 * multiplier))
				&& L1.getSelectedSensorPosition(0) > (l_pos - (102 * multiplier))))
				|| (R1.getSelectedSensorPosition(0) < (r_pos + (102 * multiplier))
						&& R1.getSelectedSensorPosition(0) > (r_pos - (102 * multiplier))))
			return true;
		else
			return false;
	}

	/**
	 * @author max
	 * @since
	 */
	public void encoderReset() {
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
	}

	/**
	 * @author max
	 *
	 */
	public static enum CONTROLLER {
		DRIVER, OPERATOR, BOTH;
	}
}
