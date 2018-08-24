package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class GyroReset extends Command {

	/**
	 * Gyro reset
	 * @author macco
	 * @see Drivetrain
	 */
	public GyroReset() {
		requires(Robot.drive);
	}

	protected void initialize() {
		setTimeout(0.1);
		Robot.drive.getGyro().reset();
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