package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class ResetGyro extends Command {

	public ResetGyro() {
		requires(Drivetrain.getInstance());
	}

	protected void initialize() {
		setTimeout(0.1);
		Drivetrain.Gyro.reset();
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
