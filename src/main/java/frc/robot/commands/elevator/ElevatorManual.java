package frc.robot.commands.elevator;

import frc.robot.Robot;
import frc.robot.subsystems.Elevator;
import frc.robot.util.GZJoystick;

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
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.elevator.manualJoystick(m_joy);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		Robot.elevator.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}
}
