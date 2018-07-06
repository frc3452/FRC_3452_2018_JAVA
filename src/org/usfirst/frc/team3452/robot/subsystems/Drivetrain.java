package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTele;
import org.usfirst.frc.team3452.robot.util.Constants;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Notifier;
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
	public double p_pos;
	private double l_pos = 0, r_pos = 0;

	// init timer
	public Timer timer = new Timer();

	/**
	 * Smartdashboard logging
	 * 
	 * @author max
	 */
	public void loggerUpdate() {
		SmartDashboard.putNumber("NavX Angle", Gyro.getAngle());

		SmartDashboard.putNumber("L1", (double) L1.getSelectedSensorPosition(0) / 4096);
		SmartDashboard.putNumber("R1", (double) -R1.getSelectedSensorPosition(0) / 4096);

		SmartDashboard.putNumber("L1 S", ((double) L1.getSelectedSensorVelocity(0)) / 1);
		SmartDashboard.putNumber("R1 S", -((double) R1.getSelectedSensorVelocity(0)) / 1);

		SmartDashboard.putNumber("% Complete", p_pos);

		SmartDashboard.putNumber("Elevator Enc", -Robot.elevator.Elev_1.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Elev Velocity", Robot.elevator.Elev_1.getSelectedSensorVelocity(0));

		SmartDashboard.putString("Selected auton", Robot.autonSelector.autonString);
		SmartDashboard.putString("Override String", Robot.autonSelector.overrideString);

		SmartDashboard.putString("FIELD DATA", Robot.lights.gsm());

		SmartDashboard.putNumber("Selector A", Robot.autonSelector.as_A.getValue());
		SmartDashboard.putNumber("Selector B", Robot.autonSelector.as_B.getValue());

		SmartDashboard.putNumber("UglyAnalog", Robot.autonSelector.uglyAnalog());

		SmartDashboard.putNumber("lerror", L1.getClosedLoopError(0));
		SmartDashboard.putNumber("rerror", R1.getClosedLoopError(0));

	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Drivetrain() {
		timer.stop();
		timer.reset();
		timer.start();

		// Talons
		L1 = new WPI_TalonSRX(Constants.kDrivetrain.L1);
		L2 = new WPI_TalonSRX(Constants.kDrivetrain.L2);
		L3 = new WPI_TalonSRX(Constants.kDrivetrain.L3);
		L4 = new WPI_TalonSRX(Constants.kDrivetrain.L4);
		R1 = new WPI_TalonSRX(Constants.kDrivetrain.R1);
		R2 = new WPI_TalonSRX(Constants.kDrivetrain.R2);
		R3 = new WPI_TalonSRX(Constants.kDrivetrain.R3);
		R4 = new WPI_TalonSRX(Constants.kDrivetrain.R4);

		Gyro = new AHRS(SPI.Port.kMXP);

		talonInit(L1);
		talonInit(L2);
		talonInit(L3);
		talonInit(L4);
		talonInit(R1);
		talonInit(R2);
		talonInit(R3);
		talonInit(R4);

		// Drivetrain
		robotDrive = new DifferentialDrive(L1, R1);
		robotDrive.setDeadband(0.045); // .08
		robotDrive.setSafetyEnabled(false);
		brake(NeutralMode.Coast);

		robotDrive.setSubsystem("Drive train");
		pdp.setSubsystem("Drive train");

		L1.setName("L1");
		L2.setName("L2");
		L3.setName("L3");
		L4.setName("L4");
		R1.setName("R1");
		R2.setName("R2");
		R3.setName("R3");
		R4.setName("R4");

		Gyro.reset();
	}

	/**
	 * talon init
	 */
	private void talonInit(WPI_TalonSRX talon) {
		// ---All talons---\\

		int id = talon.getDeviceID();
		boolean LorR;

		LorR = (id >= Constants.kDrivetrain.L1 && id <= Constants.kDrivetrain.L4);

		talon.setInverted((LorR) ? Constants.kDrivetrain.L_INVERT : Constants.kDrivetrain.R_INVERT);

		talon.configNominalOutputForward(0, 10);

		// AMP LIMIT
		// OUTER TALONS IN BLOCK = 40amp, INNER TALONS IN BLOCK = 30amp
		talon.configContinuousCurrentLimit(Constants.kDrivetrain.AMP_40_LIMIT, 10);
		talon.configPeakCurrentLimit(Constants.kDrivetrain.AMP_40_TRIGGER, 10);
		talon.configPeakCurrentDuration(Constants.kDrivetrain.AMP_40_TIME, 10);
		talon.configOpenloopRamp(Constants.kDrivetrain.RAMP_TIME, 10);

		talon.enableCurrentLimit(true);

		talon.configNeutralDeadband(0.05, 10);

		talon.setSubsystem("Drive train");
		
		// If Master
		if (id == Constants.kDrivetrain.L1 || id == Constants.kDrivetrain.R1) {
//			talon.selectProfileSlot(0, 10);
			
			talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
			talon.setSelectedSensorPosition(0, 0, 10);
			talon.setSensorPhase(true);

			// P .4
			// D 8,9

			talon.config_kF(0, .2744, 10); // was .2744
			talon.config_kP(0, 2.5, 10); // (.1 * 1023) / 7635
			talon.config_IntegralZone(0, 50, 10);
			talon.config_kI(0, 0, 10);

			// If left master
			if (LorR) {
				talon.config_kD(0, 8, 10);
			} else {
				// If right master
				talon.config_kD(0, 9, 10);
			}

			talon.config_kD(0, 25, 10);

			// If Follower
		} else {
			talon.follow(LorR ? L1 : R1);
		}
	}

	/**
	 * used for running 'process motion profile buffer' on drive train talons
	 * 
	 * @author Max
	 * @since 5/22/2018
	 */
	private class MotionProfileBuffer implements java.lang.Runnable {
		@Override
		public void run() {
			L1.processMotionProfileBuffer();
			R1.processMotionProfileBuffer();
		}
	}

	/**
	 * notifier object for running MotionProfileBuffer
	 */
	private Notifier processMotionProfile = new Notifier(new MotionProfileBuffer());

	/**
	 * Used to turn on/off runnable for motion profiling
	 * 
	 * 
	 * @param time
	 *            double - if 3452, stops notifier
	 */
	public void processMotionProfileBuffer(double time) {
		if (time == 3452)
			processMotionProfile.stop();
		else
			processMotionProfile.startPeriodic(time);
	}

	/**
	 * push motion profiles to drive train talons
	 * 
	 * @author max
	 * 
	 * @param mpL
	 *            (2-Dimensional double array)
	 * @param mpR
	 *            (2-Dimensional double array)
	 * @param mpDur
	 *            (Integer)
	 * 
	 * @since 4-22-2018
	 */
	public void motionProfileToTalons(double[][] mpL, double[][] mpR, Integer mpDur) {

		if (mpL.length != mpR.length)
			System.out.println("ERROR MOTION-PROFILE-SIZING ISSUE:\t\t" + mpL.length + "\t\t" + mpR.length);

		processMotionProfileBuffer((double) mpDur / (1000 * 2));

		TrajectoryPoint rightPoint = new TrajectoryPoint();
		TrajectoryPoint leftPoint = new TrajectoryPoint();

		// set talon srx
		L1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		R1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		L1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);
		R1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		// generate and push each mp point
		for (int i = 0; i < ((mpL.length <= mpR.length) ? mpL.length : mpR.length); i++) {

			leftPoint.position = mpL[i][0] * 4096;
			leftPoint.velocity = mpL[i][1] * 4096;

			rightPoint.position = mpR[i][0] * 4096;
			rightPoint.velocity = mpR[i][1] * 4096;

			leftPoint.timeDur = GetTrajectoryDuration(mpDur);
			rightPoint.timeDur = GetTrajectoryDuration(mpDur);

			leftPoint.headingDeg = 0;
			rightPoint.headingDeg = 0;

			leftPoint.profileSlotSelect0 = 0;
			rightPoint.profileSlotSelect0 = 0;

			leftPoint.profileSlotSelect1 = 0;
			rightPoint.profileSlotSelect1 = 0;

			leftPoint.zeroPos = false;
			rightPoint.zeroPos = false;

			if (i == 0) {
				leftPoint.zeroPos = true;
				rightPoint.zeroPos = true;
			}

			leftPoint.isLastPoint = false;
			rightPoint.isLastPoint = false;

			if ((i + 1) == mpL.length) {
				leftPoint.isLastPoint = true;
				rightPoint.isLastPoint = true;
			}

			L1.pushMotionProfileTrajectory(leftPoint);
			R1.pushMotionProfileTrajectory(rightPoint);
		}

		System.out.println("Motion profile pushed to Talons");
	}

	/**
	 * Used to process and push <b>parsed</b> motion profiles to drivetrain
	 * talons
	 * 
	 * @author max
	 * 
	 * 
	 * @since 4-22-2018
	 */
	public void motionProfileToTalons() {
		if (Robot.playback.mpL.size() != Robot.playback.mpR.size())
			System.out.println("ERROR MOTION-PROFILE-SIZING ISSUE:\t\t" + Robot.playback.mpL.size() + "\t\t"
					+ Robot.playback.mpR.size());

		processMotionProfileBuffer((double) Robot.playback.mpDur / (1000 * 2));

		TrajectoryPoint rightPoint = new TrajectoryPoint();
		TrajectoryPoint leftPoint = new TrajectoryPoint();

		// set talon srx
		L1.configMotionProfileTrajectoryPeriod(Robot.playback.mpDur, 10);
		R1.configMotionProfileTrajectoryPeriod(Robot.playback.mpDur, 10);
		L1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, Robot.playback.mpDur, 10);
		R1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, Robot.playback.mpDur, 10);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		// generate and push each mp point
		for (int i = 0; i < (Robot.playback.mpL.size() <= Robot.playback.mpR.size() ? Robot.playback.mpL.size()
				: Robot.playback.mpR.size()); i++) {

			leftPoint.position = Robot.playback.mpL.get(i).get(0) * 4096;
			leftPoint.velocity = Robot.playback.mpL.get(i).get(1) * 4096;

			rightPoint.position = Robot.playback.mpR.get(i).get(0) * 4096;
			rightPoint.velocity = Robot.playback.mpR.get(i).get(1) * 4096;

			leftPoint.timeDur = GetTrajectoryDuration(Robot.playback.mpDur);
			rightPoint.timeDur = GetTrajectoryDuration(Robot.playback.mpDur);

			leftPoint.headingDeg = 0;
			rightPoint.headingDeg = 0;

			leftPoint.profileSlotSelect0 = 0;
			rightPoint.profileSlotSelect0 = 0;

			leftPoint.profileSlotSelect1 = 0;
			rightPoint.profileSlotSelect1 = 0;

			leftPoint.zeroPos = false;
			rightPoint.zeroPos = false;

			if (i == 0) {
				leftPoint.zeroPos = true;
				rightPoint.zeroPos = true;
			}

			leftPoint.isLastPoint = false;
			rightPoint.isLastPoint = false;

			if ((i + 1) == Robot.playback.mpL.size()) {
				leftPoint.isLastPoint = true;
				rightPoint.isLastPoint = true;
			}

			L1.pushMotionProfileTrajectory(leftPoint);
			R1.pushMotionProfileTrajectory(rightPoint);
		}

		System.out.println("Motion profile pushed to Talons");
	}

	/**
	 * Validate duration
	 * 
	 * @author max
	 * @param durationMs
	 * @return TrajectoryDuration
	 */
	private TrajectoryDuration GetTrajectoryDuration(int durationMs) {
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;

		retval = retval.valueOf(durationMs);

		if (retval.value != durationMs)
			System.out.println("ERROR Invalid trajectory duration: " + durationMs);

		return retval;
	}

	/**
	 * @author max
	 * @param joy
	 */
	public void arcade(Joystick joy) {
		// Arcade((joy.getRawAxis(3) - joy.getRawAxis(2) * m_modify),
		// joy.getRawAxis(4) * m_modify);
		arcade(-joy.getRawAxis(1) * m_modify, ((joy.getRawAxis(3) - joy.getRawAxis(2)) * .635 * m_modify));
		Robot.elevator.setDriveLimit();
	}

	/**
	 * @author max
	 * @param move
	 * @param rotate
	 */
	public void arcade(double move, double rotate) {
		robotDrive.arcadeDrive(move * m_elev_modify, rotate * (m_elev_modify + .2));
	}

	/**
	 * @author max
	 * @param left
	 * @param right
	 */
	public void tank(double left, double right) {
		robotDrive.tankDrive(left * m_elev_modify, right * m_elev_modify);
	}

	/**
	 * @author max
	 * @param joy
	 */
	public void tank(Joystick joy) {
		robotDrive.tankDrive(-joy.getRawAxis(1) * m_elev_modify, -joy.getRawAxis(5) * m_elev_modify);
	}

	/**
	 * @author max
	 * @param mode
	 *            NeutralMode
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
	 */
	public void motionMagic(double leftpos, double rightpos, double leftaccel, double rightaccel, double leftspeed,
			double rightspeed) {
		double topspeed = 4240;

		robotDrive.setSafetyEnabled(false);

		l_pos = leftpos * 4096 * 1;
		r_pos = rightpos * 4096 * -1;

		double lp_pos = Math.abs(L1.getSelectedSensorPosition(0) / (4096 * leftpos));
		double rp_pos = Math.abs(R1.getSelectedSensorPosition(0) / (4096 * rightpos));

		p_pos = (lp_pos + rp_pos) / 2;

		L1.configMotionAcceleration((int) (topspeed * leftaccel), 10);
		R1.configMotionAcceleration((int) (topspeed * rightaccel), 10);

		L1.configMotionCruiseVelocity((int) (topspeed * leftspeed), 10);
		R1.configMotionCruiseVelocity((int) (topspeed * rightspeed), 10);

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
	 * @return isUnder
	 */
	public boolean encoderSpeedIsUnder(double value) {

		double l = Math.abs(L1.getSelectedSensorVelocity(0));
		double r = Math.abs(R1.getSelectedSensorVelocity(0));

		return l < value && r < value;
	}

	/**
	 * <li>Set control mode to PercentOutput.
	 * <li>Turns off runnable for motion profiling.
	 * <li>Clears motion profile trajectories
	 * <li>Set drive train masters peak outputs to full.
	 * <li>Percentage trackers to default.
	 * 
	 * @author max
	 */
	public void encoderDone() {
		L1.set(ControlMode.PercentOutput, 0);
		R1.set(ControlMode.PercentOutput, 0);

		processMotionProfileBuffer(3452);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		R1.configPeakOutputForward(1, 0);
		R1.configPeakOutputReverse(-1, 0);
		L1.configPeakOutputForward(1, 0);
		L1.configPeakOutputReverse(-1, 0);

		l_pos = 0;
		r_pos = 0;

		p_pos = 3452;
	}

	/**
	 * If L and R are within 102*multiplier of target positions, return true
	 * 
	 * @author max
	 * @param multiplier
	 *            double
	 * @return boolean
	 */
	public boolean encoderIsDone(double multiplier) {
		return (L1.getSelectedSensorPosition(0) < (l_pos + (102 * multiplier))
				&& L1.getSelectedSensorPosition(0) > (l_pos - (102 * multiplier)))
				&& R1.getSelectedSensorPosition(0) < (r_pos + (102 * multiplier))
				&& R1.getSelectedSensorPosition(0) > (r_pos - (102 * multiplier));
	}

	/**
	 * If L or R are within 102*multiplier of target positions, return true
	 * 
	 * @author max
	 * @param multiplier
	 *            double
	 * @return boolean
	 */
	public boolean encoderIsDoneEither(double multiplier) {
		return ((L1.getSelectedSensorPosition(0) < (l_pos + (102 * multiplier))
				&& L1.getSelectedSensorPosition(0) > (l_pos - (102 * multiplier))))
				|| (R1.getSelectedSensorPosition(0) < (r_pos + (102 * multiplier))
						&& R1.getSelectedSensorPosition(0) > (r_pos - (102 * multiplier)));
	}

	/**
	 * @author max
	 */
	public void encoderReset() {
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
	}

	@Override
	public void initDefaultCommand() {
		setDefaultCommand(new DriveTele());
	}

}
