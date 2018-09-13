package frc.robot.commands.drive;

import frc.robot.Robot;
import frc.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.command.Command;

public class GyroReset extends Command {

	/**
	 * Gyro reset
	 * @author macco
	 * @see Drive
	 */
	public GyroReset() {
		requires(Robot.drive);
	}

	protected void initialize() {
		setTimeout(0.1);
		Robot.drive.zeroGyro();
	}
	protected void execute() {
		
	}
	protected boolean isFinished() {
		return isTimedOut();
	}
	protected void end() {
	}
	protected void interrupted() {
		end();
	}
}