package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LightsHSV extends InstantCommand {

	private double m_h, m_s, m_v;

	public LightsHSV(double h, double s, double v) {
		super();
		requires(Lights.getInstance());

		m_h = h;
		m_s = s;
		m_v = v;
	}

	protected void initialize() {
		Lights.getInstance().hsv(m_h, m_s, m_v);
	}

}
