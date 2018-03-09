package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class WaitForGameData extends Command {

	public WaitForGameData() {
		requires(Robot.drive);
	}

	protected void initialize() {
		setTimeout(3);
	}

	protected void execute() {
		if (Robot.lights.gsm() == "NOT")
			System.out.println("ERROR WAITING FOR GAME DATA");
	}

	protected boolean isFinished() {
		return (Robot.lights.gsm() != "NOT" || isTimedOut());
	}

	protected void end() {
		
	}

	protected void interrupted() {
		end();
	}
}
