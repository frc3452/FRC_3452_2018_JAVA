package org.usfirst.frc.team3452.robot.commands.drive;
//test
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveTime extends Command {
	double m_speed, m_rotate, m_time;

	public DriveTime(double speed, double rotate, double time) {
		requires(Drivetrain.getInstance());
		m_speed = speed;
		m_rotate = rotate;
		m_time = time;
	}

	protected void initialize() {
		setTimeout(m_time);
	}

	protected void execute() {
		Drivetrain.getInstance().Arcade(m_speed, m_rotate);
	}
	protected boolean isFinished() {
		return isTimedOut();
	}
	protected void end() {
		Drivetrain.getInstance().Arcade(0,0);
	}
	protected void interrupted() {
		end();
	}
}
