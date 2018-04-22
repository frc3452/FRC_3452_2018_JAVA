package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorManual extends Command {

	private int m_controller_axis;
	private Joystick m_joy;

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
		m_controller_axis = 1;
		// operator = left analog y, driver = right analog y
	}

	protected void initialize() {
		m_controller_axis = ((m_joy == OI.opJoy) ? 1 : 5);
	}

	protected void execute() {
		Robot.elevator.Elev_1.set(ControlMode.PercentOutput,
				(m_joy.getRawAxis(m_controller_axis) * ((m_joy.getRawAxis(m_controller_axis) > 0) ? .6 : .9))); // .6 down .9 up
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);
	}

	protected void interrupted() {
		end();
	}
}
