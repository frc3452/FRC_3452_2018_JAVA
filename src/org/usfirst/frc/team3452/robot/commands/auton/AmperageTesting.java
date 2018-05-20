package org.usfirst.frc.team3452.robot.commands.auton;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Intake;

public class AmperageTesting extends Command {

	private boolean m_drive, m_elev, m_inke, m_climb;
	private double m_inc;
	private double percentage = 0;
	private boolean direction = true;

	Timer timer = new Timer();

	/**
	 * <b>WARNING</b> Remove from teleop init 
	 * 
	 * @param increment
	 * @param drivetrain
	 * @param elevator
	 * @param intake
	 * @param climber
	 * @see Drivetrain
	 * @see Intake
	 * @see Elevator
	 * @see Climber
	 */
	public AmperageTesting(double increment, boolean drivetrain, boolean elevator, boolean intake, boolean climber) {
		requires(Robot.elevator);
		m_drive = drivetrain;
		m_elev = elevator;
		m_inke = intake;
		m_climb = climber;
		m_inc = increment;
	}

	protected void initialize() {
		timer.stop();
		timer.reset();
		timer.start();

		percentage = m_inc;
	}

	protected void execute() {
		if (m_drive) {
			Robot.drive.arcade(percentage, 0);
		} else {
			Robot.drive.arcade(0, 0);
		}

		if (m_elev) {
			Robot.elevator.encoder((percentage > 0) ? 3.5 : 0);
		} else {
			Robot.elevator.encoderDone();
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

		if (percentage >= 1)
			direction = false;
		if (percentage <= -1)
			direction = true;

		if (direction)
			percentage += m_inc;
		else
			percentage -= m_inc;
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.drive.arcade(0, 0);
		Robot.elevator.encoderDone();
		Robot.intake.manual(0);
		Robot.climber.climb1.set(0);
	}

	protected void interrupted() {
		end();
	}
}
