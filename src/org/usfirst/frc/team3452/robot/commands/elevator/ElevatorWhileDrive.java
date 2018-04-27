package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorWhileDrive extends Command {

	private double m_value, m_percent;
	private boolean l_rev = false, l_fwd = false;

	/**
	 * @author macco
	 * @param value
	 * @param atPercent
	 * @see Elevator
	 */
	public ElevatorWhileDrive(double value, double atPercent) {
		requires(Robot.elevator);

		m_value = value;
		m_percent = atPercent;
	}

	@Override
	protected void initialize() {
		setTimeout(15);
		Robot.elevator.m_pos = -3452;
	}

	@Override
	protected void execute() {
		if (Robot.drive.p_pos > m_percent)
			Robot.elevator.encoder(m_value);
		else
			Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);

		if (Robot.elevator.isRemoteSensor) {
			l_rev = Robot.elevator.Elev_2.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_2.getSensorCollection().isFwdLimitSwitchClosed();
		} else {
			l_rev = Robot.elevator.Elev_1.getSensorCollection().isRevLimitSwitchClosed();
			l_fwd = Robot.elevator.Elev_1.getSensorCollection().isFwdLimitSwitchClosed();
		}
	}

	@Override
	protected boolean isFinished() {
		if (l_rev && m_value > 0)
			return true;

		if (l_fwd && m_value < 0)
			return true;

		return Robot.elevator.isDone(3.5) || isTimedOut();
	}

	@Override
	protected void end() {
		Robot.elevator.encoderDone();
		System.out.println("Elevator position completed.");
	}

	@Override
	protected void interrupted() {
		end();
	}
}
