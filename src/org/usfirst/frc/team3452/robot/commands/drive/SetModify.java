package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class SetModify extends InstantCommand {
	private double m_modify;

	public SetModify(double value) {
		super();
		m_modify = value;
	}

	protected void initialize() {
		if (m_modify == -1) {
			if (Drivetrain.getInstance().m_modify == 1) {
				Drivetrain.getInstance().m_modify = .5;
			} else {
				Drivetrain.getInstance().m_modify = 1;
			}
		} else {
			Drivetrain.getInstance().m_modify = m_modify;
			;
		}
	}

}
