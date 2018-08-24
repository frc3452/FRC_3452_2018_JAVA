package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorWhileDrive extends Command {

	private double m_value, m_percent;
	private boolean l_rev = false, l_fwd = false;

	/**
	 * @author macco
	 * @param value
	 * @param atPercent
	 * @see Elevator
	 */
	public ElevatorWhileDrive(double value, double atPercent) {
		requires(Robot.elevator);

		m_value = value;
		m_percent = atPercent;
	}

	@Override
	protected void initialize() {
		setTimeout(15);
		Robot.elevator.setTarget(-3452);
	}

	@Override
	protected void execute() {
		if (Robot.drive.getPercentageComplete() > m_percent)
			Robot.elevator.encoder(m_value);
		else
			Robot.elevator.stop();
	}

	@Override
	protected boolean isFinished() {
		if (Robot.elevator.mIO.elevator_rev_lmt && m_value > 0)
			return true;

		if (Robot.elevator.mIO.elevator_fwd_lmt && m_value < 0)
			return true;

		return Robot.elevator.isDone(kElevator.CLOSED_COMPLETION) || isTimedOut();
	}

	@Override
	protected void end() {
		Robot.elevator.encoderDone();
		Robot.elevator.stop();
		System.out.println("Elevator position completed.");
	}

	@Override
	protected void interrupted() {
		end();
	}
}
