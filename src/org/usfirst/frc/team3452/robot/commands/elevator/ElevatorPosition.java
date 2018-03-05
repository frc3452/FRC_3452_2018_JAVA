package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPosition extends Command {

	private double m_value;

	public ElevatorPosition(double value) {
		requires(Robot.elevator);

		m_value = value;
	}

	protected void initialize() {
		setTimeout(4);
	}

	protected void execute() {
		Robot.elevator.Encoder(m_value);
	}

	protected boolean isFinished() {
		return Robot.elevator.isDone(1.5) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.EncoderDone();
		System.out.println("Elevator position completed.");
	}

	protected void interrupted() {
		end();
	}
}
