
package org.usfirst.frc.team3452.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Utilities.FILES;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTele;
import org.usfirst.frc.team3452.robot.motionprofiles.MotionProfileTests;

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

	//init timer
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

		//Talons
		L1 = new WPI_TalonSRX(Constants.Drivetrain.L1);
		L2 = new WPI_TalonSRX(Constants.Drivetrain.L2);
		L3 = new WPI_TalonSRX(Constants.Drivetrain.L3);
		L4 = new WPI_TalonSRX(Constants.Drivetrain.L4);
		R1 = new WPI_TalonSRX(Constants.Drivetrain.R1);
		R2 = new WPI_TalonSRX(Constants.Drivetrain.R2);
		R3 = new WPI_TalonSRX(Constants.Drivetrain.R3);
		R4 = new WPI_TalonSRX(Constants.Drivetrain.R4);

		Gyro = new AHRS(SPI.Port.kMXP);

		//Config
		talonInit(L1);
		talonInit(L2);
		talonInit(L3);
		talonInit(L4);
		talonInit(R1);
		talonInit(R2);
		talonInit(R3);
		talonInit(R4);

		//Drivetrain
		robotDrive = new DifferentialDrive(L1, R1);
		robotDrive.setDeadband(0.045); //.08
		robotDrive.setSafetyEnabled(false);

		robotDrive.setSubsystem("Drive train");

		brake(NeutralMode.Coast);
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
		//---All talons---\\

		int id = talon.getDeviceID();
		boolean lOrR;

		lOrR = id >= Constants.Drivetrain.L1 && id <= Constants.Drivetrain.L4;

		talon.setInverted((lOrR) ? Constants.Drivetrain.L_INVERT : Constants.Drivetrain.R_INVERT);

		talon.configNominalOutputForward(0, 10);

		// AMP LIMIT
		// OUTER TALONS IN BLOCK = 40amp, INNER TALONS IN BLOCK = 30amp
		talon.configContinuousCurrentLimit(Constants.Drivetrain.AMP_40_LIMIT, 10);
		talon.configPeakCurrentLimit(Constants.Drivetrain.AMP_40_TRIGGER, 10);
		talon.configPeakCurrentDuration(Constants.Drivetrain.AMP_40_TIME, 10);
		talon.configOpenloopRamp(Constants.Drivetrain.RAMP_TIME, 10);

		talon.enableCurrentLimit(true);

		talon.configNeutralDeadband(0.05, 10);

		talon.setSubsystem("Drive train");

		//If Master
		if (id == Constants.Drivetrain.L1 || id == Constants.Drivetrain.R1) {
			talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute, 0, 10);
			talon.setSelectedSensorPosition(0, 0, 10);
			talon.setSensorPhase(true);

			talon.config_kF(0, .2379, 10);

			//If left master
			if (talon.getDeviceID() == Constants.Drivetrain.L1) {
				talon.config_kP(0, 0.425, 10);
				talon.config_kI(0, 0, 10);
				talon.config_kD(0, 4.25, 10);

			} else {
				//If right master
				talon.config_kP(0, 0.8, 10); //.8
				talon.config_kI(0, 0, 10);
				talon.config_kD(0, 4.25, 10);
			}

			talon.config_kF(0, 0, 10);
			talon.config_kP(0, 0.1, 10);
			talon.config_kI(0, 0, 10);
			talon.config_kD(0, 0, 10);

			//If Follower
		} else {
			talon.follow(lOrR ? L1 : R1);
		}
	}

	/**
	 * @author max
	 * @param joy
	 *            Joystick
	 */
	public void arcade(Joystick joy) {
		//		Arcade((joy.getRawAxis(3) - joy.getRawAxis(2) * m_modify), joy.getRawAxis(4) * m_modify);
		arcade(-joy.getRawAxis(1) * m_modify, ((joy.getRawAxis(3) - joy.getRawAxis(2)) * .635 * m_modify));
		Robot.elevator.setDriveLimit();
	}

	/**
	 * @author max
	 * @param move
	 *            double
	 * @param rotate
	 *            double
	 */
	public void arcade(double move, double rotate) {
		robotDrive.arcadeDrive(move * m_elev_modify, rotate * (m_elev_modify + .2));
	}

	/**
	 * @author max
	 * @param left
	 *            double
	 * @param right
	 *            double
	 */
	public void tank(double left, double right) {
		robotDrive.tankDrive(left * m_elev_modify, right * m_elev_modify);
	}

	/**
	 * @author max
	 * @param joy
	 *            Joystick
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
	 * Used to select which motion profile to send to talon
	 * 
	 * @author max
	 * @param file
	 *            FILES
	 * @see FILES
	 */
	public void selectMotionProfile(FILES file) {
		switch (file) {
		case Parse:
			motionProfileToTalons(Robot.playback.mpL, Robot.playback.mpR, Robot.playback.mpDur);
			break;
		case MotionProfileTests_Test1:
			motionProfileToTalons(MotionProfileTests.Test1.mpL, MotionProfileTests.Test1.mpR, MotionProfileTests.Test1.mpDur);
			break;
		default:
			break;
		}
	}

	private class MotionRunnable implements java.lang.Runnable {
		@Override
		public void run() {
			L1.processMotionProfileBuffer();
			R1.processMotionProfileBuffer();
		}
		
	}
	private Notifier processMotionProfile = new Notifier(new MotionRunnable());

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
	private void motionProfileToTalons(double[][] mpL, double[][] mpR, Integer mpDur) {
	
		if (mpL.length != mpR.length)
			System.out.println("ERROR MOTION-PROFILE-SIZING ISSUE:\t\t" + mpL.length + "\t\t" + mpR.length);

		processMotionProfileBuffer((double) mpDur / 2);

		TrajectoryPoint rightPoint = new TrajectoryPoint();
		TrajectoryPoint leftPoint = new TrajectoryPoint();

		//set talon srx 
		L1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		R1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		L1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);
		R1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		//generate and push each mp point
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
	 * Used to process and push motion profiles to drivetrain talons
	 * 
	 * @author max
	 * 
	 * @param mpL
	 *            (2-Dimensional double arrayList)
	 * @param mpR
	 *            (2-Dimensional double arrayList)
	 * @param mpDur
	 *            (Integer)
	 * 
	 * @since 4-22-2018
	 */
	private void motionProfileToTalons(ArrayList<ArrayList<Double>> mpL, ArrayList<ArrayList<Double>> mpR,
			Integer mpDur) {

		if (mpL.size() != mpR.size())
			System.out.println("ERROR MOTION-PROFILE-SIZING ISSUE:\t\t" + mpL.size() + "\t\t" + mpR.size());

		processMotionProfileBuffer((double) mpDur / 2);

		TrajectoryPoint rightPoint = new TrajectoryPoint();
		TrajectoryPoint leftPoint = new TrajectoryPoint();

		//set talon srx 
		L1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		R1.configMotionProfileTrajectoryPeriod(mpDur, 10);
		L1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);
		R1.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, mpDur, 10);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		//generate and push each mp point
		for (int i = 0; i < ((mpL.size() <= mpR.size()) ? mpL.size() : mpR.size()); i++) {

			leftPoint.position = mpL.get(i).get(0) * 4096;
			leftPoint.velocity = mpL.get(i).get(1) * 4096;

			rightPoint.position = mpL.get(i).get(0) * 4096;
			rightPoint.velocity = mpR.get(i).get(1) * 4096;

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
	 *            int
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
	 *            double
	 * @return isUnder boolean
	 * @since boolean
	 */
	public boolean encoderSpeedIsUnder(double value) {

		double l = Math.abs(L1.getSelectedSensorVelocity(0));
		double r = Math.abs(R1.getSelectedSensorVelocity(0));

		return l < value && r < value;
	}

	/**
	 * - Set drive train masters peak outputs to full. 
	 * - Set control mode to PercentOutput. 
	 * - Percentage trackers to default.
	 * - Turns off runnable for motion profiling. 
	 * 
	 * @author max
	 */
	public void encoderDone() {
		processMotionProfileBuffer(3452);
		
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
