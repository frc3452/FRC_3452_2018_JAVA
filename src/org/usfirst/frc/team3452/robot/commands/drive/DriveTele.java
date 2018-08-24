package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class DriveTele extends Command {

	/**
	 * Drive during teleop
	 * 
	 * @author macco
	 * @see Drivetrain
	 */
	public DriveTele() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		if (Robot.autonSelector.isDemo())
			Robot.drive.m_modify = .5;
		else
			Robot.drive.m_modify = 1;
	}

	@Override
	protected void execute() {
		if (!Robot.autonSelector.isDemo())
			Robot.drive.arcade(OI.driverJoy);
		else 
			Robot.drive.alternateArcade(OI.driverJoy);
		
		// Robot.drive.tank(OI.driverJoy);
		
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
		OI.rumble(CONTROLLER.BOTH, 0);
		Robot.drive.arcade(0, 0);

	}

	@Override
	protected void interrupted() {
		end();
	}
}
