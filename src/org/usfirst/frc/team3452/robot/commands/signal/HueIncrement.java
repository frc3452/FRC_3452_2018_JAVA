package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class HueIncrement extends InstantCommand {

	/**
	 * @author macco
	 * @see Lights
	 */
	public HueIncrement() {
		super();
		requires(Robot.lights);
	}

	protected void initialize() {
		Robot.lights.m_hue++;
		if (Robot.lights.m_hue > 360)
			Robot.lights.m_hue = 0;
	}

}
