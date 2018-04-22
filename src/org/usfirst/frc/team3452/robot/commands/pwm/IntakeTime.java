package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeTime extends Command {

	private double m_speed, m_timeout;

	/**
	 * @author macco
	 * @param speed
	 * @param timeout
	 * @see Intake
	 */
	public IntakeTime(double speed, double timeout) {
		requires(Robot.intake);

		m_speed = speed;
		m_timeout = timeout;
	}

	protected void initialize() {
		setTimeout(m_timeout);
	}

	protected void execute() {
		Robot.intake.manual(m_speed);
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
