package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.ESO;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	private ESO m_override;

	/**
	 * @author macco
	 * @param override
	 * @see Drivetrain
	 * @see Elevator
	 * @see ESO
	 */
	public OverrideSet(ESO override) {
		super();
		m_override = override;
	}

	protected void initialize() {
		Robot.elevator.setSpeedLimitingOverride(m_override);
	}
}
