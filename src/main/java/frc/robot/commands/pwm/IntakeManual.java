package frc.robot.commands.pwm;

import frc.robot.Robot;
import frc.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeManual extends Command {
	private double m_speed;

	/**
	 * @author macco
	 * @param speed
	 * @see Intake
	 */
	public IntakeManual(double speed) {
		requires(Robot.intake);
		
		m_speed = speed;
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.intake.manual(m_speed);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.intake.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}