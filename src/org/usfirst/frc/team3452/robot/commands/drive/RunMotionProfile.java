package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author max
 * @since 4-22-2018
 */
public class RunMotionProfile extends Command {

	private MotionProfileStatus rStat = new MotionProfileStatus();
	private MotionProfileStatus lStat = new MotionProfileStatus();

	private SetValueMotionProfile set = SetValueMotionProfile.Disable;

	public RunMotionProfile() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		Robot.drive.parsedFileToTalons();
		setTimeout(12);
	}

	@Override
	protected void execute() {
		if (lStat.activePointValid && lStat.isLast && rStat.activePointValid && rStat.isLast)
			set = SetValueMotionProfile.Hold;
		else
			set = SetValueMotionProfile.Enable;

		Robot.drive.L1.set(ControlMode.MotionProfile, set.value);
		Robot.drive.R1.set(ControlMode.MotionProfile, set.value);
		
		Robot.drive.L1.getMotionProfileStatus(lStat);
		Robot.drive.R1.getMotionProfileStatus(rStat);
		
		System.out.println(rStat.timeDurMs + "\t\t" + rStat.isLast);
	}

	@Override
	protected boolean isFinished() {
//		return isTimedOut();
				return lStat.isLast || rStat.isLast || isTimedOut();
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
