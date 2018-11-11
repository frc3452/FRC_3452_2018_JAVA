package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Constants;
import frc.robot.Constants.kIntake;
import frc.robot.Robot;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;

public class DriveToCube extends Command {

	private double m_speed, m_rotation, i_lpos, i_rpos;
	private boolean m_complete, flag_1, flag_2, timeoutSet;
	private Timer timer = new Timer();

	private Drive drive = Drive.getInstance();

	/**
	 * @author macco
	 * @param speed
	 * @param rotationLimit
	 * @see Drive
	 */
	public DriveToCube(double speed, double rotationLimit) {
		requires(drive);

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

		i_lpos = drive.getLeftRotations();
		i_rpos = drive.getRightRotations();

		System.out.println(i_lpos);
		System.out.println(i_rpos);

		timer.stop();
		timer.reset();
		timer.start();
	}

	@Override
	protected void execute() {
		drive.arcade(m_speed, 0);

		Intake.getInstance().manual(kIntake.Speeds.INTAKE);

		if (drive.getLeftRotations() - i_lpos > m_rotation
				|| drive.getRightRotations() - i_rpos > m_rotation)
			m_complete = true;

		if (drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) > 12
				|| drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) > 12)
			flag_1 = true;

		if (flag_1 && (drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) < 7
				|| drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) < 7))
			flag_2 = true;

		if (flag_2 && (drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) > 12
				|| drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) > 12)) {

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
		drive.stop();
		Intake.getInstance().stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
