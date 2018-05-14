package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTele extends Command {

	/**
	 * Drive during teleop
	 * @author macco
	 * @see Drivetrain
	 */
	public DriveTele() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		
	}

	@Override
	protected void execute() {
		Robot.drive.arcade(OI.driverJoy);
//		Robot.drive.Tank(OI.driverJoy);
		if (Robot.elevator.m_overriden)
			OI.rumble(CONTROLLER.DRIVER, .45);
		else
			OI.rumble(CONTROLLER.DRIVER, 0);
		
		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		OI.rumble(CONTROLLER.DRIVER, 0);
		Robot.drive.arcade(0,0);
		
	}

	@Override
	protected void interrupted() {
		end();
	}
}
