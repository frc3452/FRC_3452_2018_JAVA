package org.usfirst.frc.team3452.robot.commands.pwm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Intake;

public class IntakeWhileDrive extends Command {

	private double m_value, m_percent, m_timeout;
	private boolean timeoutSet = false;

	public Timer timer = new Timer();

	/**
	 * @author macco
	 * @param value
	 * @param atPercent
	 * @param timeout
	 * @see Intake
	 */
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
		if (Robot.drive.getPercentageComplete() > m_percent) {
			Robot.intake.manual(m_value);

			if (timeoutSet == false) {
				System.out.println("timeout set");
				setTimeout(m_timeout + timer.get());
				timeoutSet = true;
			}

		} else {
			Robot.intake.manual(0);
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
