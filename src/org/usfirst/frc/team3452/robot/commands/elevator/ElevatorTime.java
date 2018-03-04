package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorTime extends Command {

	private double m_speed, m_time;

	public ElevatorTime(double speed, double time) {
		requires(Robot.elevator);

		m_speed = speed;
		m_time = time;
	}

	protected void initialize() {
		setTimeout(m_time);
	}

	protected void execute() {
		Robot.elevator.Elev_1.set(-m_speed);
	}

	protected boolean isFinished() {
		return isTimedOut();
	}

	protected void end() {
		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);
	}

	protected void interrupted() {
		end();
	}
}
