package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drive;

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