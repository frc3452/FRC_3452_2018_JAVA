package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

public class AmperageTesting extends Command {

	private boolean m_drive, m_elev, m_inke, m_climb;
	private double input[];
	private double percentage = 0;
	private boolean direction = true;

	Timer timer = new Timer();

	public AmperageTesting(double values[],boolean drivetrain, boolean elevator, boolean intake, boolean climber) {
		requires(Robot.elevator);
		m_drive = drivetrain;
		m_elev = elevator;
		m_inke = intake;
		m_climb = climber;
		input = values;
	}

	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();
		setTimeout(10);
		
		percentage = input[1];
	}

	protected void execute() {
		if (m_drive) {
			Robot.drive.Arcade(percentage, 0);
		} else {
			Robot.drive.Arcade(0, 0);
		}
		
		if (m_elev) {
			Robot.elevator.Encoder((percentage > 0) ? 3.5 : 0);
		} else {
			Robot.elevator.EncoderDone();
			Robot.elevator.Elev_1.set(0);
		}
		if (m_inke) {
			Robot.intake.manual(percentage);
		} else {
			Robot.intake.manual(0);
		}
		if (m_climb) {
			Robot.climber.climb1.set(percentage);
		} else {
			Robot.climber.climb1.set(0);
		}
	
		if (percentage >= input[2])
			direction = false;
		if (percentage <= input[1])
			direction = true;

		if (direction)
			percentage += input[0];
		else
			percentage -= input[0];
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drive.Arcade(0, 0);
		Robot.elevator.EncoderDone();
		Robot.intake.manual(0);
		Robot.climber.climb1.set(0);
	}

	protected void interrupted() {
		end();
	}
}
