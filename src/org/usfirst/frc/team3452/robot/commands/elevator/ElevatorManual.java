package org.usfirst.frc.team3452.robot.commands.elevator;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.util.GZJoystick;

import edu.wpi.first.wpilibj.command.Command;

public class ElevatorManual extends Command {

//	private int m_axis;
	private GZJoystick m_joy;

//	double value = 0;
//	double speeds[] = new double[2];

	/**
	 * Operator control of elevator
	 * 
	 * @author macco
	 * @param joy
	 * @see Elevator
	 */
	public ElevatorManual(GZJoystick joy) {
		requires(Robot.elevator);
		m_joy = joy;
//		m_axis = 1;
		// operator = left analog y, driver = right analog y
	}

	@Override
	protected void initialize() {
//		m_axis = ((m_joy == OI.opJoy) ? 1 : 5);

		// speeds[0] is downward speed, speeds[1] is upward speed
//		if (!Robot.autonSelector.isDemo()) {
//			speeds[0] = .6;
//			speeds[1] = 1;
//		} else {
//			Robot.elevator.softLimits(true);
//			speeds[0] = .3;
//			speeds[1] = .5;
//		}
	}

	@Override
	protected void execute() {
//		value = m_joy.getRawAxis(m_axis) * (m_joy.getRawAxis(m_axis) > 0 ? speeds[0] : speeds[1]);
//		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, value);
		Robot.elevator.manualJoystick(m_joy);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.elevator.stop();
//		Robot.elevator.Elev_1.set(ControlMode.PercentOutput, 0);
//		Robot.elevator.softLimits(false);
	}

	@Override
	protected void interrupted() {
		end();
	}
}
