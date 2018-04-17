package org.usfirst.frc.team3452.robot.commands.playback;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Playback.STATE;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;

import edu.wpi.first.wpilibj.command.Command;

public class Record extends Command {

	private TASK m_task;
	private String m_name;

	public Record(String name, TASK task) {
		m_task = task;
		m_name = name;
	}

	protected void initialize() {
		Robot.playback.playbackControl(m_name, m_task, STATE.STARTUP);
	}

	protected void execute() {
		Robot.playback.playbackControl(m_name, m_task, STATE.RUNTIME);
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		Robot.playback.playbackControl(m_name, m_task, STATE.FINISH);
	}

	protected void interrupted() {
		end();
	}
}
