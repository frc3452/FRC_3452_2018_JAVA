package frc.robot.commands.pwm;

import frc.robot.Robot;
import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeSpin extends Command {

	private double m_speed;
	private boolean m_clockwise;

	/**
	 * @author macco
	 * @param speed
	 * @param clockwise
	 * @see Intake
	 */
	public IntakeSpin(double speed, boolean clockwise) {
		requires(Robot.intake);

		m_speed = speed;
		m_clockwise = clockwise;
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.intake.spin(m_speed, m_clockwise);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.intake.stop();
	}

	protected void interrupted() {
		end();
	}
}
