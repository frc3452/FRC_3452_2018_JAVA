package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class EncoderReset extends InstantCommand {

    /**
     * Encoder reset
     * @author macco
     * @see Drivetrain
     */
    public EncoderReset() {
        super();
        requires(Robot.drive);
    }
    protected void initialize() {
    	Robot.drive.encoderReset();
    }

}
