package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants.kDrivetrain;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTele;
import org.usfirst.frc.team3452.robot.util.GZJoystick;
import org.usfirst.frc.team3452.robot.util.GZSRX;
import org.usfirst.frc.team3452.robot.util.GZSRX.Breaker;
import org.usfirst.frc.team3452.robot.util.GZSRX.Master;
import org.usfirst.frc.team3452.robot.util.GZSRX.Side;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Units;
import org.usfirst.frc.team3452.robot.util.Util;

import com.ctre.phoenix.motion.TrajectoryPoint;
import com.ctre.phoenix.motion.TrajectoryPoint.TrajectoryDuration;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Drivetrain2 extends GZSubsystem {

	private DriveState mState = DriveState.NEUTRAL;
	private DriveState prevState = mState;

	// PDP
	private PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	// DRIVETRAIN
	private GZSRX L1, L2, L3, L4, R1, R2, R3, R4;

	// ROBOT DRIVE OBJECT
//	private DifferentialDrive robotDrive;

	// GYRO
	private AHRS mGyro;

	// init timer
	private Timer timer = new Timer();

	private double percentageModify = 1;
	private double percentageComplete;
	private double left_target = 0, right_target = 0;

	public Drivetrain2() {

		// init timer
		timer.stop();
		timer.reset();
		timer.start();

		L1 = new GZSRX(kDrivetrain.L1, Breaker.AMP_40, Side.LEFT, Master.MASTER);
		L2 = new GZSRX(kDrivetrain.L2, Breaker.AMP_40, Side.LEFT, Master.FOLLOWER);
		L3 = new GZSRX(kDrivetrain.L3, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);
		L4 = new GZSRX(kDrivetrain.L4, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);

		R1 = new GZSRX(kDrivetrain.R1, Breaker.AMP_40, Side.RIGHT, Master.MASTER);
		R2 = new GZSRX(kDrivetrain.R2, Breaker.AMP_40, Side.RIGHT, Master.FOLLOWER);
		R3 = new GZSRX(kDrivetrain.R3, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);
		R4 = new GZSRX(kDrivetrain.R4, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);

		mGyro = new AHRS(SPI.Port.kMXP);

		brake(NeutralMode.Coast);

		// TODO 3) B - FINISH CONSTRUCTION

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

	public synchronized void onStateStart(DriveState newState) {
		switch (newState) {
		case MOTION_MAGIC:
			break;
		case MOTION_PROFILE:
			break;
		case NEUTRAL:
			break;
		case OPEN_LOOP:
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
			OI.rumble(CONTROLLER.BOTH, 0);
			break;
		default:
			break;
		}
	}

	@Override
	public synchronized void loop() {
		switch (mState) {
		case MOTION_MAGIC:

			Values.control_mode = ControlMode.MotionMagic;
			Values.left_output = Values.left_desired_output;
			Values.right_output = Values.right_desired_output;

			break;
		case MOTION_PROFILE:

			Values.control_mode = ControlMode.MotionProfile;
			Values.left_output = Values.left_desired_output;
			Values.right_output = Values.right_desired_output;

			break;
		case NEUTRAL:

			Values.control_mode = ControlMode.Disabled;
			Values.left_output = 0;
			Values.right_output = 0;

			break;
		case OPEN_LOOP:

			Values.control_mode = ControlMode.PercentOutput;
			Values.left_output = Values.left_desired_output;
			Values.right_output = Values.right_desired_output;

			break;
		case DEMO:

			Values.control_mode = ControlMode.PercentOutput;
			Values.left_output = Values.left_desired_output * .5;
			Values.right_output = Values.right_desired_output * .5;

			break;

		default:
			System.out.println("WARNING: Incorrect drive state " + mState + " reached.");
			break;
		}
	}

	public static class Values {
		// in
		static double left_encoder_ticks = -1;
		static double left_encoder_rotations = -1;
		static double left_encoder_vel = -1;

		static double right_encoder_ticks = -1;
		static double right_encoder_rotations = -1;
		static double right_encoder_vel = -1;

		static double L1_amp = -1;
		static double L2_amp = -1;
		static double L3_amp = -1;
		static double L4_amp = -1;
		static double R1_amp = -1;
		static double R2_amp = -1;
		static double R3_amp = -1;
		static double R4_amp = -1;

		// out
		static private double left_output = 0;
		static double left_desired_output = 0;

		static private double right_output = 0;
		static double right_desired_output = 0;
		static ControlMode control_mode = ControlMode.PercentOutput;
	}

	@Override
	protected synchronized void in() {
		Values.left_encoder_ticks = L1.getSelectedSensorPosition(0);
		Values.left_encoder_rotations = Units.ticks_to_rotations(Values.left_encoder_ticks);
		Values.left_encoder_vel = L1.getSelectedSensorVelocity(0);

		Values.right_encoder_ticks = -R1.getSelectedSensorPosition(0);
		Values.right_encoder_rotations = -Units.ticks_to_rotations(Values.right_encoder_ticks);
		Values.right_encoder_vel = -R1.getSelectedSensorVelocity(0);

		Values.L1_amp = L1.getOutputCurrent();
		Values.L2_amp = L2.getOutputCurrent();
		Values.L3_amp = L3.getOutputCurrent();
		Values.L4_amp = L4.getOutputCurrent();
		Values.R1_amp = R1.getOutputCurrent();
		Values.R2_amp = R2.getOutputCurrent();
		Values.R3_amp = R3.getOutputCurrent();
		Values.R4_amp = R4.getOutputCurrent();
	}

	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putNumber("NavX Angle", mGyro.getAngle());

		SmartDashboard.putNumber("L1", Values.left_encoder_rotations);
		SmartDashboard.putNumber("R1", Values.right_encoder_rotations);

		SmartDashboard.putNumber("L1 Vel", Values.left_encoder_vel);
		SmartDashboard.putNumber("R1 Vel", Values.right_encoder_vel);

		SmartDashboard.putNumber("PercentageCompleted", getPercentageComplete());
	}

	public synchronized void arcade(GZJoystick joy) {
		setState(DriveState.OPEN_LOOP);

		arcade(joy.getLeftAnalogY() * percentageModify,
				(joy.getRightTrigger() - joy.getLeftTrigger()) * .8 * percentageModify);
	}

	public synchronized void alternateArcade(GZJoystick joy) {
		setState(DriveState.DEMO);

		arcade(joy.getLeftAnalogY(), (joy.getRightAnalogX() * .85));
	}

	public synchronized void arcade(double move, double rotate) {
		setState(DriveState.OPEN_LOOP);

		double[] temp = arcadeToLR(move * Robot.elevator2.getPercentageModify(),
				rotate * (Robot.elevator2.getPercentageModify() + .2));

		Values.left_desired_output = temp[0];
		Values.right_desired_output = temp[1];
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
		setState(DriveState.OPEN_LOOP);

		// TODO STATE TESTING - CHECK SIGN
		Values.left_desired_output = Util.applyDeadband(left * Robot.elevator2.getPercentageModify(),
				kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);
		Values.right_desired_output = -Util.applyDeadband(right * Robot.elevator2.getPercentageModify(),
				kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);
	}

	public synchronized void tank(GZJoystick joy) {
		setState(DriveState.OPEN_LOOP);

		Values.left_desired_output = Util.applyDeadband(joy.getLeftAnalogY() * Robot.elevator2.getPercentageModify(),
				kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);
		// TODO STATE TESTING -CHECK SIGN
		Values.right_desired_output = -Util.applyDeadband(joy.getRightAnalogY() * Robot.elevator2.getPercentageModify(),
				kDrivetrain.DIFFERENTIAL_DRIVE_DEADBAND);
	}

	public synchronized void brake(NeutralMode mode) {
		L1.setNeutralMode(mode);
		L2.setNeutralMode(mode);
		L3.setNeutralMode(mode);
		L4.setNeutralMode(mode);
		R1.setNeutralMode(mode);
		R2.setNeutralMode(mode);
		R3.setNeutralMode(mode);
		R4.setNeutralMode(mode);
	}

	public synchronized void motionMagic(double leftRotations, double rightRotations, double leftAccel,
			double rightAccel, double leftSpeed, double rightSpeed) {
		setState(DriveState.MOTION_MAGIC);

		double topspeed = 3941;

		left_target = Units.rotations_to_ticks(leftRotations);
		// = leftRotations * 4096 * 1;
		right_target = Units.rotations_to_ticks(rightRotations);

		percentageComplete = Math.abs(
				((Values.left_encoder_rotations / left_target) + (Values.right_encoder_rotations / right_target)) / 2);

		L1.configMotionAcceleration((int) (topspeed * leftAccel), 10);
		R1.configMotionAcceleration((int) (topspeed * rightAccel), 10);

		L1.configMotionCruiseVelocity((int) (topspeed * leftSpeed), 10);
		R1.configMotionCruiseVelocity((int) (topspeed * rightSpeed), 10);

		Values.control_mode = ControlMode.MotionMagic;
		Values.left_desired_output = left_target;
		Values.right_desired_output = right_target;
	}

	@Deprecated
	public synchronized void encoder(double leftRotations, double rightRotations, double leftPercentageLimit,
			double rightPercentageLimit) {
		L1.configPeakOutputForward(leftPercentageLimit, 10);
		L1.configPeakOutputReverse(-leftPercentageLimit, 10);
		R1.configPeakOutputForward(rightPercentageLimit, 10);
		R1.configPeakOutputReverse(-rightPercentageLimit, 10);

		left_target = Units.rotations_to_ticks(leftRotations);
		right_target = -Units.rotations_to_ticks(rightRotations);

		percentageComplete = Math.abs(
				((Values.left_encoder_rotations / left_target) + (Values.right_encoder_rotations / right_target)) / 2);

		Values.control_mode = ControlMode.Position;
		Values.left_desired_output = left_target;
		Values.right_desired_output = right_target;
	}

	public synchronized boolean encoderSpeedIsUnder(double ticksPer100Ms) {
		double l = Math.abs(Values.left_encoder_vel);
		double r = Math.abs(Values.right_encoder_vel);

		return l < ticksPer100Ms && r < ticksPer100Ms;
	}

	public synchronized void encoderDone() {
		Values.control_mode = ControlMode.PercentOutput;
		Values.left_desired_output = Values.right_desired_output = 0;

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
		return (Values.left_encoder_ticks < left_target + 102 * multiplier)
				&& (Values.left_encoder_ticks > left_target - 102 * multiplier)
				&& (Values.right_encoder_ticks < right_target + 102 * multiplier)
				&& (Values.right_encoder_ticks > right_target - 102 * multiplier);
	}

	public synchronized boolean encoderIsDoneEither(double multiplier) {
		return (Values.left_encoder_ticks < left_target + 102 * multiplier
				&& Values.left_encoder_ticks > left_target - 102 * multiplier)
				|| (Values.right_encoder_ticks < right_target + 102 * multiplier
						&& Values.right_encoder_ticks > right_target - 102 * multiplier);
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

	public synchronized double getPDPChannelCurrent(int channel) {
		return pdp.getCurrent(channel);
	}

	public synchronized double getPDPTemperature() {
		return pdp.getTemperature();
	}

	public synchronized double getPDPTotalCurrent() {
		return pdp.getTotalCurrent();
	}

	public synchronized double getPDPTotalEnergy() {
		return pdp.getTotalEnergy();
	}

	public synchronized double getPDPTotalPower() {
		return pdp.getTotalPower();
	}

	public synchronized double getPDPVoltage() {
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

	public synchronized Timer getTimer() {
		return timer;
	}

	public synchronized AHRS getGyro() {
		return mGyro;
	}

	@Override
	protected synchronized void out() {
		L1.set(Values.control_mode, Values.left_output);
		R1.set(Values.control_mode, Values.right_output);
	}

	public enum DriveState {
		OPEN_LOOP, DEMO, NEUTRAL, MOTION_MAGIC, MOTION_PROFILE,
	}

	@Override
	public synchronized void stop() {
		setState(DriveState.NEUTRAL);
	}

	public synchronized void setState(DriveState wantedState) {

		if (this.isDisabed()) {

			onStateExit(mState);
			mState = DriveState.NEUTRAL;
			onStateStart(mState);

		} else if (Robot.autonSelector.isDemo()) {

			onStateExit(mState);
			mState = DriveState.DEMO;
			onStateStart(mState);

		} else if (mState != wantedState) {

			onStateExit(mState);
			mState = wantedState;
			onStateStart(mState);
		}
	}

	public synchronized void checkPrevState()
	{
		if (mState != prevState)
		{
			onStateExit(prevState);
			onStateStart(mState);
		}
		prevState = mState;
	}
	
	public String getStateString() {
		return mState.toString();
	}

	public DriveState getState() {
		return mState;
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new DriveTele());
	}
}
