package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import frc.robot.Robot;

public class SpeedModify extends InstantCommand {

    public SpeedModify() {
        super();
    }

    protected void initialize() {
        Robot.drive.slowSpeed(!Robot.drive.isSlow());
    }
}