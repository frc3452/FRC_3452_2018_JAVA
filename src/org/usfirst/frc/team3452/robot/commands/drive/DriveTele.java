package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTele extends Command {

	public DriveTele() {
		requires(Robot.drive);
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.drive.Arcade(OI.driverJoy);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drive.Arcade(0,0);
	}

	protected void interrupted() {
		end();
	}
}
