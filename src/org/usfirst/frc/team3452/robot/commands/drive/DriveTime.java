package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTime extends Command {
	private double m_speed, m_rotate, m_time;

	/**
	 * @author macco
	 * @param speed
	 * @param rotate
	 * @param time
	 * @see Drive
	 */
	public DriveTime(double speed, double rotate, double time) {
		requires(Robot.drive);
		m_speed = speed;
		m_rotate = rotate;
		m_time = time;
	}

	protected void initialize() {
		setTimeout(m_time);
	}

	protected void execute() {
		Robot.drive.arcade(m_speed, m_rotate);
	}
	protected boolean isFinished() {
		return isTimedOut();
	}
	protected void end() {
		Robot.drive.stop();
	}
	protected void interrupted() {
		end();
	}
}
