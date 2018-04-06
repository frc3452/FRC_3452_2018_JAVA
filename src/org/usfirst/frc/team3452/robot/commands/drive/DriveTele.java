package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.CONTROLLER;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTele extends Command {

	public DriveTele() {
		requires(Robot.drive);
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.drive.Arcade(OI.driverJoy);
//		Robot.drive.Tank(OI.driverJoy);
		if (Robot.elevator.m_overriden)
			OI.rumble(CONTROLLER.DRIVER, .45);
		else
			OI.rumble(CONTROLLER.DRIVER, 0);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		OI.rumble(CONTROLLER.DRIVER, 0);
		Robot.drive.Arcade(0,0);
	}

	protected void interrupted() {
		end();
	}
}
