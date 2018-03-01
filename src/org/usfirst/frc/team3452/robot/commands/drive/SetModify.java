package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class SetModify extends InstantCommand {
	double m_value;

	public SetModify(double value) {
		super();
		m_value = value;
	}

	protected void initialize() {
		Drivetrain.getInstance().m_modify = m_value;
	}

}
