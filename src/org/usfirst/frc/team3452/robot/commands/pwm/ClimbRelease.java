package org.usfirst.frc.team3452.robot.commands.pwm;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ClimbRelease extends CommandGroup {

    public ClimbRelease() {
    	addSequential(new ClimbReleaseControl(1, .125));
    	addSequential(new ClimbReleaseControl(-1, .125));
    }
}
