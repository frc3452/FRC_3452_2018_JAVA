package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTele extends Command {

	public DriveTele() {
		requires(Drivetrain.getInstance());
	}

	protected void initialize() {
	}

	protected void execute() {
		Drivetrain.getInstance().Arcade(OI.driverJoy);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Drivetrain.getInstance().Arcade(0,0);
	}

	protected void interrupted() {
		end();
	}
}
