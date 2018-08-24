package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.motionprofiles.Path;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.DriveState;
import org.usfirst.frc.team3452.robot.util.GZSRX.Side;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;

import edu.wpi.first.wpilibj.command.Command;

/**
 * @author max
 * @since 4-22-2018
 */
public class RunMotionProfile extends Command {

	private MotionProfileStatus rStat = new MotionProfileStatus();
	private MotionProfileStatus lStat = new MotionProfileStatus();

	private Path path_ = null;

	public RunMotionProfile(Path path) {
		requires(Robot.drive);

		path_ = path;
	}

	@Override
	protected void initialize() {
		// check if we are parsing or running a stored motion profile
		if (path_.mpDur() == 3452)
			Robot.drive.motionProfileToTalons();
		else
			Robot.drive.motionProfileToTalons(path_.mpL(), path_.mpR(), path_.mpDur());
	}

	@Override
	protected void execute() {
		Robot.drive.setState(DriveState.MOTION_PROFILE);

		if (lStat.btmBufferCnt > 5 && rStat.btmBufferCnt > 5) {
			Robot.drive.mIO.left_desired_output = Robot.drive.mIO.right_desired_output = SetValueMotionProfile.Enable.value;
		} else {
			Robot.drive.mIO.left_desired_output = Robot.drive.mIO.right_desired_output = SetValueMotionProfile.Disable.value;

		}

		Robot.drive.getMotionProfileStatus(Side.LEFT, lStat);
		Robot.drive.getMotionProfileStatus(Side.RIGHT, rStat);
	}

	@Override
	protected boolean isFinished() {
		// return isTimedOut();
		return (lStat.activePointValid && lStat.isLast) || (rStat.activePointValid && rStat.isLast) || isTimedOut();
	}

	@Override
	protected void end() {
		System.out.println("Motion Profile Complete");
		Robot.drive.encoderDone();
		Robot.drive.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
