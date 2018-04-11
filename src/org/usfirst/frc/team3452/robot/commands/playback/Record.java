package org.usfirst.frc.team3452.robot.commands.playback;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;

import edu.wpi.first.wpilibj.command.Command;

public class Record extends Command {

	private TASK m_task;
	private String m_name;

	public Record(String name, TASK task) {
		//    	requires(Robot.playback);

		m_name = name;
		m_task = task;
	}

	protected void initialize() {
		switch (m_task) {
		case RECORD:
			Robot.drive.timer.stop();
			Robot.drive.timer.reset();
			Robot.drive.timer.start();

			System.out.println("Opening RECORD");
			Robot.playback.createFile(m_name, false);
			break;
		case PARSE:
			System.out.println("Opening PARSE");
			Robot.playback.createFile(m_name, true);

			Robot.playback.parseFile();
			//			System.out.println("printing");
			//			Robot.playback.printTimes();
			break;
		case PLAY:
			break;
		}
	}

	protected void execute() {
		switch (m_task) {
		case RECORD:
			Robot.playback.writeToFile();
			break;
		case PARSE:
			break;
		case PLAY:
			break;
		}
	}

	protected boolean isFinished() {
		return false;
	}

	protected void end() {
		switch (m_task) {
		case RECORD:
			System.out.println("Closing RECORD");
			Robot.playback.closeFile(false);
			break;
		case PARSE:
			System.out.println("Closing PARSE");
			Robot.playback.closeFile(true);
			break;
		case PLAY:
			break;
		}
	}

	protected void interrupted() {
		end();
	}
}
