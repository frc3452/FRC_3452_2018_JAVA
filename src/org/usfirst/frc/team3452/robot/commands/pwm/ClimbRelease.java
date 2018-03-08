package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbRelease extends Command {

	private boolean m_release;

	public ClimbRelease(boolean release) {
		requires(Robot.climber);

		m_release = release;
	}

	protected void initialize() {
		
	}

	protected void execute() {
		Robot.climber.release(m_release);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.climber.release(false);
	}

	protected void interrupted() {
		end();
	}
}
