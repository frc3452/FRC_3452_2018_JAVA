package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.EO;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	private EO m_override;

	/**
	 * @author macco
	 * @param override
	 * @see Drivetrain
	 * @see Elevator
	 * @see EO
	 */
	public OverrideSet(EO override) {
		super();
		m_override = override;
	}

	protected void initialize() {
		switch (m_override) {
		case ON:
			Robot.elevator.m_overriden = true;
			break;
		case OFF:
			Robot.elevator.m_overriden = false;
			break;
		case TOGGLE:
			Robot.elevator.m_overriden = !Robot.elevator.m_overriden;
			break;
		}
	}
}
