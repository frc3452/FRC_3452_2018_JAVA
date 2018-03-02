package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LightsHSV extends InstantCommand {

	private double m_h, m_s, m_v, m_r, m_g, m_b;
	private float m_rgb[];

	public LightsHSV(double h, double s, double v) {
		super();
		requires(Lights.getInstance());

		m_h = h;
		m_s = s;
		m_v = v;
	}

	protected void initialize() {
		m_rgb = Lights.getInstance().hsv(m_h, m_s, m_v);
		Lights.getInstance().rgb(m_rgb[0], m_rgb[1], m_rgb[2]);
	}

}
