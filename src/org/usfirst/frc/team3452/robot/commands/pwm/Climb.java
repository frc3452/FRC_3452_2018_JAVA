package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.ElevatorState;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double m_speed;

	/**
	 * @author macco
	 * @param speed
	 * @see Climber
	 */
	public Climb(double speed) {
		requires(Robot.climber);

		m_speed = speed;
	}

	protected void initialize() {
	}

	protected void execute() {
		if (Robot.elevator.getState() != ElevatorState.DEMO) {
			// WAIT ON FIRST TWO PRESSES
			if (Robot.climber.getClimbCounter() < 2) {
			} else
				Robot.climber.manual(m_speed);
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.climber.stop();
		Robot.climber.addClimberCounter();
	}

	protected void interrupted() {
		end();
	}
}
