package org.usfirst.frc.team3452.robot.triggers;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * @author max
 * @return Robot.elevator.m_overriden
 * @see Elevator
 */
public class DriveSafteyOverriden extends Trigger {

	public boolean get() {
		return Robot.elevator.m_overriden;
	}
}
