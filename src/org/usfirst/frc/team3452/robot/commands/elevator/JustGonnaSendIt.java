package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class JustGonnaSendIt extends CommandGroup {

    public JustGonnaSendIt() {
    	addSequential(new ElevatorTime(1, 10));
		addSequential(new IntakeTime(0, .0495));
		addSequential(new IntakeTime(1, .5));
    }
}
