package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToCube extends Command {

	private double m_speed, m_centerX;
	private boolean m_complete, flag_1, flag_2;

	public DriveToCube(double speed) {
		requires(Robot.drive);

		m_speed = speed;
	}

	protected void initialize() {
		setTimeout(17);
		m_complete = false;
		flag_1 = false;
		flag_2 = false;
	}

	protected void execute() {
		if (Robot.lights.centerX(0) == 3452)
			m_centerX = 320;
		else
			m_centerX = Robot.lights.centerX(0);

		if (Robot.lights.visionLength() > 0) {

			if (m_centerX < 335 && m_centerX > 325) {
				Robot.drive.Arcade(m_speed, 0);

				//				m_complete = true;

			} else if (m_centerX > 335) {

				Robot.drive.Arcade(m_speed, .125 * 2.1);

			} else if (m_centerX < 325) {

				Robot.drive.Arcade(m_speed, -.125 * 2.1);

			}
		} else {
			Robot.drive.Arcade(m_speed, 0);
		}

		Robot.intake.manual(-.9);

		if (Robot.drive.pdp.getCurrent(9) > 8 || Robot.drive.pdp.getCurrent(8) > 8)
			flag_1 = true;

		if (flag_1 && (Robot.drive.pdp.getCurrent(9) < 6 || Robot.drive.pdp.getCurrent(8) < 6))
			flag_2 = true;

		if (flag_2 && (Robot.drive.pdp.getCurrent(9) > 11 || Robot.drive.pdp.getCurrent(8) > 11))
			setTimeout(1.2);

		System.out.println(m_centerX);
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
