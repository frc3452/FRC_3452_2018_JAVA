package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double m_speed;

	public Climb(double speed) {
		requires(Robot.climber);

		m_speed = speed;
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.climber.control(m_speed);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.climber.control(0);
	}

	protected void interrupted() {
		end();
	}
}
