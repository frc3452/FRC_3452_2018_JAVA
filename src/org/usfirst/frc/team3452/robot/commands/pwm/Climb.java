package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double m_speed;

	private Timer timer = new Timer();

	public Climb(double speed) {
		requires(Robot.climber);

		m_speed = speed;
	}

	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();
	}

	protected void execute() {
		if (timer.get() > .7)
			Robot.climber.control(m_speed);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		timer.stop();
		Robot.climber.control(0);
	}

	protected void interrupted() {
		end();
	}
}
