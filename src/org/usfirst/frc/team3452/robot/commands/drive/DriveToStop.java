package org.usfirst.frc.team3452.robot.commands.drive;

import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class DriveToStop extends Command {
	private double m_speed;

	public DriveToStop(double speed) {
		requires(Drivetrain.getInstance());
	}

	protected void initialize() {
		setTimeout(15);
	}

	protected void execute() {
		Drivetrain.getInstance().Arcade(m_speed, 0);
	}

	protected boolean isFinished() {
		Drivetrain.getInstance();
		return (Drivetrain.L1.getSelectedSensorVelocity(0) < 150 || Drivetrain.R1.getSelectedSensorVelocity(0) > -150)
				|| isTimedOut();
	}

	protected void end() {
		Drivetrain.getInstance().Arcade(0, 0);
	}

	protected void interrupted() {
		end();
	}
}
