package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbRelease extends Command {

	private double m_release, m_timeout;

	public ClimbRelease(double release, double timeout) {
		requires(Robot.climber);

		m_release = release;
		m_timeout = timeout;
	}

	protected void initialize() {
		setTimeout(m_timeout);
	}

	protected void execute() {
		Robot.climber.release(m_release);
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
		Robot.climber.release(0);
	}

	protected void interrupted() {
		end();
	}
}
