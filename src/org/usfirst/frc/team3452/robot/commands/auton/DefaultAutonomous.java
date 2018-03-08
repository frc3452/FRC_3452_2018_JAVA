package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DefaultAutonomous extends CommandGroup {

	public DefaultAutonomous() {
		addParallel(new DriveTime(.45, 0, .5));
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

		addSequential(new DriveTime(0, 0, 4));
		addSequential(new ElevatorPosition(3));
		addSequential(new DriveTime(.25, 0, 5));
	}
}
