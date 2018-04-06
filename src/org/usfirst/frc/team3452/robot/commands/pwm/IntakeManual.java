package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.CONTROLLER;

import edu.wpi.first.wpilibj.command.Command;

public class IntakeManual extends Command {
	private double m_speed;

	public IntakeManual(double speed) {
		requires(Robot.intake);
		
		m_speed = speed;
	}

	protected void initialize() {
	}

	protected void execute() {
		Robot.intake.manual(m_speed);
		OI.rumble(CONTROLLER.BOTH, .4);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.intake.manual(0);
		OI.rumble(CONTROLLER.BOTH,0);
	}

	protected void interrupted() {
		end();
	}
}
