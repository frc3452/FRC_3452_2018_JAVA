package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class LightsHSV extends Command {

	private double m_h, m_s, m_v, c_h, c_s, c_v, timeout;
	private String m_mode;
	private boolean complete = false;

	public LightsHSV(double h, double s, double v, String mode) {
		requires(Robot.lights);

		m_h = h;
		m_s = s;
		m_v = v;
		m_mode = mode;

	}

	protected void initialize() {
		complete = false;
		if (m_mode.charAt(0) == 'F') {
			if (m_h > Robot.lights.p_h)
				c_h = Robot.lights.p_h / m_h;
			else if (m_h < Robot.lights.p_h)
				c_h = m_h / Robot.lights.p_h;
			else
				c_h = 1;

			if (m_s > Robot.lights.p_s)
				c_s = Robot.lights.p_s = m_s;
			else if (m_s < Robot.lights.p_s)
				c_s = m_s / Robot.lights.p_s;
			else
				c_s = 1;

			if (m_v > Robot.lights.p_v)
				c_v = Robot.lights.p_v = m_v;
			else if (m_v < Robot.lights.p_v)
				c_v = m_v / Robot.lights.p_v;
			else
				c_v = 1;

		}

		Robot.lights.lightTimer.stop();
		Robot.lights.lightTimer.reset();
	}

	protected void execute() {
		switch (m_mode.charAt(0)) {
		case 'R':
			Robot.lights.hsv(Math.random() * 360 + 0, m_s, m_v);
			break;
		case 'C':
			Robot.lights.hsv(Robot.lights.m_hue, m_s, m_v);
			Robot.lights.m_hue++;
			if (Robot.lights.m_hue > 360)
				Robot.lights.m_hue = 0;
			break;
		case 'F':
			timeout = (double) Character.digit(m_mode.charAt(m_mode.length() - 1), 1);
			Robot.lights.hsv(c_h * (Robot.lights.lightTimer.get() / timeout),
					c_s * (Robot.lights.lightTimer.get() / timeout), c_v * (Robot.lights.lightTimer.get() / timeout));
			break;
		default:
			Robot.lights.hsv(m_h, m_s, m_v);
			break;
		}

		if (m_mode.charAt(0) == 'F') {
			if (Robot.lights.lightTimer.get() >= timeout) {
				complete = true;
			}
		} else {
			complete = true;
		}

	}

	protected boolean isFinished() {
		return complete;
	}

	protected void end() {
	}

	protected void interrupted() {
	}
}
