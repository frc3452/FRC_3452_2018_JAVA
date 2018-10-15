package frc.robot.commands.elevator;

import frc.robot.Constants.kElevator;
import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorWhileDrive extends Command {

	private double m_value, m_percent;

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
			Robot.elevator.setHeight(m_value);
		else
			Robot.elevator.stop();
	}

	@Override
	protected boolean isFinished() {
		if (Robot.elevator.getTopLimit() && m_value > 0)
			return true;

		if (Robot.elevator.getBottomLimit() && m_value < 0)
			return true;

		return Robot.elevator.isEncoderMovementDone(kElevator.CLOSED_COMPLETION) || isTimedOut();
	}

	@Override
	protected void end() {
		Robot.elevator.stop();
		System.out.println("Elevator position completed.");
	}

	@Override
	protected void interrupted() {
		end();
	}
}
