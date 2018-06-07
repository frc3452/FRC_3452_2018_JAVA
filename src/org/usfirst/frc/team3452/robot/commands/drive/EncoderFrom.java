package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EncoderFrom extends CommandGroup {

    /**
     * Encoder reset, then EncoderDrive
     *
     * @param left
     * @param right
     * @param leftspeed
     * @param rightspeed
     * @param topspeed
     * @author macco
     * @see EncoderReset
     * @see EncoderDrive
     */
    public EncoderFrom(double left, double right, double leftspeed, double rightspeed, double topspeed) {

        addSequential(new EncoderReset());
        addSequential(new EncoderDrive(left, right, leftspeed, rightspeed, topspeed));
    }
}
