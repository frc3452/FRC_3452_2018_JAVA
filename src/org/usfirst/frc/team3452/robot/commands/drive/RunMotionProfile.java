package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author max
 * @since 4-22-2018
 */
public class RunMotionProfile extends Command {

	private MotionProfileStatus R_status = new MotionProfileStatus();
	private MotionProfileStatus L_status = new MotionProfileStatus();
	
	public RunMotionProfile() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		Robot.drive.motionprofile();
		setTimeout(12);
	}

	@Override
	protected void execute() {
		Robot.drive.L1.processMotionProfileBuffer();
		Robot.drive.R1.processMotionProfileBuffer();

		Robot.drive.L1.set(ControlMode.MotionProfile, 1);
		Robot.drive.R1.set(ControlMode.MotionProfile, 1);

		Robot.drive.L1.getMotionProfileStatus(L_status);
		Robot.drive.R1.getMotionProfileStatus(R_status);
		System.out.println(L_status.timeDurMs + "\t\t" + R_status.isLast);
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut();
		//		return L_status.isLast || R_status.isLast || isTimedOut();
	}

	@Override
	protected void end() {
		System.out.println("Motion Profile Complete");
		Robot.drive.encoderDone();
	}

	@Override
	protected void interrupted() {
	}
}
