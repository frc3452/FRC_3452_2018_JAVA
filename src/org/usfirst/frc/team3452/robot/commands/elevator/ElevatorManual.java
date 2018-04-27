package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorManual extends Command {

	private int m_axis;
	private Joystick m_joy;

	double speedmodifier = 0;

	/**
	 * Operator control of elevator
	 * 
	 * @author macco
	 * @param joy
	 * @see Elevator
	 */
	public ElevatorManual(Joystick joy) {
		requires(Robot.elevator);

		m_joy = joy;
		m_axis = 1;
		// operator = left analog y, driver = right analog y
	}

	@Override
	protected void initialize() {
		m_axis = ((m_joy == OI.opJoy) ? 1 : 5);
	}

	@Override
	protected void execute() {
		speedmodifier = m_joy.getRawAxis(m_axis) * (m_joy.getRawAxis(m_axis) > 0 ? .6 : 1);

		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, speedmodifier);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
