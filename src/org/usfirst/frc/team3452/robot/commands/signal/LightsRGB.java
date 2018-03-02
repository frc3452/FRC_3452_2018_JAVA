package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class LightsRGB extends InstantCommand {

	private int m_r, m_g, m_b;

	public LightsRGB(int r, int g, int b) {
		super();
		requires(Lights.getInstance());
	}

	protected void initialize() {
		Lights.getInstance().rgb(m_r, m_g, m_b);
	}
}
