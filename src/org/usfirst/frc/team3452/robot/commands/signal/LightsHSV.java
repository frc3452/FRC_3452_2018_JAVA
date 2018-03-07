package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LightsHSV extends InstantCommand {

	private double m_h, m_s, m_v;

	public LightsHSV(double h, double s, double v) {
		super();
		requires(Robot.lights);

		m_h = h;
		m_s = s;
		m_v = v;
	}

	protected void initialize() {
		Robot.lights.hsv(m_h, m_s, m_v);
	}

}
