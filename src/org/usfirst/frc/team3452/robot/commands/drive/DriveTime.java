package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.DriveState;

public class DriveTime extends Command {
	private double m_speed, m_rotate, m_time;

	/**
	 * @author macco
	 * @param speed
	 * @param rotate
	 * @param time
	 * @see Drivetrain
	 */
	public DriveTime(double speed, double rotate, double time) {
		requires(Robot.drive);
		m_speed = speed;
		m_rotate = rotate;
		m_time = time;
	}

	protected void initialize() {
		setTimeout(m_time);
		Robot.drive.setState(DriveState.OPEN_LOOP);
	}

	protected void execute() {
		Robot.drive.arcade(m_speed, m_rotate);
	}
	protected boolean isFinished() {
		return isTimedOut();
	}
	protected void end() {
		Robot.drive.arcade(0,0);
	}
	protected void interrupted() {
		end();
	}
}
