package frc.robot.commands.elevator;

import frc.robot.Constants.kElevator;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPosition extends Command {

	private double m_value;
//	private boolean l_rev = false, l_fwd = false;

private Elevator elevator = Elevator.getInstance();

	/**
	 * Encoder movement of elevator
	 * 
	 * @author macco
	 * @param value
	 * @see Elevator
	 */
	public ElevatorPosition(double value) {
		requires(elevator);

		m_value = value;
	}

	protected void initialize() {
		setTimeout(3);
	}

	protected void execute() {
		elevator.setHeight(m_value);
	}

	protected boolean isFinished() {
		return elevator.isEncoderMovementDone(kElevator.CLOSED_COMPLETION) || isTimedOut();
	}

	protected void end() {
		elevator.stop();
	}

	protected void interrupted() {
		end();
	}
}
