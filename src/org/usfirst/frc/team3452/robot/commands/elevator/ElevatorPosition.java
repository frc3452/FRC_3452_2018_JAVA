package org.usfirst.frc.team3452.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kElevator;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

public class ElevatorPosition extends Command {

	private double m_value;
//	private boolean l_rev = false, l_fwd = false;

	/**
	 * Encoder movement of elevator
	 * 
	 * @author macco
	 * @param value
	 * @see Elevator
	 */
	public ElevatorPosition(double value) {
		requires(Robot.elevator);

		m_value = value;
	}

	protected void initialize() {
		setTimeout(3);
	}

	protected void execute() {
		Robot.elevator.encoder(m_value);
	}

	protected boolean isFinished() {
		if (Robot.elevator.mIO.elevator_rev_lmt && m_value > 0)
			return true;

		if (Robot.elevator.mIO.elevator_fwd_lmt && m_value < 0)
			return true;

		return Robot.elevator.isDone(kElevator.CLOSED_COMPLETION) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.stop();
	}

	protected void interrupted() {
		end();
	}
}
