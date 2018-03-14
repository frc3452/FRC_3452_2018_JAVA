package org.usfirst.frc.team3452.robot.triggers;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 *
 */
public class IntakeIn extends Trigger {

    public boolean get() {
    	return (Robot.intake.Intake_L.get() < 0);
    }
}
