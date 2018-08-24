package org.usfirst.frc.team3452.robot.commands.pwm;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.ElevatorState;

public class Climb extends Command {
	private double m_speed;

	private Timer timer = new Timer();

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
		timer.stop();
		timer.reset();
		timer.start();
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
		timer.stop();
		Robot.climber.stop();
		Robot.climber.addClimberCounter();
	}

	protected void interrupted() {
		end();
	}
}
