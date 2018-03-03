package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForGameData extends Command {

	public WaitForGameData() {
		requires(Drivetrain.getInstance());
	}

	protected void initialize() {
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return ((Lights.getInstance().gsm() != "NO") ? true : false);
	}

	protected void end() {
	}

	protected void interrupted() {
		end();
	}
}
