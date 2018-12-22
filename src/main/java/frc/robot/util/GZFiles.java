package frc.robot.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Scanner;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import frc.robot.Constants;
import frc.robot.Constants.kFiles;
import frc.robot.subsystems.Drive;

/**
 * <b>Playback subsystem</b> Also used for file writing, logging, etc.
 * 
 * @author max
 * @since 4-18-2018
 *
 */
public class GZFiles {

	public ArrayList<ArrayList<Double>> mpL = new ArrayList<>();
	public ArrayList<ArrayList<Double>> mpR = new ArrayList<>();
	public int mpDur = 0;

	private FileReader fr;
	private Scanner scnr;
	private BufferedWriter bw;

	private String prevLog = "Empty";

	private boolean hasPrintedLogFailed = false;
	private boolean hasPrintedProfileRecordFailed = false;

	private boolean isLogging = false;

	private static GZFiles mInstance = null;

	public static synchronized GZFiles getInstance() {
		if (mInstance == null)
			mInstance = new GZFiles();

		return mInstance;
	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	private GZFiles() {
	}

	private void parseMotionProfileCSV() {
		String st;

		mpL.clear();
		mpR.clear();

		try {
			// Skip first line of text
			scnr.nextLine();

			// take time var
			mpDur = Integer.parseInt(scnr.nextLine().split(",")[0]);

			// loop through each line
			while (scnr.hasNextLine()) {
				ArrayList<Double> temp = new ArrayList<>();

				st = scnr.nextLine();

				String[] ar = st.split(",");

				temp.add(Double.parseDouble(ar[0]));
				temp.add(Double.parseDouble(ar[1]));
				mpL.add(temp);

				temp = new ArrayList<>();

				temp.add(Double.parseDouble(ar[2]));
				temp.add(Double.parseDouble(ar[3]));
				mpR.add(temp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Parse failed!");
		}
	}

	/**
	 * print array
	 * 
	 * @author max
	 */
	@SuppressWarnings("unused")
	private void printListValues() {
		try {
			for (int i = 0; i < mpL.size(); i++) {
				System.out.println(mpL.get(i).get(0) + "\t" + mpL.get(i).get(1) + "\t" + mpR.get(i).get(0) + "\t"
						+ mpR.get(i).get(1) + "\t" + mpDur);
			}

		} catch (Exception e) {
			System.out.println("Printing failed!");
		}
	}

	/**
	 * write time + L and R drivetrain speeds to file
	 * 
	 * @author max
	 * @param isStartup startup
	 */
	private void writeToProfile(boolean isStartup) {
		try {
			if (isStartup) {
				// on startup, write header
				bw.write("leftPos,leftSpeed,rightPos,rightSpeed,");
				bw.write("\r\n");
				bw.write(String.valueOf(Constants.kFiles.RECORDING_MOTION_PROFILE_MS) + ",0,0,0,");
				bw.write("\r\n");
				profileRecord.startPeriodic((double) Constants.kFiles.RECORDING_MOTION_PROFILE_MS / 1000);
			} else {
				profileRecord.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Writing to profile failed!");
		}
	}

	public void parse(String name, Folder folder, boolean usb) {
		csvControl(name, folder, usb, TASK.Parse, true);
		csvControl(name, folder, usb, TASK.Parse, false);
	}

	/**
	 * notifier object for running profile recorder
	 */
	private Notifier profileRecord = new Notifier(new Runnable() {
		@Override
		public void run() {
			try {
				// populate position and speed values
				// WRITE PROFILES IN TICKS,
				double leftPos = Drive.getInstance().getLeftRotations();
				double leftSpeed = Drive.getInstance().getLeftVel();

				double rightPos = Drive.getInstance().getRightRotations();
				double rightSpeed = Drive.getInstance().getRightVel();

				// write values
				bw.write(String.valueOf(leftPos + ","));
				bw.write(String.valueOf(leftSpeed + ","));
				bw.write(String.valueOf(rightPos + ","));
				bw.write(String.valueOf(rightSpeed + ","));
				bw.write("\r\n");

			} catch (Exception e) {
				if (!hasPrintedProfileRecordFailed) {
					System.out.println("Writing to profile failed!");
					hasPrintedProfileRecordFailed = true;
				}
			}
		}
	});

	/**
	 * logging system
	 * 
	 * @author max
	 * @param startup boolean
	 */
	private void writeToLog(boolean startup) {
		try {
			// ON STARTUP, PRINT NAMES
			if (startup) {
				bw.write(GZLog.getInstance().getHeader());
				bw.write("\r\n");
				bw.write(GZLog.getInstance().getFunctions());
				bw.write("\r\n");

				logging.startPeriodic(kFiles.LOGGING_SPEED);
				isLogging = true;
			} else {
				logging.stop();
				isLogging = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Writing to log failed!");
		}
	}

	/**
	 * notifier object for running profile recorder
	 */
	private Notifier logging = new Notifier(new Runnable() {
		@Override
		public void run() {
			try {
				bw.write(GZLog.getInstance().getLog());
				bw.write("\r\n");

			} catch (Exception e) {
				if (!hasPrintedLogFailed) {
					System.out.println("Logging writing failed!");
					hasPrintedLogFailed = true;
				}
			}

		}
	});

	private void createCSVFile(String fileName, Folder folder, fileState readwrite, boolean usb) {
		createCSVFile(fileName, folder, readwrite, usb, false);
	}

	private void createCSVFile(String fileName, Folder folder, fileState readwrite, boolean usb, boolean logging) {
		boolean fileCreateFail = false;

		switch (readwrite) {
		case READ:
			// SET UP FILE READING
			try {
				scnr = new Scanner(new FileReader(GZFileMaker.getFile(fileName, folder, usb, false).getFile()));
			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("File reading failed!");
			}
			break;
		case WRITE:
			// create file writing vars
			try {
				bw = new BufferedWriter(new FileWriter(GZFileMaker.getFile(fileName, folder, usb, true).getFile()));
			} catch (Exception e) {

				// If logging, try again on rio
				if (logging) {
					System.out.println("Writing log to USB failed... trying RIO");
					try {
						bw = new BufferedWriter(
								new FileWriter(GZFileMaker.getFile(fileName, folder, false, true).getFile()));
					} catch (Exception g) {
						fileCreateFail = true;
					}
				} else { // we're not logging and we couldn't make a file
					fileCreateFail = true;
				}
			}
		}

		if (fileCreateFail)
			System.out.println("File creation failed!");
	}

	private void closeCSVFile(fileState readwrite) {
		try {
			switch (readwrite) {
			case READ:
				scnr.close();
				fr.close();
				break;
			case WRITE:
				bw.close();
				break;
			}
		} catch (Exception e) {
			System.out.println("File closing failed!");
		}
	}

	/**
	 * Used to control file writing.
	 * 
	 * @author max
	 * @param name  file name
	 * @param usb   true|false for writing to usb
	 * @param task
	 * @param state
	 * @see TASK
	 * @see STATE
	 */
	public void csvControl(String name, Folder folder, boolean usb, TASK task, boolean startup) {
		if (startup) {
			switch (task) {
			case Record:
				System.out.println("Opening Record: " + name + ".csv");
				createCSVFile(name, folder, fileState.WRITE, usb);
				writeToProfile(true);

				break;

			case Parse:
				System.out.println("Opening Parse: " + name + ".csv");
				createCSVFile(name, folder, fileState.READ, usb);
				parseMotionProfileCSV();
				// printValues();
				break;

			case Log:

				if (isLogging == false) {
					System.out.println("Opening Log: " + loggingName(true) + ".csv");
					createCSVFile(loggingName(false), folder, fileState.WRITE, usb, true);
					writeToLog(true);
				}

				break;
			}
		} else { // not startup
			switch (task) {
			case Record:
				System.out.println("Closing " + task + ": " + name + ".csv");
				writeToProfile(false);
				closeCSVFile(fileState.WRITE);
				break;
			case Parse:
				System.out.println("Closing " + task + ": " + name + ".csv");
				closeCSVFile(fileState.READ);
				break;
			case Log:
				if (isLogging) {
					System.out.println("Closing " + task + ": " + loggingName(false) + ".csv");
					writeToLog(false);
					closeCSVFile(fileState.WRITE);
				}
				break;
			}
		}
	}

	public static class Folder {
		private String mFolderOnRIO;
		private String mFolderOnUSB;

		public Folder(String rio, String usb) {
			this.mFolderOnRIO = rio;
			this.mFolderOnUSB = usb;
		}

		public Folder(String rio) {
			this(rio, "");
			this.usbIsRIOInFolder("3452");
		}

		private void usbIsRIOInFolder(String folder) {
			this.mFolderOnUSB = folder + "/" + this.mFolderOnRIO;
		}

		private String getRIOFolder() {
			return mFolderOnRIO;
		}

		private String getUSBFolder() {
			return mFolderOnUSB;
		}

		public String get(boolean usb) {
			return (usb ? getUSBFolder() : getRIOFolder());
		}
	}

	public static void copyFile(File source, File dest) throws IOException {
		Files.deleteIfExists(dest.toPath());
		Files.copy(source.toPath(), dest.toPath());
	}

	private String loggingName(boolean returnCurrent) {
		if (returnCurrent) {
			String retval = (DriverStation.getInstance().isFMSAttached() ? "FIELD_" : "") + GZUtil.dateTime(false);
			prevLog = retval;
			return retval;
		} else {
			return prevLog;
		}
	}

	public static class HTML {

		public static String header(String f, int headernumber, String color) {
			return newLine(
					"<h" + headernumber + " style=\"color:" + color + "\"" + ">" + f + "</h" + headernumber + ">");
		}

		public static String button(String buttonTitle, String content) {
			return newLine("<button class=\"collapsible\">" + buttonTitle + "</button> <div class=\"content\">"
					+ content + "</div>");
		}

		public static String header(String f) {
			return header(f, 1, "black");
		}

		public static String table(String f) {
			return newLine("<table>" + "<tbody>" + f + "</tbody>"+  "</table>");
		}

		public static String paragraph(String f) {
			return newLine("<p>" + f + "</p>");
		}

		public static String tableHeader(String f) {
			return newLine("<th>" + f + "</th>");
		}

		public static String newLine(String f) {
			return f + "\n";
		}

		public static String tableRow(String f) {
			return newLine("<tr>" + f + "</tr>");
		}

		public static String tableCell(String f, String color, boolean cell_is_color) {
			String temp;
			if (cell_is_color)
				temp = "<td style=\"background-color:" + color + "; color: " + color + "\">" + f + "</td>";
			else
				temp = "<td style=\"background-color:" + color + ";\">" + f + "</td>";
			return temp;
		}

		public static String tableCell(String f) {
			return "<td>" + f + "</td>";
		}

		public static String bold(String f, String color) {
			return "<b style=\"color:" + color + "\">" + f + "</b>";
		}

		public static final String BASE_HTML_FILE = "<html>" + "<head>" + "<style>"
				+ "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">" + ".collapsible {"
				+ "background-color: #777;" + "color: white;" + "cursor: pointer;" + "padding: 18px;" + "width: 100%;"
				+ "border: none;" + "text-align: left;" + "outline: none;" + "font-size: 15px;" + "}"
				+ ".active, .collapsible:hover {" + "background-color: #555;" + "}" +

				".content {" + "padding: 0 18px;" + "max-height: 0;" + "overflow: hidden;"
				+ "transition: max-height 0.2s ease-out;" + "background-color: #f1f1f1;" + "}" +

				"table {" + "border: 1px solid black;" + "border-collapse: collapse;" + "width: \"device-width\";" + "}"
				+ "body {} th, td {" + "border: 5px solid black;" + "padding: 5px;" + "text-align: center;" + "}"
				+ "</style>" + "</head>" + "<body>" + "$BODY\r\n" + "\r\n" + "<script>"
				+ "var coll = document.getElementsByClassName(\"collapsible\");" + "var i;" +

				"for (i = 0; i < coll.length; i++) {" + "coll[i].addEventListener(\"click\", function() {"
				+ "this.classList.toggle(\"active\");" + "var content = this.nextElementSibling;"
				+ "if (content.style.maxHeight){" + "content.style.maxHeight = null;" + "} else {"
				+ "content.style.maxHeight = content.scrollHeight + \"px\";" + "}" + "});" + "}" + "</script>" +

				"</body>" + "</html>";

		public static final String OLD_BASE_HTML_FILE = "<html>\r\n" + "<head>\r\n" + "<style>\r\n" + "table {\r\n"
				+ "    border: 1px solid black;\r\n" + "    border-collapse: collapse;\r\n" + "  	width:50%;\r\n"
				+ "}\r\n" + "  body { \r\n" + "  }\r\n" + "th, td {\r\n" + "  	border: 5px solid black;\r\n"
				+ "    padding: 5px;\r\n" + "    text-align: center;\r\n" + "}\r\n" + "  \r\n" + "</style>\r\n"
				+ "</head>\r\n" + "<body>\r\n" + "\r\n" + "$BODY\r\n" + "\r\n" + "</body>\r\n" + "</html>";

		public static void createHTMLFile(GZFile g, String body) {
			createHTMLFile(g.getFile(), body);
		}

		public static void createHTMLFile(File f, String body) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(f));
				String html = BASE_HTML_FILE.replace("$BODY", body);
				bw.write(html);
				bw.close();
			} catch (Exception e) {
				System.out.println("Could not make HTML File at " + f.getPath());
			}
		}

	}

	/**
	 * reading or writing
	 * 
	 * @author max
	 * 
	 */
	public enum fileState {
		READ, WRITE
	}

	/**
	 * record, log, parse, play
	 * 
	 * @author max
	 */
	public enum TASK {
		Record, Log, Parse
	}
}
