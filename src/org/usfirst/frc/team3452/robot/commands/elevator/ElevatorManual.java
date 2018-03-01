package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorManual extends Command {

	int m_controller_axis;
	Joystick m_joy;

	public ElevatorManual(Joystick joy) {
		requires(Elevator.getInstance());

		m_joy = joy;
		m_controller_axis = 1;
		// operator = left analog y, driver = right analog y
	}

	protected void initialize() {
		m_controller_axis = ((m_joy == OI.operatorJoy) ? 1 : 5);
	}

	protected void execute() {
		Elevator.Elev_1.set(ControlMode.PercentOutput,
				(m_joy.getRawAxis(m_controller_axis) * ((m_joy.getRawAxis(m_controller_axis) > 0) ? .6 : .6))); // .4
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Elevator.Elev_1.set(ControlMode.PercentOutput, 0);
	}
	protected void interrupted() {
		end();
	}
}
