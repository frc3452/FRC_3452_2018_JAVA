package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeWhileDrive extends Command {

	private double m_value, m_percent, m_timeout;

	public IntakeWhileDrive(double value, double atPercent, double timeout) {
		requires(Robot.intake);

		m_value = value;
		m_percent = atPercent;
		m_timeout = timeout;
	}

	protected void initialize() {
	}

	protected void execute() {
		if (Robot.drive.p_pos > m_percent) {

			Robot.intake.manual(m_value);
			setTimeout(m_timeout);

		}
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
		Robot.intake.manual(0);
	}

	protected void interrupted() {
		end();
	}
}
