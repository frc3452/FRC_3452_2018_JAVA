package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class LightsPulse extends InstantCommand {

	private int m_h;
	private double m_s, m_low,m_high, m_rate;

	/**
	 * @author macco
	 * @param h
	 * @param s
	 * @param low
	 * @param high
	 * @param rate
	 * @see Lights
	 */
	public LightsPulse(int h, double s, double low, double high, double rate) {
		super();
		requires(Robot.lights);
		
		m_h = h;
		m_s = s;
		m_low = low;
		m_high = high;
		m_rate = rate;
	}

	protected void initialize() {
		Robot.lights.pulse(m_h, m_s, m_low, m_high, m_rate);
	}

}
