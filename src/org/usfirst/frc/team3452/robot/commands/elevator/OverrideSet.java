package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	boolean m_override;

	public OverrideSet(boolean override) {
		super();
		m_override = override;
	}

	protected void initialize() {
		Elevator.getInstance().m_overriden = m_override;
	}

}
