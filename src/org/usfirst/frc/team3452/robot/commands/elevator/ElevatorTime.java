package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorTime extends Command {

	private double m_speed, m_time;
	private boolean l_rev = false, l_fwd = false;

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

		if (Robot.elevator.isRemoteSensor) {
			l_rev = Robot.elevator.Elev_2.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_2.getSensorCollection().isFwdLimitSwitchClosed();
		} else {
			l_rev = Robot.elevator.Elev_1.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_1.getSensorCollection().isFwdLimitSwitchClosed();
		}
	}

	protected boolean isFinished() {
		if (l_rev && m_speed > 0)
			return true;

		if (l_fwd && m_speed < 0)
			return true;

		return isTimedOut();
	}

	protected void end() {
		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);
	}

	protected void interrupted() {
		end();
	}
}
