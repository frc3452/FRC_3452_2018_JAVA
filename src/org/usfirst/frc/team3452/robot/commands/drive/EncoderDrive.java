package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class EncoderDrive extends Command {
	private double m_left, m_right, m_laccel, m_raccel, m_topspeed;

	/**
	 * @author macco
	 * @param left
	 * @param right
	 * @param leftaccel
	 * @param rightaccel
	 * @param topspeed
	 * @see Drivetrain
	 * @see Drivetrain
	 */
	public EncoderDrive(double left, double right, double leftaccel, double rightaccel, double topspeed) {
		requires(Robot.drive);

		m_left = left;
		m_right = right;
		m_laccel = leftaccel;
		m_raccel = rightaccel;
		m_topspeed = topspeed;
	}

	protected void initialize() {
		setTimeout(9);
	}

	protected void execute() {
		Robot.drive.motionMagic(m_left, m_right, m_laccel, m_raccel, m_topspeed, m_topspeed);
	}

	protected boolean isFinished() {
		return Robot.drive.encoderIsDone(1.5) || isTimedOut();
	}

	protected void end() {
		Robot.drive.encoderDone();
		System.out.println("Encoder drive completed.");
	}

	protected void interrupted() {
		end();
	}
}
