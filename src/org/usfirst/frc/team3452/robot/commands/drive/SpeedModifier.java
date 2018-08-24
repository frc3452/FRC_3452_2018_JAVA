package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.DriveState;

public class SpeedModifier extends InstantCommand {
	private double m_modify;

	/**
	 * <h1>Drivetrain speed modifier</h1>
	 * 
	 * @param value -1 = toggle from full to 60%, else is set
	 * @see Drivetrain
	 */
	public SpeedModifier(double value) {
		super();
		m_modify = value;
	}

	protected void initialize() {

		if (Robot.drive.getState() == DriveState.DEMO) {
			if (m_modify == -1) {
				if (Robot.drive.getPercentageModify() == 1)
					Robot.drive.setPercentageModify(.75);
				else if (Robot.drive.getPercentageModify() == .75)
					Robot.drive.setPercentageModify(.5);
				else if (Robot.drive.getPercentageModify() == .5)
					Robot.drive.setPercentageModify(1);

			} else {
				Robot.drive.setPercentageModify(m_modify);
			}
		}
	}

}
