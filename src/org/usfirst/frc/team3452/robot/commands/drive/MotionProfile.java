package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author max
 * @since 4-22-2018
 */
public class MotionProfile extends Command {

	private MotionProfileStatus R_status = new MotionProfileStatus();
	private MotionProfileStatus L_status = new MotionProfileStatus();

	public MotionProfile() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		Robot.drive.motionprofile();
		setTimeout(4);
	}

	@Override
	protected void execute() {
		for (int i = 0; i < 5; i++) {
			Robot.drive.L1.processMotionProfileBuffer();
			Robot.drive.R1.processMotionProfileBuffer();
		}
		
		Robot.drive.L1.set(ControlMode.MotionProfile, 2);
		Robot.drive.R1.set(ControlMode.MotionProfile, 2);

		Robot.drive.L1.getMotionProfileStatus(L_status);
		Robot.drive.R1.getMotionProfileStatus(R_status);
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut();
		//		return L_status.isLast && R_status.isLast;
	}

	@Override
	protected void end() {
		Robot.drive.encoderDone();
	}

	@Override
	protected void interrupted() {
	}
}
