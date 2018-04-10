package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class IntakeWhileDrive extends Command {

	private double m_value, m_percent, m_timeout;
	private boolean timeoutSet = false;

	public Timer timer = new Timer();

	public IntakeWhileDrive(double value, double atPercent, double timeout) {
		requires(Robot.intake);

		m_value = value;
		m_percent = atPercent;
		m_timeout = timeout;
	}

	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();
	}

	protected void execute() {
		System.out.println(Robot.drive.p_pos + "\t\t" + m_percent);

		if (Robot.drive.p_pos > m_percent) {

			System.out.println("greater value");
			Robot.intake.manual(m_value);

			if (timeoutSet == false) {
				System.out.println("timeout set");
				setTimeout(m_timeout + timer.get());
				timeoutSet = true;
			}

		}
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
		System.out.println("Done");
		Robot.intake.manual(0);
	}

	protected void interrupted() {
		end();
	}
}
