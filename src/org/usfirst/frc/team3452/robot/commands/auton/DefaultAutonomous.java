package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DefaultAutonomous extends CommandGroup {

	/**
	 * Lift elevator and drivetime
	 * 
	 * @author macco
	 */
	public DefaultAutonomous() {
		addSequential(new ElevatorTime(.5, .15));

		addSequential(new DriveTime(0, 0, 1));
		addSequential(new ElevatorTime(.5, .7));
		addSequential(new DriveTime(.4, 0, 5));
	}
}
