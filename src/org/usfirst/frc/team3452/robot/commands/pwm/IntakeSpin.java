package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeSpin extends Command {

	private double m_speed;
	private boolean m_clockwise;

	public IntakeSpin(double speed, boolean clockwise) {
		requires(Intake.getInstance());

		m_speed = speed;
		m_clockwise = clockwise;
	}

	protected void initialize() {
	}

	protected void execute() {
		Intake.Intake_L.set((m_speed * ((m_clockwise) ? -1 : 1)));
		Intake.Intake_R.set((m_speed * ((m_clockwise) ? 1 : -1)));
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Intake.getInstance().manual(0);
	}

	protected void interrupted() {
		end();
	}
}
