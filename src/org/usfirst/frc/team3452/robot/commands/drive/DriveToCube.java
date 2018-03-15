package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToCube extends Command {

	private boolean m_complete = false, intake_startup = false, intake_stable = false;

	public DriveToCube() {
		requires(Robot.drive);
	}

	protected void initialize() {
		setTimeout(8);
		m_complete = false;
		intake_startup = false;
		intake_stable = false;
	}

	protected void execute() {
		if (Robot.lights.visionLength() > 0) {
			//			System.out.println(Robot.lights.centerX(0));

			if (Robot.lights.centerX(0) < 335 && Robot.lights.centerX(0) > 325) {
				Robot.drive.Arcade(0.45, 0);
				//				m_complete = true;
			} else if (Robot.lights.centerX(0) > 335) {
				Robot.drive.Arcade(0.45, .125 * 2.1);
			} else if (Robot.lights.centerX(0) < 325) {
				Robot.drive.Arcade(0.45, -.125 * 2.1);
			}
		} else {
			Robot.drive.Arcade(.3, 0);
		}

		Robot.intake.manual(-1);

		if (Robot.drive.pdp.getCurrent(9) > 10 || Robot.drive.pdp.getCurrent(8) > 10)
			intake_startup = true;
		
		if (intake_startup && (Robot.drive.pdp.getCurrent(9) < 7.5 || Robot.drive.pdp.getCurrent(8) < 7.5))
			intake_stable = true;
		
		if (intake_stable && (Robot.drive.pdp.getCurrent(9) > 10 || Robot.drive.pdp.getCurrent(8) > 10))
			m_complete = true;

		System.out.println("startup: " + intake_startup + "\t\t\tstable: " + intake_stable);
//		System.out.println(Robot.drive.pdp.getCurrent(9) + "\t\t" + Robot.drive.pdp.getCurrent(8));
	}

	protected boolean isFinished() {
		return isTimedOut() || m_complete;
	}

	protected void end() {
		Robot.drive.Arcade(0, 0);
		Robot.intake.manual(0);
	}

	protected void interrupted() {
		end();
	}
}
