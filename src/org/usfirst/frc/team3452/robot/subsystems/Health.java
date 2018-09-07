package org.usfirst.frc.team3452.robot.subsystems;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Util.AlertLevel;

public class Health {

	private ArrayList<AlertLevel> mAlertLevel;
	private ArrayList<String> mSubsystem;
	private ArrayList<String> mMessage;

	public Health() {
		List<String> subsystems = Arrays.asList(Drive.class.getSimpleName(), Elevator.class.getSimpleName(),
				Intake.class.getSimpleName(), Climber.class.getSimpleName(), Lights.class.getSimpleName());
		
		for (String s : subsystems) {
			mSubsystem.add(s);
			mAlertLevel.add(AlertLevel.NONE);
			mMessage.add("NA");
		}
	}

	public void addAlert(AlertLevel level, GZSubsystem subsystem, String message) {
		mAlertLevel.add(level);
		mSubsystem.add(subsystem.getClass().getSimpleName().toString());
		mMessage.add(message);
	}

	public void generateHealth() {
		String htmlString = base_file;
		String body = "";
		//make array for errors per subsystem
		//find highest AlertLevel, store
		//For loop, 
		//if( Arrays.asList("a","b","c").contains("a") )
	}

	private String base_file = "<html>\r\n" + "<head>\r\n" + "<style>\r\n" + "table {\r\n"
			+ "    border: 1px solid black;\r\n" + "    border-collapse: collapse;\r\n" + "  	width:50%;\r\n"
			+ "}\r\n" + "  body { \r\n" + "  }\r\n" + "th, td {\r\n" + "  	border: 5px solid black;\r\n"
			+ "    padding: 5px;\r\n" + "    text-align: center;\r\n" + "}\r\n" + "  \r\n" + "</style>\r\n"
			+ "</head>\r\n" + "<body>\r\n" + "\r\n" + "$BODY\r\n" + "\r\n" + "</body>\r\n" + "</html>";
}
