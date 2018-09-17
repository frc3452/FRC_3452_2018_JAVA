package frc.robot.commands.drive;

import frc.robot.Robot;
import frc.robot.subsystems.Drive;

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