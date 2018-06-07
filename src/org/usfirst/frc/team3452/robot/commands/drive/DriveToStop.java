package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class DriveToStop extends Command {
    private double m_speed;

    /**
     * @param speed
     * @author macco
     * @see Drivetrain
     */
    public DriveToStop(double speed) {
        requires(Robot.drive);

        m_speed = speed;
    }

    protected void initialize() {
        setTimeout(10);
    }

    protected void execute() {
        Robot.drive.arcade(m_speed, 0);
    }

    protected boolean isFinished() {
        return Robot.drive.encoderSpeedIsUnder(60) || isTimedOut();
    }

    protected void end() {
        Robot.drive.arcade(0, 0);
    }

    protected void interrupted() {
        end();
    }
}
