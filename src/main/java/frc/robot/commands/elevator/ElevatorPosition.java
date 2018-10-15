package frc.robot.commands.elevator;

import frc.robot.Constants.kElevator;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

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
		Robot.elevator.setRotations(m_value);
	}

	protected boolean isFinished() {
		return Robot.elevator.isEncoderMovementDone(kElevator.CLOSED_COMPLETION) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.stop();
	}

	protected void interrupted() {
		end();
	}
}
