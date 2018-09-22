package frc.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import frc.robot.Constants;
import frc.robot.Constants.kFileManagement;
import frc.robot.Robot;
import frc.robot.util.GZLog;
import frc.robot.util.Util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

/**
 * <b>Playback subsystem</b> Also used for file writing, logging, etc.
 * 
 * @author max
 * @since 4-18-2018
 *
 */
public class Files {

	public ArrayList<ArrayList<Double>> mpL = new ArrayList<>();
	public ArrayList<ArrayList<Double>> mpR = new ArrayList<>();
	public int mpDur = 0;

	private FileReader fr;
	private Scanner scnr;
	private BufferedWriter bw;

	public GZLog mLog;

	/**
	 * Time string converted to numbers for parsing
	 */
	private String prevLog = "Empty";

	private boolean hasPrintedLogFailed = false;
	private boolean hasPrintedProfileRecordFailed = false;

	private boolean isLogging = false;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Files() {
		mLog = new GZLog();
	}
	
	public void fillLogger()
	{
		mLog.fillLogger();
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
				bw.write(String.valueOf(Constants.kFileManagement.RECORDING_MOTION_PROFILE_MS) + ",0,0,0,");
				bw.write("\r\n");
				profileRecord.startPeriodic((double) Constants.kFileManagement.RECORDING_MOTION_PROFILE_MS / 1000);
			} else {
				profileRecord.stop();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Writing to profile failed!");
		}
	}

	/**
	 * profile recording runnable
	 * 
	 * @author Max
	 * @since 5/22/2018
	 */
	private class profileRecordRunnable implements java.lang.Runnable {
		@Override
		public void run() {
			try {
				// populate position and speed values
				// WRITE PROFILES IN TICKS,
				double leftPos = 0;
				double leftSpeed = 0;

				double rightPos = 0;
				double rightSpeed = 0;

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
	}

	/**
	 * notifier object for running profile recorder
	 */
	private Notifier profileRecord = new Notifier(new profileRecordRunnable());

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
				bw.write(mLog.getHeader());
				bw.write("\r\n");
				logging.startPeriodic(kFileManagement.LOGGING_SPEED);
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
	 * logging runnable
	 * 
	 * @author Max
	 * @since 5/22/2018
	 */
	private class loggingRunnable implements java.lang.Runnable {
		@Override
		public void run() {
			try {
				bw.write(mLog.getLog());
				bw.write("\r\n");

			} catch (Exception e) {
				if (!hasPrintedLogFailed) {
					System.out.println("Logging writing failed!");
					hasPrintedLogFailed = true;
				}
			}

		}
	}

	/**
	 * notifier object for running profile recorder
	 */
	private Notifier logging = new Notifier(new loggingRunnable());

	private void createCSVFile(String fileName, String folder, fileState readwrite, boolean usb) {
		try {
			switch (readwrite) {
			case READ:
				// SET UP FILE READING
				File f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");

				fr = new FileReader(f);
				scnr = new Scanner(fr);

				break;
			case WRITE:
				new File(((usb) ? "/u/" : "/home/lvuser") + folder).mkdirs();

				// SETUP FILE WRITING
				f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder);
				f.mkdirs();
				f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");

				// if it isn't there, create it
				if (!f.exists())
					f.createNewFile();

				// create file writing vars
				bw = new BufferedWriter(new FileWriter(f));
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("File " + readwrite + " from " + (usb ? "USB" : "RIO") + "failed!");
		}
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
	public void csvControl(String name, String folder, boolean usb, TASK task, boolean startup) {
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
					createCSVFile(loggingName(false), folder, fileState.WRITE, usb);
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

	private String loggingName(boolean returnCurrent) {
		if (returnCurrent) {
			String retval = (DriverStation.getInstance().isFMSAttached() ? "FIELD_" : "") + Util.dateTime(false);
			prevLog = retval;
			return retval;
		} else {
			return prevLog;
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
