package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LightsWait extends Command {

	private double m_timeout;

	public LightsWait(double timeout) {
		requires(Robot.lights);

		m_timeout = timeout;
	}

	protected void initialize() {
		setTimeout(m_timeout);
	}

	protected void execute() {
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
