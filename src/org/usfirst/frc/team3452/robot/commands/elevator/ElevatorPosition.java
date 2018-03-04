package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPosition extends Command {

	private double m_value;

	public ElevatorPosition(double value) {
		requires(Robot.elevator);

		m_value = value;
	}

	protected void initialize() {
		setTimeout(2);
	}

	protected void execute() {
		Robot.elevator.Encoder(m_value);
	}

	protected boolean isFinished() {
		return Robot.elevator.isDone(3) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.EncoderDone();
	}

	protected void interrupted() {
		end();
	}
}
