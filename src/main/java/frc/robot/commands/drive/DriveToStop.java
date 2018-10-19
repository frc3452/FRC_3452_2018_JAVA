package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.subsystems.Drive;

public class DriveToStop extends Command {
	private double m_speed;

	/**
	 * @author macco
	 * @param speed
	 * @see Drive
	 */
	public DriveToStop(double speed) {
		requires(Robot.drive);
		
		m_speed = speed;
	}

	protected void initialize() {
		setTimeout(10);
	}

	protected void execute() {
		Robot.drive.arcade(m_speed, 0);
	}

	protected boolean isFinished() {
		return Robot.drive.encoderSpeedIsUnder(200) || isTimedOut();
	}

	protected void end() {
		Robot.drive.stop();
	}

	protected void interrupted() {
		end();
	}
}
