package frc.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frc.robot.util.GZFileMaker;
import frc.robot.util.GZFiles;
import frc.robot.util.GZSubsystem;
import frc.robot.util.GZUtil;
import frc.robot.util.GZFileMaker.ValidFileExtensions;

public class Health {

	private Map<GZSubsystem, ArrayList<ArrayList<String>>> map = new HashMap<>();

	private static Health mInstance = null;

	private List<GZSubsystem> mSubsystems;

	public void assignSubsystems(List<GZSubsystem> list) {
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
			String body = "", table = "";

			// Make sure map has every subsystem in it
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
			body += GZFiles.header("Health Checker");
			body += GZFiles.paragraph("(File generated on " + GZUtil.dateTime(false) + ")");

			// Print header
			table += GZFiles.tableRow(GZFiles.tableHeader("Subsystem") + GZFiles.tableHeader("Check"));

			// Write table
			for (GZSubsystem s : mSubsystems)
				table += GZFiles.tableRow(GZFiles.tableCell(s.getClass().getSimpleName())
						+ GZFiles.tableCell("", s.getHighestAlert().stringValue, true));

			// Put table tags around values
			table = GZFiles.table(table);

			// Add table to body.
			body += table;

			body += GZFiles.header("Alerts:");

			// Print alerts
			for (GZSubsystem s : mSubsystems) {

				// If errors are present
				if (s.getHighestAlert() != AlertLevel.NONE) {

					// Print subsystem name
					body += GZFiles.header(s.getClass().getSimpleName(), 3);

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
									body += GZFiles.paragraph(GZFiles.bold(error.get(1), "red"));
								break;
							case 1:
								if (error.get(0).equals(AlertLevel.WARNING.stringValue))
									body += GZFiles.paragraph(error.get(1));
								break;
							}
						}
				}
			}

			try {
				File htmlFile = GZFileMaker.getFile("Health", "", ValidFileExtensions.HTML, false, true);
				GZFiles.createHTMLFile(htmlFile, body);
			} catch (Exception e) {
				System.out.println("Could not write health file!");
				// e.printStackTrace();
			}

		} catch (Exception e) {
			System.out.println("Could not generate health file!");
			e.printStackTrace();
		}
	}

	public enum AlertLevel {
		WARNING("yellow"), ERROR("red"), NONE("green");

		private final String stringValue;

		AlertLevel(final String s) {
			stringValue = s;
		}

		// further methods, attributes, etc.
	}

}
