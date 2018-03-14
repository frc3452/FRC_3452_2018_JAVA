package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LightsHSV extends Command {

	private double m_h, m_s, m_v;
	private String m_mode;

	public LightsHSV(double h, double s, double v, String mode) {
		requires(Robot.lights);
		m_h = h;
		m_s = s;
		m_v = v;
		m_mode = mode;
	}

	protected void initialize() {
	}

	protected void execute() {
		switch (m_mode) {
		case "RANDOM":
			Robot.lights.hsv((Math.random() * 360 + 0), m_s, m_v);
			break;
		case "CYCLE":
			Robot.lights.hsv(Robot.lights.m_hue, m_s, m_v);
			break;
		default:
			Robot.lights.hsv(m_h, m_s, m_v);
			break;
		}

	}

	protected boolean isFinished() {
		return true;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
