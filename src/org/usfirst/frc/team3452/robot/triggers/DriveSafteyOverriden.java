
package org.usfirst.frc.team3452.robot.triggers;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * returns boolean Robot.elevator.m_overriden
 * @author max
 * @see Elevator
 */
public class DriveSafteyOverriden extends Trigger {

	@Override
	public boolean get() {
		return Robot.elevator.m_overriden;
	}
}
