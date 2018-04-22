package org.usfirst.frc.team3452.robot.commands.playback;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Playback;
import org.usfirst.frc.team3452.robot.subsystems.Playback.STATE;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;

import edu.wpi.first.wpilibj.command.Command;

public class Record extends Command {

	private TASK m_task;
	private String m_name;
	private boolean m_usb;

	/**
	 * Command version of playback control
	 * 
	 * @author macco
	 * @param name
	 * @param usb
	 * @param task
	 * @see Playback
	 */
	public Record(String name, boolean usb, TASK task) {
		m_task = task;
		m_name = name;
		m_usb = usb;
	}

	protected void initialize() {
		Robot.playback.control(m_name, m_usb, m_task, STATE.STARTUP);
	}

	protected void execute() {
		Robot.playback.control(m_name, m_usb, m_task, STATE.RUNTIME);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.playback.control(m_name, m_usb, m_task, STATE.FINISH);
	}

	protected void interrupted() {
		end();
	}
}
