package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPosition extends Command {

	private double m_value;

	public ElevatorPosition(double value) {
		requires(Elevator.getInstance());

		m_value = value;
	}

	protected void initialize() {
		setTimeout(2);
	}

	protected void execute() {
		Elevator.getInstance().Encoder(m_value);
	}

	protected boolean isFinished() {
		return Elevator.getInstance().isDone(3) || isTimedOut();
	}

	protected void end() {
		Elevator.getInstance().EncoderDone();
	}

	protected void interrupted() {
		end();
	}
}
