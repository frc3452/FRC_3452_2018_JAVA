package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.motionprofiles.Path;
import org.usfirst.frc.team3452.robot.subsystems.Drive.DriveState;
import org.usfirst.frc.team3452.robot.util.GZSRX.Side;

import com.ctre.phoenix.motion.MotionProfileStatus;

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

		if (lStat.btmBufferCnt > 5 && rStat.btmBufferCnt > 5)
			Robot.drive.setWantedState(DriveState.MOTION_PROFILE);
		else
			Robot.drive.stop();

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
		Robot.drive.stop();
		System.out.println("Motion Profile Complete");
	}

	@Override
	protected void interrupted() {
		end();
	}
}
