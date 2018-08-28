package org.usfirst.frc.team3452.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team3452.robot.Constants.kDrivetrain;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZJoystick;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSRX.Breaker;
import org.usfirst.frc.team3452.robot.util.GZSRX.Master;
import org.usfirst.frc.team3452.robot.util.GZSRX.Side;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;
import org.usfirst.frc.team3452.robot.util.Util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain extends GZSubsystem {

	private DriveState mState = DriveState.NEUTRAL;
	private DriveState mWantedState = mState;
	public IO mIO = new IO();

	// PDP
	private PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	// DRIVETRAIN
	private GZSRX L1, L2, L3, L4, R1, R2, R3, R4;
	private List<GZSRX> controllers;

	// GYRO
	private AHRS mGyro;

	private double percentageModify = 1;
	private double percentageComplete;
	private double left_target = 0, right_target = 0;

	public Drivetrain() {
		L1 = new GZSRX(kDrivetrain.L1, Breaker.AMP_40, Side.LEFT, Master.MASTER);
		L2 = new GZSRX(kDrivetrain.L2, Breaker.AMP_40, Side.LEFT, Master.FOLLOWER);
		L3 = new GZSRX(kDrivetrain.L3, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);
		L4 = new GZSRX(kDrivetrain.L4, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);

		R1 = new GZSRX(kDrivetrain.R1, Breaker.AMP_40, Side.RIGHT, Master.MASTER);
		R2 = new GZSRX(kDrivetrain.R2, Breaker.AMP_40, Side.RIGHT, Master.FOLLOWER);
		R3 = new GZSRX(kDrivetrain.R3, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);
		R4 = new GZSRX(kDrivetrain.R4, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);

		controllers = Arrays.asList(L1, L2, L3, L4, R1, R2, R3, R4);

		mGyro = new AHRS(SPI.Port.kMXP);

		brake(NeutralMode.Coast);

		talonInit(controllers);

		pdp.setSubsystem("Drive train");

		L1.setName("L1");
		L2.setName("L2");
		L3.setName("L3");
		L4.setName("L4");
		R1.setName("R1");
		R2.setName("R2");
		R3.setName("R3");
		R4.setName("R4");

		mGyro.reset();
	}

	@Override
	protected synchronized void out() {
		L1.set(mIO.control_mode, mIO.left_output);
		R1.set(mIO.control_mode, mIO.right_output);
	}

	public enum DriveState {
		OPEN_LOOP, OPEN_LOOP_DRIVER, DEMO, NEUTRAL, MOTION_MAGIC, MOTION_PROFILE,
	}

	@Override
	public synchronized void stop() {
		setWantedState(DriveState.NEUTRAL);
	}

	public synchronized void setWantedState(DriveState wantedState) {
		this.mWantedState = wantedState;
	}

	private synchronized void handleStates() {
		
		//Do not allow .disable() while connected to the field.
		if (((this.isDisabed() && !Robot.auton.isFMS()) || mWantedState == DriveState.NEUTRAL)) {

			if (currentStateIsNot(DriveState.NEUTRAL)) {
				onStateExit(mState);
				mState = DriveState.NEUTRAL;
				onStateStart(mState);
			}

		} else if (Robot.auton.isDemo()) {

			if (currentStateIsNot(DriveState.DEMO)) {
				onStateExit(mState);
				mState = DriveState.DEMO;
				onStateStart(mState);
			}

		} else if (mState != mWantedState) {
			onStateExit(mState);
			mState = mWantedState;
			onStateStart(mState);
		}
	}
	
	private void talonInit(List<GZSRX> srx) {
		for (GZSRX s : srx) {
//			String name = talon.getName();
			s.configFactoryDefault(GZSRX.TIMEOUT);

			s.setInverted((s.getSide() == Side.LEFT) ? kDrivetrain.L_INVERT : kDrivetrain.R_INVERT);

			// TODO ISSUE #11
			// CURRENT LIMIT
			s.configContinuousCurrentLimit(
					s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_LIMIT : kDrivetrain.AMP_30_LIMIT,
					GZSRX.TIMEOUT);
			s.configPeakCurrentLimit(
					s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_TRIGGER : kDrivetrain.AMP_30_TRIGGER,
					GZSRX.TIMEOUT);
			s.configPeakCurrentDuration(
					s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_TIME : kDrivetrain.AMP_30_TIME,
					GZSRX.TIMEOUT);
			s.configOpenloopRamp(kDrivetrain.OPEN_LOOP_RAMP_TIME, GZSRX.TIMEOUT);
			s.enableCurrentLimit(true);

			s.configNeutralDeadband(0.05, GZSRX.TIMEOUT);

			s.setSubsystem("Drive train");

			if (s.getMaster() == Master.MASTER) {

				final ErrorCode sensorPresent = s.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,
						0, GZSRX.TIMEOUT);

				if (sensorPresent != ErrorCode.OK)
					System.out.println("ERROR DRIVETRAIN " + s.getSide() + " ENCODER NOT DETECTED.");

				s.setSelectedSensorPosition(0, 0, GZSRX.TIMEOUT);
				s.setSensorPhase(true);

				if (s.getSide() == Side.LEFT) {
					s.config_kP(0, 0.425, GZSRX.TIMEOUT);
					s.config_kI(0, 0, GZSRX.TIMEOUT);
					s.config_kD(0, 4.25, GZSRX.TIMEOUT);

				} else {
					s.config_kP(0, 0.425, GZSRX.TIMEOUT); // .8
					s.config_kI(0, 0, GZSRX.TIMEOUT);
					s.config_kD(0, 4.25, GZSRX.TIMEOUT);
				}
			} else {
				s.follow(s.getSide() == Side.LEFT ? L1 : R1);
			}

		}
	}

	private synchronized void onStateStart(DriveState newState) {
		switch (newState) {
		case MOTION_MAGIC:
			break;
		case MOTION_PROFILE:
			break;
		case NEUTRAL:
			break;
		case OPEN_LOOP:
			break;
		case OPEN_LOOP_DRIVER:
			break;
		case DEMO:
			break;
		default:
			break;
		}
	}

	public synchronized void onStateExit(DriveState prevState) {
		switch (prevState) {
		case MOTION_MAGIC:
			encoderDone();
			break;
		case MOTION_PROFILE:
			break;
		case NEUTRAL:
			break;
		case OPEN_LOOP:
			break;
		case OPEN_LOOP_DRIVER:
			break;
		case DEMO:
			break;
		default:
			break;
		}
	}

	@Override
	public synchronized void loop() {
		handleStates();
		in();
		out();

		switch (mState) {
		case MOTION_MAGIC:

			mIO.control_mode = ControlMode.MotionMagic;
			mIO.left_output = mIO.left_desired_output;
			mIO.right_output = mIO.right_desired_output;

			break;
		case MOTION_PROFILE:

			mIO.control_mode = ControlMode.MotionProfile;
			mIO.left_desired_output = mIO.right_desired_output = SetValueMotionProfile.Enable.value;
			
			mIO.left_output = mIO.left_desired_output;
			mIO.right_output = mIO.right_desired_output;

			break;
		case NEUTRAL:

			mIO.control_mode = ControlMode.Disabled;
			mIO.left_output = 0;
			mIO.right_output = 0;

			break;
		case OPEN_LOOP:

			mIO.control_mode = ControlMode.PercentOutput;
			mIO.left_output = mIO.left_desired_output;
			mIO.right_output = mIO.right_desired_output;

			break;
		case OPEN_LOOP_DRIVER:

			arcade(OI.driverJoy);
			mIO.control_mode = ControlMode.PercentOutput;
			mIO.left_output = mIO.left_desired_output;
			mIO.right_output = mIO.right_desired_output;

			break;
		case DEMO:

			alternateArcade(OI.driverJoy);
			mIO.control_mode = ControlMode.PercentOutput;
			mIO.left_output = mIO.left_desired_output * kDrivetrain.DEMO_DRIVE_MODIFIER;
			mIO.right_output = mIO.right_desired_output * kDrivetrain.DEMO_DRIVE_MODIFIER;

			break;

		default:
			System.out.println("WARNING: Incorrect drive state " + mState + " reached.");
			break;
		}

	}

	public static class IO {
		// in

		// Left Encoder
		public Double left_encoder_ticks = Double.NaN, left_encoder_rotations = Double.NaN,
				left_encoder_vel = Double.NaN, left_encoder_speed = Double.NaN;

		// Right Encoder
		public Double right_encoder_ticks = Double.NaN, right_encoder_rotations = Double.NaN,
				right_encoder_vel = Double.NaN, right_encoder_speed = Double.NaN;

		// Amperage
		public Double L1_amp = Double.NaN, L2_amp = Double.NaN, L3_amp = Double.NaN, L4_amp = Double.NaN,
				R1_amp = Double.NaN, R2_amp = Double.NaN, R3_amp = Double.NaN, R4_amp = Double.NaN;

		// Voltage
		public Double L1_volt = Double.NaN, L2_volt = Double.NaN, L3_volt = Double.NaN, L4_volt = Double.NaN,
				R1_volt = Double.NaN, R2_volt = Double.NaN, R3_volt = Double.NaN, R4_volt = Double.NaN;

		// out
		private double left_output = 0;
		public double left_desired_output = 0;

		private double right_output = 0;
		public double right_desired_output = 0;
		ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected synchronized void in() {
		mIO.left_encoder_ticks = (double) L1.getSelectedSensorPosition(0);
		mIO.left_encoder_vel = (double) L1.getSelectedSensorVelocity(0);
		mIO.left_encoder_rotations = Units.ticks_to_rotations(mIO.left_encoder_ticks);
		mIO.left_encoder_speed = Units.ticks_to_rotations(mIO.left_encoder_vel);

		mIO.right_encoder_ticks = (double) -R1.getSelectedSensorPosition(0);
		mIO.right_encoder_vel = (double) -R1.getSelectedSensorVelocity(0);
		mIO.right_encoder_rotations = Units.ticks_to_rotations(mIO.right_encoder_ticks);
		mIO.right_encoder_speed = Units.ticks_to_rotations(mIO.right_encoder_vel);

		mIO.L1_amp = L1.getOutputCurrent();
		mIO.L2_amp = L2.getOutputCurrent();
		mIO.L3_amp = L3.getOutputCurrent();
		mIO.L4_amp = L4.getOutputCurrent();
		mIO.R1_amp = R1.getOutputCurrent();
		mIO.R2_amp = R2.getOutputCurrent();
		mIO.R3_amp = R3.getOutputCurrent();
		mIO.R4_amp = R4.getOutputCurrent();

		mIO.L1_volt = L1.getMotorOutputVoltage();
		mIO.L2_volt = L2.getMotorOutputVoltage();
		mIO.L3_volt = L3.getMotorOutputVoltage();
		mIO.L4_volt = L4.getMotorOutputVoltage();
		mIO.R1_volt = R1.getMotorOutputVoltage();
		mIO.R2_volt = R2.getMotorOutputVoltage();
		mIO.R3_volt = R3.getMotorOutputVoltage();
		mIO.R4_volt = R4.getMotorOutputVoltage();
	}

	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putNumber("NavX Angle", mGyro.getAngle());

		SmartDashboard.putNumber("L1", mIO.left_encoder_rotations);
		SmartDashboard.putNumber("R1", mIO.right_encoder_rotations);

		SmartDashboard.putNumber("L1 Vel", mIO.left_encoder_vel);
		SmartDashboard.putNumber("R1 Vel", mIO.right_encoder_vel);

		SmartDashboard.putNumber("PercentageCompleted", getPercentageComplete());
	}

	// called in OPEN_LOOP_DRIVER state
	private synchronized void arcade(GZJoystick joy) {
		arcadeNoState(joy.getLeftAnalogY(), (joy.getRightTrigger() - joy.getLeftTrigger()) * .8);
	}

	// called in DEMO state
	private synchronized void alternateArcade(GZJoystick joy) {
		arcadeNoState(joy.getLeftAnalogY(), (joy.getRightAnalogX() * .85));
	}

	/**
	 * WARNING, TO ONLY BE CALLED BY METHOD THAT CALLS setState!!!!!!!!!
	 * 
	 * @param move
	 * @param rotate
	 */
	private synchronized void arcadeNoState(double move, double rotate) {
		double[] temp = arcadeToLR(move * Robot.elevator.getPercentageModify() * percentageModify,
				rotate * Robot.elevator.getPercentageModify() * percentageModify);

		mIO.left_desired_output = temp[0];
		mIO.right_desired_output = temp[1];
	}

	public synchronized void arcade(double move, double rotate) {
		setWantedState(DriveState.OPEN_LOOP);
		arcadeNoState(move, rotate);
	}

	// Modified from DifferentialDrive.java to produce double array, [0] being left
	// motor value, [1] being right motor value
	public synchronized double[] arcadeToLR(double xSpeed, double zRotation) {
		xSpeed = Util.limit(xSpeed);
		xSpeed = Util.applyDeadband(xSpeed, kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);

		zRotation = Util.limit(zRotation);
		zRotation = Util.applyDeadband(zRotation, kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);

		double leftMotorOutput;
		double rightMotorOutput;

		double maxInput = Math.copySign(Math.max(Math.abs(xSpeed), Math.abs(zRotation)), xSpeed);

		if (xSpeed >= 0.0) {
			// First quadrant, else second quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			} else {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			}
		} else {
			// Third quadrant, else fourth quadrant
			if (zRotation >= 0.0) {
				leftMotorOutput = xSpeed + zRotation;
				rightMotorOutput = maxInput;
			} else {
				leftMotorOutput = maxInput;
				rightMotorOutput = xSpeed - zRotation;
			}
		}

		double retval[] = { 0, 0 };
		retval[0] = Util.limit(leftMotorOutput);
		retval[1] = -Util.limit(rightMotorOutput);

		return retval;
	}

	public synchronized void tank(double left, double right) {
		setWantedState(DriveState.OPEN_LOOP);
		mIO.left_desired_output = left * Robot.elevator.getPercentageModify() * percentageModify;
		mIO.right_desired_output = right * Robot.elevator.getPercentageModify() * percentageModify;
	}

	public synchronized void tank(GZJoystick joy) {
		tank(joy.getLeftAnalogY(), joy.getRightAnalogY());
	}

	public synchronized void brake(NeutralMode mode) {
		controllers.forEach((s) -> s.setNeutralMode(mode));
	}

	public synchronized void motionMagic(double leftRotations, double rightRotations, double leftAccel,
			double rightAccel, double leftSpeed, double rightSpeed) {
		setWantedState(DriveState.MOTION_MAGIC);

		double topspeed = 3941;

		left_target = Units.rotations_to_ticks(leftRotations);
		right_target = Units.rotations_to_ticks(rightRotations);

		percentageComplete = Math
				.abs(((mIO.left_encoder_rotations / left_target) + (mIO.right_encoder_rotations / right_target)) / 2);

		L1.configMotionAcceleration((int) (topspeed * leftAccel), 10);
		R1.configMotionAcceleration((int) (topspeed * rightAccel), 10);

		L1.configMotionCruiseVelocity((int) (topspeed * leftSpeed), 10);
		R1.configMotionCruiseVelocity((int) (topspeed * rightSpeed), 10);

		mIO.left_desired_output = left_target;
		mIO.right_desired_output = right_target;
	}

	public synchronized boolean encoderSpeedIsUnder(double ticksPer100Ms) {
		double l = Math.abs(mIO.left_encoder_vel);
		double r = Math.abs(mIO.right_encoder_vel);

		return l < ticksPer100Ms && r < ticksPer100Ms;
	}

	public synchronized void encoderDone() {
		stop();

		processMotionProfileBuffer(3452);

		L1.clearMotionProfileTrajectories();
		R1.clearMotionProfileTrajectories();

		R1.configPeakOutputForward(1, 0);
		R1.configPeakOutputReverse(-1, 0);
		L1.configPeakOutputForward(1, 0);
		L1.configPeakOutputReverse(-1, 0);

		left_target = 0;
		right_target = 0;

		percentageComplete = 3452;
	}

	public synchronized boolean encoderIsDone(double multiplier) {
		return (mIO.left_encoder_ticks < left_target + 102 * multiplier)
				&& (mIO.left_encoder_ticks > left_target - 102 * multiplier)
				&& (mIO.right_encoder_ticks < right_target + 102 * multiplier)
				&& (mIO.right_encoder_ticks > right_target - 102 * multiplier);
	}

	public synchronized boolean encoderIsDoneEither(double multiplier) {
		return (mIO.left_encoder_ticks < left_target + 102 * multiplier
				&& mIO.left_encoder_ticks > left_target - 102 * multiplier)
				|| (mIO.right_encoder_ticks < right_target + 102 * multiplier
						&& mIO.right_encoder_ticks > right_target - 102 * multiplier);
	}

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
	 * @param time double - if 3452, stops notifier
	 */
	public synchronized void processMotionProfileBuffer(double time) {
		if (time == 3452)
			processMotionProfile.stop();
		else
			processMotionProfile.startPeriodic(time);
	}

	public synchronized void getMotionProfileStatus(Side side, MotionProfileStatus statusToFill) {
		if (side == Side.LEFT)
			L1.getMotionProfileStatus(statusToFill);
		else if (side == Side.RIGHT)
			R1.getMotionProfileStatus(statusToFill);
	}

	/**
	 * push motion profiles to drive train talons
	 * 
	 * @since 4-22-2018
	 */
	public synchronized void motionProfileToTalons(double[][] mpL, double[][] mpR, Integer mpDur) {

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
	 * Used to process and push <b>parsed</b> motion profiles to drivetrain talons
	 * 
	 * @author max
	 * @since 4-22-2018
	 */
	public synchronized void motionProfileToTalons() {
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

	@SuppressWarnings("static-access")
	private synchronized TrajectoryDuration GetTrajectoryDuration(int durationMs) {
		TrajectoryDuration retval = TrajectoryDuration.Trajectory_Duration_0ms;
		retval = retval.valueOf(durationMs);

		if (retval.value != durationMs)
			System.out.println("ERROR Invalid trajectory duration: " + durationMs);

		return retval;
	}

	public synchronized void enableFollower() {
		for (GZSRX c : controllers) {
			switch (c.getSide()) {
			case LEFT:
				c.follow(L1);
				break;
			case RIGHT:
				c.follow(R1);
				break;
			case NO_INFO:
				System.out.println("ERROR Drive talon " + c.getName() + " could not enter follower mode!");
			}
		}
	}

	public synchronized void zeroEncoders() {
		L1.setSelectedSensorPosition(0, 0, 10);
		R1.setSelectedSensorPosition(0, 0, 10);
	}

	public synchronized void zeroSensors() {
		zeroEncoders();
		zeroGyro();
	}

	public synchronized void zeroGyro() {
		mGyro.reset();
	}

	public synchronized Double getPDPChannelCurrent(int channel) {
		return pdp.getCurrent(channel);
	}

	public synchronized Double getPDPTemperature() {
		return pdp.getTemperature();
	}

	public synchronized Double getPDPTotalCurrent() {
		return pdp.getTotalCurrent();
	}

	public synchronized Double getPDPVoltage() {
		return pdp.getVoltage();
	}

	public synchronized double getPercentageModify() {
		return percentageModify;
	}

	public synchronized void setPercentageModify(double percentageModify) {
		this.percentageModify = percentageModify;
	}

	public synchronized double getPercentageComplete() {
		return percentageComplete;
	}

	public synchronized void setPercentageComplete(double percentageComplete) {
		this.percentageComplete = percentageComplete;
	}

	public synchronized double getGyroAngle() {
		return mGyro.getAngle();
	}

	public synchronized double getGyroFusedHeading() {
		return mGyro.getFusedHeading();
	}


	private synchronized boolean currentStateIsNot(DriveState state) {
		return mState != state;
	}

	public String getStateString() {
		return mState.toString();
	}

	public DriveState getState() {
		return mState;
	}

	protected void initDefaultCommand() {
	}
}
