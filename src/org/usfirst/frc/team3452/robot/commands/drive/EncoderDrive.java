package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class EncoderDrive extends Command {
	private double m_left, m_right, m_lspeed, m_rspeed, m_topspeed;

	public EncoderDrive(double left, double right, double leftspeed, double rightspeed, double topspeed) {
		requires(Drivetrain.getInstance());

		m_left = left;
		m_right = right;
		m_lspeed = leftspeed;
		m_rspeed = rightspeed;
		m_topspeed = topspeed;
	}

	protected void initialize() {
		setTimeout(9);
	}

	protected void execute() {
		Drivetrain.getInstance().MotionMagic(m_left, m_right, m_lspeed, m_rspeed, m_topspeed);
	}

	protected boolean isFinished() {
		return Drivetrain.getInstance().isMove(1.3) || isTimedOut();
	}

	protected void end() {
		Drivetrain.getInstance().EncoderDone();
	}

	protected void interrupted() {
		end();
	}
}
