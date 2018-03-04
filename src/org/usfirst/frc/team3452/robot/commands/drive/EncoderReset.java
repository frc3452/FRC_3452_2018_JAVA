package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class EncoderReset extends InstantCommand {

    public EncoderReset() {
        super();
        requires(Robot.drive);
    }
    protected void initialize() {
    	Robot.drive.EncoderReset();
    }

}
