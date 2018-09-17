
package frc.robot.triggers;

import frc.robot.Robot;
import frc.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.buttons.Trigger;

/**
 * returns boolean Robot.elevator.m_overriden
 * @author max
 * @see Elevator
 */
public class DriveSafteyOverriden extends Trigger {

	@Override
	public boolean get() {
		return Robot.elevator.isSpeedOverriden();
	}
}
