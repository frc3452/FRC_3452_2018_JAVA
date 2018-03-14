package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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

		OI.driverJoy.setRumble(RumbleType.kLeftRumble, 1);
		OI.driverJoy.setRumble(RumbleType.kRightRumble, 1);
		OI.opJoy.setRumble(RumbleType.kLeftRumble, 1);
		OI.opJoy.setRumble(RumbleType.kRightRumble, 1);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.intake.manual(0);

		OI.driverJoy.setRumble(RumbleType.kLeftRumble, 0);
		OI.driverJoy.setRumble(RumbleType.kRightRumble, 0);
		OI.opJoy.setRumble(RumbleType.kLeftRumble, 0);
		OI.opJoy.setRumble(RumbleType.kRightRumble, 0);
	}

	protected void interrupted() {
		end();
	}
}
