package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.drive.SmoothMooth;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class Test extends CommandGroup {

	public Test() {
		addSequential(new EncoderReset());
		addSequential(new GyroReset());
		addSequential(new DriveTime(0,0,.5));
		
		addParallel(new ElevatorWhileDrive(3.5, .9, true));
//		addSequential(new SmoothMooth(10, 12.1, .25, .05, .4, 0, .017));
	}
}
