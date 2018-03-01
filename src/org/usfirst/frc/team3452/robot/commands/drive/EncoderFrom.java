package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class EncoderFrom extends CommandGroup {

	public EncoderFrom(double left, double right, double leftspeed, double rightspeed, double topspeed) {
		Drivetrain.L1.setSelectedSensorPosition(0, 0, 10);
		Drivetrain.R1.setSelectedSensorPosition(0, 0, 10);

		addSequential(new EncoderDrive(left, right, leftspeed, rightspeed, topspeed));
	}
}
