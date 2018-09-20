package frc.robot.commands.elevator;

import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorTime extends Command {

	private double m_speed, m_time;

	/**
	 * @author macco
	 * @param speed positive = up
	 * @param time
	 * @see Elevator
	 */

	public ElevatorTime(double speed, double time) {
		requires(Robot.elevator);

		m_speed = speed;
		m_time = time;
	}

	protected void initialize() {
		setTimeout(m_time);
	}

	protected void execute() {
		Robot.elevator.manual(m_speed);

	}

	protected boolean isFinished() {

		if (Robot.elevator.getTopLimit() && m_speed > 0)
			return true;

		if (Robot.elevator.getBottomLimit() && m_speed < 0)
			return true;

		return isTimedOut();
	}

	protected void end() {
		Robot.elevator.stop();
	}

	protected void interrupted() {
		end();
	}
}
