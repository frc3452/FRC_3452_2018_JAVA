package frc.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.motionprofiles.Path;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Drive.DriveState;
import frc.robot.util.GZSRX.Side;

/**
 * @author max
 * @since 4-22-2018
 */
public class RunMotionProfile extends Command {

	private MotionProfileStatus rStat = new MotionProfileStatus();
	private MotionProfileStatus lStat = new MotionProfileStatus();

	private Path path_ = null;

	private Drive drive = Drive.getInstance();

	public RunMotionProfile(Path path) {
		requires(drive);

		path_ = path;
	}

	@Override
	protected void initialize() {
		// check if we are parsing or running a stored motion profile
		if (path_.mpDur() == 3452)
			drive.motionProfileToTalons();
		else
			drive.motionProfileToTalons(path_.mpL(), path_.mpR(), path_.mpDur());
	}

	@Override
	protected void execute() {

		if (lStat.btmBufferCnt > 5 && rStat.btmBufferCnt > 5)
			drive.setWantedState(DriveState.MOTION_PROFILE);
		else
			drive.stop();

		drive.getMotionProfileStatus(Side.LEFT, lStat);
		drive.getMotionProfileStatus(Side.RIGHT, rStat);
	}

	@Override
	protected boolean isFinished() {
		// return isTimedOut();
		return (lStat.activePointValid && lStat.isLast) || (rStat.activePointValid && rStat.isLast) || isTimedOut();
	}

	@Override
	protected void end() {
		drive.stop();
		System.out.println("Motion Profile Complete");
	}

	@Override
	protected void interrupted() {
		end();
	}
}
