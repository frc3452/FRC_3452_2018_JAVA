package org.usfirst.frc.team3452.robot.commands.signal;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LightsCycle extends CommandGroup {

	public LightsCycle() {
		addSequential(new LightsHSV(0, 1, .25, "CYCLE"));
		addSequential(new HueIncrement());
		addSequential(new LightsWait(0.02));
	}
}
