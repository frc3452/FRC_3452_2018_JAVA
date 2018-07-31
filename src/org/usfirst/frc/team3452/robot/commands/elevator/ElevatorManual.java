package org.usfirst.frc.team3452.robot.commands.elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;

public class ElevatorManual extends Command {

	private int m_axis;
	private Joystick m_joy;

	double speedmodifier = 0;
	double speeds[] = new double[2];

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

		if (!Robot.autonSelector.isSaftey()) {
			speeds[0] = .6;
			speeds[1] = 1;
		} else {
			speeds[0] = .3;
			speeds[1] = .5;
		}
	}

	@Override
	protected void execute() {
		// TODO REMOVE
		if (m_joy.getRawAxis(m_axis) > 0)
			// DOWN
			speedmodifier = m_joy.getRawAxis(m_axis) * speeds[0];
		else {

			// UP

			if (((double) -Robot.elevator.Elev_1.getSelectedSensorPosition(0) / 4096) < 5)
				speedmodifier = m_joy.getRawAxis(m_axis) * speeds[1];
			else {
				if (m_joy.getRawAxis(m_axis) > 0)
					speedmodifier = m_joy.getRawAxis(m_axis) * speeds[1];
				else
					speedmodifier = 0;
			}
		}

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
