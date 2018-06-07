package team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import team3452.robot.Robot;
import team3452.robot.subsystems.Drivetrain;

public class DriveTime extends Command {
    private double m_speed, m_rotate, m_time;

    /**
     * @param speed
     * @param rotate
     * @param time
     * @author macco
     * @see Drivetrain
     */
    public DriveTime(double speed, double rotate, double time) {
        requires(Robot.drive);
        m_speed = speed;
        m_rotate = rotate;
        m_time = time;
    }

    protected void initialize() {
        setTimeout(m_time);
    }

    protected void execute() {
        Robot.drive.arcade(m_speed, m_rotate);
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
        Robot.drive.arcade(0, 0);
    }

    protected void interrupted() {
        end();
    }
}
