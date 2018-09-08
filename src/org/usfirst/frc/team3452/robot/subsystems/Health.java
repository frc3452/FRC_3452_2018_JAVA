package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;
import org.usfirst.frc.team3452.robot.util.Util;

public class Health {

	private Map<GZSubsystem, ArrayList<ArrayList<String>>> map = new HashMap<>();

	public Health() {

		ArrayList<String> temp1 = new ArrayList<>();
		temp1.add(AlertLevel.NONE.toString());
		temp1.add("NA");

		ArrayList<ArrayList<String>> temp2 = new ArrayList<ArrayList<String>>();
		temp2.add(temp1);

		for (GZSubsystem s : Robot.allSubsystems.getSubsystems())
			map.put(s, temp2);
	}

	public void addAlert(GZSubsystem subsystem, AlertLevel level, String message) {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(level.toString());
		temp.add(message);
		map.get(subsystem).add(temp);
	}

	public void generateHealth() {
		try {
			String htmlString = base_file;
			String body = "", table = "";

			// Find highest alert level per subsystem
			// Loop through each subsystem
			for (GZSubsystem s : Robot.allSubsystems.getSubsystems()) {
				// Loop through all errors
				for (int i = 0; i < map.get(s).size() - 1; i++) {

					// Get alert level of current error
					String alert_level = map.get(s).get(i).get(0);

					if (alert_level.equals(AlertLevel.ERROR.toString()))
						s.setHighestAlert(AlertLevel.ERROR);
					else if (alert_level.equals(AlertLevel.WARNING.toString())
							&& (s.getHighestAlert() != AlertLevel.ERROR))
						s.setHighestAlert(AlertLevel.WARNING);
				}
			}

			// Populate body
			body += header("Health Checker");
			body += paragraph("(File generated on " + Util.dateTime(false) + ")");

			// Print header
			table += tableRow(tableHeader("Subsystem") + tableHeader("Check"));

			// Write table
			for (GZSubsystem s : Robot.allSubsystems.getSubsystems())
				table += tableRow(
						tableCell(s.getClass().getSimpleName() + tableCell("", s.getHighestAlert().toString(), true)));

			//Put table tags around values
			table = table(table);
			
			//Add table to body.
			body += table;

			body += header("Alerts:");

			// Print alerts
			for (GZSubsystem s : Robot.allSubsystems.getSubsystems()) {

				// If errors are present
				if (s.getHighestAlert() != AlertLevel.NONE) {

					// Loop through errors
					for (int i = 0; i < map.get(s).size(); i++) {

						// Store current error
						ArrayList<String> error = map.get(s).get(i);

						// If error = ERROR, do bold and red. If warning, paragraph
						if (error.get(0).equals(AlertLevel.ERROR.toString()))
							body += bold(error.get(1), "red");
						else
							body += paragraph(error.get(1));
					}
				}
			}

			htmlString.replace("$BODY", body);

			try {
				File newHtmlFile = new File("/home/lvuser/Health.html");
				BufferedWriter bw = new BufferedWriter(new FileWriter(newHtmlFile));
				bw.write(htmlString);
				bw.close();
			} catch (Exception e) {
				System.out.println("Could not write health file!");
				e.printStackTrace();
			}
			
		} catch (Exception e) {
			System.out.println("Could not generate health file!");
			e.printStackTrace();
		}
	}

	public String header(String f) {
		return "<h1>" + f + "</h1>";
	}

	public String table(String f) {
		return "<table>" + f + "</table";
	}

	public String paragraph(String f) {
		return "<p>" + f + "</p";
	}

	public String tableHeader(String f) {
		return "<th>" + f + "</th";
	}

	public String tableRow(String f) {
		return "<tr>" + f + "</tr";
	}

	public String tableCell(String f, String color, boolean celll_is_color) {
		String temp;
		if (celll_is_color)
			temp = "<td style=\"background-color:" + color + "; color: " + color + "\">" + f + "</td>";
		else
			temp = "td style=\"background-color:" + color + ";\">" + f + "</td>";
		return temp;
	}

	public String tableCell(String f) {
		return "<td" + f + "</td";
	}

	public String bold(String f, String color) {
		return "<b style=\"color:" + color + "\">" + f + "</b>";
	}

	private String base_file = "<html>\r\n" + "<head>\r\n" + "<style>\r\n" + "table {\r\n"
			+ "    border: 1px solid black;\r\n" + "    border-collapse: collapse;\r\n" + "  	width:50%;\r\n"
			+ "}\r\n" + "  body { \r\n" + "  }\r\n" + "th, td {\r\n" + "  	border: 5px solid black;\r\n"
			+ "    padding: 5px;\r\n" + "    text-align: center;\r\n" + "}\r\n" + "  \r\n" + "</style>\r\n"
			+ "</head>\r\n" + "<body>\r\n" + "\r\n" + "$BODY\r\n" + "\r\n" + "</body>\r\n" + "</html>";

	public enum AlertLevel {
		WARNING("yellow"), ERROR("red"), NONE("green");

		private final String stringValue;

		AlertLevel(final String s) {
			stringValue = s;
		}

		public String toString() {
			return stringValue;
		}
		// further methods, attributes, etc.
	}

}
