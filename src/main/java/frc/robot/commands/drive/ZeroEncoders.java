package frc.robot.commands.drive;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ZeroEncoders extends InstantCommand {

    public ZeroEncoders() {
        super();
        requires(Robot.drive);
    }

    // Called once when the command executes
    protected void initialize() {
    	Robot.drive.zeroEncoders();
    }

}
