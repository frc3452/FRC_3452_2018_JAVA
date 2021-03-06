package frc.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc.robot.util.GZSubsystem;
import frc.robot.util.GZUtil;

public class Health {

	private Map<GZSubsystem, ArrayList<ArrayList<String>>> map = new HashMap<>();

	private static Health mInstance = null;
	
	private List<GZSubsystem> mSubsystems;

	public void assignSubsystems(List<GZSubsystem> list)
	{
		this.mSubsystems = list;
	}

	private Health() {
	}

	public synchronized static Health getInstance() {
		if (mInstance == null)
			mInstance = new Health();
		return mInstance;
	}

	public void addAlert(GZSubsystem subsystem, AlertLevel level, String message) {
		ArrayList<String> temp = new ArrayList<>();
		temp.add(level.stringValue);
		temp.add(message);

		if (!(map.containsKey(subsystem))) {
			ArrayList<String> temp1 = new ArrayList<>();
			temp1.add(AlertLevel.NONE.stringValue);
			temp1.add("NA");

			ArrayList<ArrayList<String>> temp2 = new ArrayList<ArrayList<String>>();
			temp2.add(temp1);
			map.put(subsystem, temp2);
		}

		map.get(subsystem).add(temp);
	}

	public void generateHealth() {
		try {
			String htmlString = base_file;
			String body = "", table = "";

			//Make sure map has every subsystem in it
			for (GZSubsystem s : mSubsystems)
				this.addAlert(s, AlertLevel.NONE, "NA");

			// Find highest alert level per subsystem
			// Loop through each subsystem
			for (GZSubsystem s : mSubsystems) {
				// Loop through all errors

				for (int i = 0; i < map.get(s).size(); i++) {

					// Get alert level of current error
					String alert_level = map.get(s).get(i).get(0);

					if (alert_level.equals(AlertLevel.ERROR.stringValue))
						s.setHighestAlert(AlertLevel.ERROR);
					else if (alert_level.equals(AlertLevel.WARNING.stringValue)
							&& (s.getHighestAlert() != AlertLevel.ERROR))
						s.setHighestAlert(AlertLevel.WARNING);
				}
			}

			// Populate body
			body += header("Health Checker");
			body += paragraph("(File generated on " + GZUtil.dateTime(false) + ")");

			// Print header
			table += tableRow(tableHeader("Subsystem") + tableHeader("Check"));

			// Write table
			for (GZSubsystem s : mSubsystems)
				table += tableRow(
						tableCell(s.getClass().getSimpleName()) + tableCell("", s.getHighestAlert().stringValue, true));

			// Put table tags around values
			table = table(table);

			// Add table to body.
			body += table;

			body += header("Alerts:");

			// Print alerts
			for (GZSubsystem s : mSubsystems) {

				// If errors are present
				if (s.getHighestAlert() != AlertLevel.NONE) {

					// Print subsystem name
					body += header(s.getClass().getSimpleName(), 3);

					// Loop through errors twice, once for errors and once for warnings
					// This prints errors first, then warnings
					for (int errrorLoop = 0; errrorLoop < 2; errrorLoop++)
						for (int allErrorsForSubsystem = 0; allErrorsForSubsystem < map.get(s)
								.size(); allErrorsForSubsystem++) {

							// Store current error
							ArrayList<String> error = map.get(s).get(allErrorsForSubsystem);

							switch (errrorLoop) {
							case 0:
								if (error.get(0).equals(AlertLevel.ERROR.stringValue))
									body += paragraph(bold(error.get(1), "red"));
								break;
							case 1:
								if (error.get(0).equals(AlertLevel.WARNING.stringValue))
									body += paragraph(error.get(1));
								break;
							}
						}
				}
			}

			htmlString = htmlString.replace("$BODY", body);

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

	public String header(String f, int headernumber) {
		return newLine("<h" + headernumber + ">" + f + "</h" + headernumber + ">");
	}

	public String header(String f) {
		return header(f, 1);
	}

	public String table(String f) {
		return newLine("<table>" + f + "</table>");
	}

	public String paragraph(String f) {
		return newLine("<p>" + f + "</p>");
	}

	public String tableHeader(String f) {
		return newLine("<th>" + f + "</th>");
	}

	public String newLine(String f) {
		return f + "\n";
	}

	public String tableRow(String f) {
		return newLine("<tr>" + f + "</tr>");
	}

	public String tableCell(String f, String color, boolean celll_is_color) {
		String temp;
		if (celll_is_color)
			temp = "<td style=\"background-color:" + color + "; color: " + color + "\">" + f + "</td>";
		else
			temp = "<td style=\"background-color:" + color + ";\">" + f + "</td>";
		return temp;
	}

	public String tableCell(String f) {
		return "<td>" + f + "</td>";
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

		// further methods, attributes, etc.
	}

}
