package org.usfirst.frc.team3452.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

public class ElevatorPosition extends Command {

	private double m_value;
	private boolean l_rev = false, l_fwd = false;

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

		if (Robot.elevator.isRemoteSensor) {
			l_rev = Robot.elevator.Elev_2.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_2.getSensorCollection().isFwdLimitSwitchClosed();
		} else {
			l_rev = Robot.elevator.Elev_1.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_1.getSensorCollection().isFwdLimitSwitchClosed();
		}
	}

	protected boolean isFinished() {
		if (l_rev && m_value > 0)
			return true;

		if (l_fwd && m_value < 0)
			return true;

		return Robot.elevator.isDone(3.5) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.encoderDone();
	}

	protected void interrupted() {
		end();
	}
}
