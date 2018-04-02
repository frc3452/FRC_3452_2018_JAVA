package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorWhileDrive extends Command {

	private double m_value, m_percent;
	private boolean m_side;

	public ElevatorWhileDrive(double value, double atPercent, boolean side) {
		requires(Robot.elevator);

		m_value = value;
		m_percent = atPercent;
		m_side = side;
	}

	protected void initialize() {
		setTimeout(15);
		Robot.elevator.m_pos = -3452;
	}

	protected void execute() {
		if ((m_side ? (Robot.drive.lp_pos) : (Robot.drive.rp_pos)) > m_percent)
			Robot.elevator.Encoder(m_value);
	}

	protected boolean isFinished() {
		if (Robot.elevator.Elev_1.getSensorCollection().isRevLimitSwitchClosed() && m_value > 10)
			return true;

		if (Robot.elevator.Elev_1.getSensorCollection().isFwdLimitSwitchClosed() && m_value < 0)
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
