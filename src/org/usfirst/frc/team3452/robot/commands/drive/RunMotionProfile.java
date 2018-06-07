package org.usfirst.frc.team3452.robot.commands.drive;

import com.ctre.phoenix.motion.MotionProfileStatus;
import com.ctre.phoenix.motion.SetValueMotionProfile;
import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.motionprofiles.Path;

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
        //check if we are parsing or running a stored motion profile
        if (path_.mpDur() == 3452)
            Robot.drive.motionProfileToTalons();
        else
            Robot.drive.motionProfileToTalons(path_.mpL(), path_.mpR(), path_.mpDur());
    }

    @Override
    protected void execute() {

        if (lStat.btmBufferCnt > 5 && rStat.btmBufferCnt > 5) {
            Robot.drive.L1.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
            Robot.drive.R1.set(ControlMode.MotionProfile, SetValueMotionProfile.Enable.value);
        } else {
            Robot.drive.L1.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
            Robot.drive.R1.set(ControlMode.MotionProfile, SetValueMotionProfile.Disable.value);
        }

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
