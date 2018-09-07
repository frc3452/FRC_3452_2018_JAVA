package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drive;
import org.usfirst.frc.team3452.robot.subsystems.Drive.DriveState;

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
		Robot.drive.setWantedState(DriveState.OPEN_LOOP);
	}

	protected void execute() {
		Robot.drive.arcade(m_speed, 0);
	}

	protected boolean isFinished() {
		return Robot.drive.encoderSpeedIsUnder(60) || isTimedOut();
	}

	protected void end() {
		Robot.drive.stop();
	}

	protected void interrupted() {
		end();
	}
}
