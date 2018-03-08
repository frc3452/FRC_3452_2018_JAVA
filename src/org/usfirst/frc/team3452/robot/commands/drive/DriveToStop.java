package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToStop extends Command {
	private double m_speed;

	public DriveToStop(double speed) {
		requires(Robot.drive);
	}

	protected void initialize() {
		setTimeout(10);
	}

	protected void execute() {
		Robot.drive.Arcade(m_speed, 0);
	}

	protected boolean isFinished() {
		return Robot.drive.encoderSpeedIsUnder(100) || isTimedOut();
	}

	protected void end() {
		Robot.drive.Arcade(0, 0);
	}

	protected void interrupted() {
		end();
	}
}
