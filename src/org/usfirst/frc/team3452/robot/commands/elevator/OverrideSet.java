package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class OverrideSet extends InstantCommand {

	private int m_override;

	public OverrideSet(int override) {
		super();
		m_override = override;
	}

	protected void initialize() {
		if (m_override == -1) {
			Robot.elevator.m_overriden = !Robot.elevator.m_overriden;
		} else if (m_override == 0) {
			Robot.elevator.m_overriden = false;
		} else if (m_override == 1) {
			Robot.elevator.m_overriden = true;
		}
	}

}
