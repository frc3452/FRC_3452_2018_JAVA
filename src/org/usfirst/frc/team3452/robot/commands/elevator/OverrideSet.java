package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	private int m_override;

	public OverrideSet(int override) {
		super();
		m_override = override;
	}

	protected void initialize() {
		if (m_override == -1) {
			Elevator.getInstance().m_overriden = !Elevator.getInstance().m_overriden;
		} else if (m_override == 0) {
			Elevator.getInstance().m_overriden = false;
		} else if (m_override == 1) {
			Elevator.getInstance().m_overriden = true;
		}
	}

}
