package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.Intake;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class DriveToCube extends Command {

	private double m_speed, m_rotation, i_lpos, i_rpos;
	private boolean m_complete, flag_1, flag_2, timeoutSet;
	private Timer timer = new Timer();

	/**
	 * @author macco
	 * @param speed
	 * @param rotationLimit
	 * @see Drivetrain
	 */
	public DriveToCube(double speed, double rotationLimit) {
		requires(Robot.drive);

		m_speed = speed;
		m_rotation = rotationLimit;
	}

	@Override
	protected void initialize() {
		setTimeout(10);

		m_complete = false;
		flag_1 = false;
		flag_2 = false;
		timeoutSet = false;

		i_lpos = (double) Robot.drive.L1.getSelectedSensorPosition(0) / 4096;
		i_rpos = (double) -Robot.drive.R1.getSelectedSensorPosition(0) / 4096;
		
		System.out.println(i_lpos);
		System.out.println(i_rpos);

		timer.stop();
		timer.reset();
		timer.start();
	}

	@Override
	protected void execute() {
		Robot.drive.arcade(m_speed, 0);

		Robot.intake.manual(Intake.Speeds.INTAKE);

		if (((double) Robot.drive.L1.getSelectedSensorPosition(0) / 4096) - i_lpos > m_rotation
				|| ((double) Robot.drive.R1.getSelectedSensorPosition(0) / 4096) - i_rpos > m_rotation)
			m_complete = true;
		
		if (Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_L) > 12 || Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_R) > 12)
			flag_1 = true;

		if (flag_1 && (Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_L) < 7 || Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_R) < 7))
			flag_2 = true;

		if (flag_2 && (Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_L) > 12 || Robot.drive.pdp.getCurrent(Constants.PDP.INTAKE_R) > 12)) {

			if (!timeoutSet) {
				setTimeout(timer.get() + .4);
				timeoutSet = true;
			}
		}
	}

	@Override
	protected boolean isFinished() {
		return isTimedOut() || m_complete;
	}

	@Override
	protected void end() {
		Robot.drive.arcade(0, 0);
		Robot.intake.manual(0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
