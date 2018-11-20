package frc.robot.commands.auton;

import frc.robot.commands.drive.DriveTime;
import frc.robot.commands.elevator.ElevatorTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class DefaultAutonomous extends CommandGroup {

	public DefaultAutonomous() {
		addSequential(new DriveTime(0, 0, 1));
	}
}
