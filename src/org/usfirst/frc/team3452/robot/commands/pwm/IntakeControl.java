package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeControl extends Command {

	private double m_speed, m_timeout;

	public IntakeControl(double speed, double timeout) {
		requires(Intake.getInstance());

		m_speed = speed;
		m_timeout = timeout;
	}

	protected void initialize() {
		setTimeout(m_timeout);
	}

	protected void execute() {
		Intake.getInstance().manual(m_speed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
		Intake.getInstance().manual(0);
	}

	protected void interrupted() {
		end();
	}
}
