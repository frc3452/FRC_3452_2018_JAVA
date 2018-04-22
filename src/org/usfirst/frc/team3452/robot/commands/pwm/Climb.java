package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

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
		//WAIT ON FIRST TWO PRESSES
		if (Robot.climber.climbCounter < 2) {
			if (timer.get() > .7)
				Robot.climber.control(m_speed * ((timer.get() - .7) * 1.5));
		} else {   
			Robot.climber.control(m_speed /* * (timer.get() * 1.5)*/);
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		timer.stop();
		Robot.climber.control(0);
		Robot.climber.climbCounter++;
	}

	protected void interrupted() {
		end();
	}
}
