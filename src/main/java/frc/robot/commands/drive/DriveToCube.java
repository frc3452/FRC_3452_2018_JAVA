package frc.robot.commands.drive;

import frc.robot.Constants;
import frc.robot.Constants.kIntake;
import frc.robot.Robot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Drive.DriveState;

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
	 * @see Drive
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

		i_lpos = Robot.drive.getLeftRotations();
		i_rpos = Robot.drive.getRightRotations();

		System.out.println(i_lpos);
		System.out.println(i_rpos);

		timer.stop();
		timer.reset();
		timer.start();
	}

	@Override
	protected void execute() {
		Robot.drive.arcade(m_speed, 0);

		Robot.intake.manual(kIntake.Speeds.INTAKE);

		if (Robot.drive.getLeftRotations() - i_lpos > m_rotation
				|| Robot.drive.getRightRotations() - i_rpos > m_rotation)
			m_complete = true;

		if (Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) > 12
				|| Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) > 12)
			flag_1 = true;

		if (flag_1 && (Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) < 7
				|| Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) < 7))
			flag_2 = true;

		if (flag_2 && (Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) > 12
				|| Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) > 12)) {

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
		Robot.drive.stop();
		Robot.intake.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
