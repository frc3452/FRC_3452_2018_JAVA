package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorPosition extends Command {

	private double m_value;

	public ElevatorPosition(double value) {
		requires(Robot.elevator);

		m_value = value;
	}

	protected void initialize() {
		setTimeout(3);
	}

	protected void execute() {
		Robot.elevator.Encoder(m_value);
	}

	protected boolean isFinished() {
		if(Robot.elevator.Elev_1.getSensorCollection().isRevLimitSwitchClosed() && m_value > 10)
			return true;
		
		if (Robot.elevator.Elev_1.getSensorCollection().isFwdLimitSwitchClosed() && m_value < 0)
			return true;
		
		return Robot.elevator.isDone(3.5) || isTimedOut();
	}

	protected void end() {
		Robot.elevator.EncoderDone();
		System.out.println("Elevator position completed: " + DriverStation.getInstance().getMatchTime());
	}

	protected void interrupted() {
		end();
	}
}
