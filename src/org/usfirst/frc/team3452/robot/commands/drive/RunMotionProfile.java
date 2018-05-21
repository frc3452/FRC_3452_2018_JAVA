package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Utilities.FILES;

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

	private FILES m_file;

	public RunMotionProfile(FILES file) {
		requires(Robot.drive);

		m_file = file;
	}

	@Override
	protected void initialize() {
		Robot.drive.selectMotionProfile(m_file);
	}

	@Override
	protected void execute() {
		if (lStat.btmBufferCnt > 5 && rStat.btmBufferCnt > 5) {
			set = SetValueMotionProfile.Enable;
		}
		
		Robot.drive.L1.set(ControlMode.MotionProfile, set.value);
		Robot.drive.R1.set(ControlMode.MotionProfile, set.value);

		Robot.drive.L1.getMotionProfileStatus(lStat);
		Robot.drive.R1.getMotionProfileStatus(rStat);
	}

	@Override
	protected boolean isFinished() {
		//		return isTimedOut();
		return (lStat.activePointValid && lStat.isLast) || (rStat.activePointValid && rStat.isLast) || isTimedOut();
	}

	@Override
	protected void end() {
		System.out.println("Motion Profile Complete");
 		Robot.drive.encoderDone();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
