package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorWhileDrive extends Command {

	private double m_value, m_percent;

	public ElevatorWhileDrive(double value, double atPercent) {
		requires(Robot.elevator);

		m_value = value;
		m_percent = atPercent;
	}

	protected void initialize() {
		setTimeout(15);
		Robot.elevator.m_pos = -3452;
	}

	protected void execute() {
		if (Robot.drive.p_pos > m_percent)
			Robot.elevator.Encoder(m_value);
	}

	protected boolean isFinished() {
		if (Robot.elevator.Elev_2.getSensorCollection().isRevLimitSwitchClosed() && m_value > 0)
			return true;

		if (Robot.elevator.Elev_2.getSensorCollection().isFwdLimitSwitchClosed() && m_value < 0)
			return true;

		return Robot.elevator.isDone(3.5) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.EncoderDone();
		System.out.println("Elevator position completed.");
	}

	protected void interrupted() {
		end();
	}
}
