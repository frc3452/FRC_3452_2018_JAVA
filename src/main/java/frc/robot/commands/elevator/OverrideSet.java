package frc.robot.commands.elevator;

import frc.robot.Robot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Elevator.ESO;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	private ESO m_override;

	/**
	 * @author macco
	 * @param override
	 * @see Drive
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
