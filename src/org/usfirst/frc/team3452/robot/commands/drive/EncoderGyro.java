package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderGyro extends Command {

	private double l_pos, r_pos, l_accel, r_accel, m_speed, c_gyro, i_gyro;

	public EncoderGyro(double leftpos, double rightpos, double leftaccel, double rightaccel, double speed) {
		requires(Robot.drive);

		l_pos = leftpos;
		r_pos = rightpos;
		l_accel = leftaccel;
		r_accel = rightaccel;
		m_speed = speed;
		i_gyro = 0;
		c_gyro = 0;
	}

	protected void initialize() {
		setTimeout(15);
		i_gyro = Robot.drive.Gyro.getAngle();
	}

	protected void execute() {
		c_gyro = Robot.drive.Gyro.getAngle();
		
		if (c_gyro > i_gyro)
			Robot.drive.MotionMagic(l_pos, r_pos, l_accel, r_accel, m_speed * (1 - (.03 * Math.abs(c_gyro))), m_speed);
		else
			Robot.drive.MotionMagic(l_pos, r_pos, l_accel, r_accel, m_speed, m_speed * (1 - (.03 * Math.abs(c_gyro))));

	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
