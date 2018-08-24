package org.usfirst.frc.team3452.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.ESO;
import org.usfirst.frc.team3452.robot.subsystems.Elevator.ElevatorState;

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
		if (Robot.elevator.getState() != ElevatorState.DEMO)
			switch (m_override) {
			case ON:
				Robot.elevator.setOverriden(true);
				break;
			case OFF:
				Robot.elevator.setOverriden(false);
				break;
			case TOGGLE:
				Robot.elevator.setOverriden(!Robot.elevator.isOverriden());
				break;
			}
	}
}
