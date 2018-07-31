package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class SpeedModifier extends InstantCommand {
	private double m_modify;

	/**
	 * <h1>Drivetrain speed modifier</h1>
	 * 
	 * @param value
	 *            -1 = toggle from full to 60%, else is set
	 * @see Drivetrain
	 */
	public SpeedModifier(double value) {
		super();
		m_modify = value;
	}

	protected void initialize() {
		if (!Robot.autonSelector.isSaftey()) {
			if (m_modify == -1) {

				if (Robot.drive.m_modify == 1)
					Robot.drive.m_modify = .75;
				else if (Robot.drive.m_modify == .75)
					Robot.drive.m_modify = .5;
				else if (Robot.drive.m_modify == .5)
					Robot.drive.m_modify = 1;

			} else {
				Robot.drive.m_modify = m_modify;
			}
		}
	}

}
