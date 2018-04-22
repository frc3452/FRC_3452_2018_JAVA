package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderGyro extends Command {

	private double l_pos, r_pos, l_accel, r_accel, m_speed, c_gyro, t_gyro, k;

	/**
	 * Encoder drive with gyro correction
	 * 
	 * @author macco
	 * @param leftpos
	 * @param rightpos
	 * @param leftaccel
	 * @param rightaccel
	 * @param speed
	 * @param angle
	 * @param constant
	 * @see Drivetrain
	 */
	public EncoderGyro(double leftpos, double rightpos, double leftaccel, double rightaccel, double speed, double angle,
			double constant) {
		requires(Robot.drive);

		l_pos = leftpos;
		r_pos = rightpos;
		l_accel = leftaccel;
		r_accel = rightaccel;
		m_speed = speed;
		t_gyro = angle;
		c_gyro = 0;
		k = constant;
	}

	protected void initialize() {
		setTimeout(10);
	}

	protected void execute() {
		c_gyro = Robot.drive.Gyro.getAngle();

		if (c_gyro < t_gyro + .4 && c_gyro > t_gyro - .4)
			Robot.drive.MotionMagic(l_pos, r_pos, l_accel, r_accel, m_speed, m_speed);

		else if (c_gyro > t_gyro)
			Robot.drive.MotionMagic(l_pos, r_pos, l_accel, r_accel, m_speed * (1 - (k * Math.abs(c_gyro - t_gyro))),
					m_speed);

		else if (c_gyro < t_gyro)
			Robot.drive.MotionMagic(l_pos, r_pos, l_accel, r_accel, m_speed,
					m_speed * (1 - (k * Math.abs(c_gyro - t_gyro))));
	}

	protected boolean isFinished() {
		return Robot.drive.encoderIsDoneEither(1.3) || isTimedOut();
	}

	protected void end() {
		Robot.drive.encoderDone();
	}

	protected void interrupted() {
		end();
	}
}
