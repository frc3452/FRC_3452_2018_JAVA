package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.Lights;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LightsCycle extends CommandGroup {

	/**
	 * @author max
	 * @see Lights
	 */
	public LightsCycle() {
		addSequential(new LightsHSV(0, 1, .25, "CYCLE"));
		addSequential(new HueIncrement());
		addSequential(new LightsWait(0.02));
	}
}
