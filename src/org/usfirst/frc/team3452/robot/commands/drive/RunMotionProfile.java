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
	private int bufferLooper = 0;

	private FILES m_file;
	
	public RunMotionProfile(FILES file) {
		requires(Robot.drive);
		
		m_file = file;
		
	}

	@Override
	protected void initialize() {
		Robot.drive.L1.getMotionProfileStatus(lStat);
		Robot.drive.R1.getMotionProfileStatus(rStat);

		Robot.drive.selectMotionProfile(m_file);

		while (bufferLooper < 50) {
			if (lStat.btmBufferCnt > 10 && rStat.btmBufferCnt > 10) {
				set = SetValueMotionProfile.Enable;
				bufferLooper = 51;
			} else {
				set = SetValueMotionProfile.Disable;
			}
			bufferLooper++;

			System.out.println("BUFFER BOY: " + bufferLooper);
		}
	}

	@Override
	protected void execute() {
		Robot.drive.L1.processMotionProfileBuffer();
		Robot.drive.R1.processMotionProfileBuffer();
		
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

		Robot.drive.L1.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
		Robot.drive.R1.set(ControlMode.MotionProfile, SetValueMotionProfile.Hold.value);
		
		Robot.drive.encoderDone();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
