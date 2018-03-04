package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LightsCycle extends CommandGroup {

	public LightsCycle() {
		addSequential(new LightsHSV(Robot.lights.m_hue, 1, .5));
		addSequential(new LightsWait(0.05));

		Robot.lights.m_hue++;
		if (Robot.lights.m_hue > 360)
			Robot.lights.m_hue = 0;
	}
}
