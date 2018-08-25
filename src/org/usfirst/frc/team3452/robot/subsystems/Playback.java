package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kPlayback;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.Util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;

/**
 * <b>Playback subsystem</b> Also used for file writing, logging, etc.
 * 
 * @author max
 * @since 4-18-2018
 *
 */
public class Playback {
	/**
	 * variable for storing left values for motion profile
	 */
	public ArrayList<ArrayList<Double>> mpL = new ArrayList<>();
	private FileWriter fw;

	private FileReader fr;
	private Scanner scnr;
	public ArrayList<ArrayList<Double>> mpR = new ArrayList<>();
	int mpDur = 0;
	private BufferedWriter bw;

	/**
	 * Time string converted to numbers for parsing
	 */
	private String prevDateTimeString = "Empty";

	private boolean hasPrintedLogFailed = false;
	private boolean hasPrintedProfileRecordFailed = false;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Playback() {
	}

	/**
	 * parse file and convert to array
	 * 
	 * @author max
	 */
	private void parseFile() {
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
	private void printValues() {
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
	 * @param isStartup
	 *            startup
	 */
	private void writeToProfile(boolean isStartup) {
		try {
			if (isStartup) {
				// on startup, write header
				bw.write("leftPos,leftSpeed,rightPos,rightSpeed,");
				bw.write("\r\n");
				bw.write(String.valueOf(Constants.kPlayback.RECORDING_MOTION_PROFILE_MS) + ",0,0,0,");
				bw.write("\r\n");
				profileRecord.startPeriodic((double) Constants.kPlayback.RECORDING_MOTION_PROFILE_MS / 1000);
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
				//WRITE PROFILES IN TICKS, 
				double leftPos = Robot.drive.mIO.left_encoder_rotations;
				double leftSpeed = Robot.drive.mIO.left_encoder_speed;

				double rightPos = -Robot.drive.mIO.right_encoder_rotations;
				double rightSpeed = -Robot.drive.mIO.right_encoder_speed;
				
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
	 * @param startup
	 *            boolean
	 */
	private void writeToLog(boolean startup) {
		try {
			// ON STARTUP, PRINT NAMES
			if (startup) {
				bw.write(Util.dateTime(false) + "," + "L-RPM,R-RPM," + "L1-AMP,L2-AMP,L3-AMP,L4-AMP,"
						+ "L1-V,L2-V,L3-V,L4-V," + "R1-AMP,R2-AMP,R3-AMP,R4-AMP," + "R1-V,R2-V,R3-V,R4-V,"
						+ "Elev_1-AMP,Elev_2-AMP," + "Elev_1-V,Elev_2-V," + "Intake_L-AMP,Intake_R-AMP,"
						+ "Climber_1-AMP,Climber_2-AMP," + "BATTERY");
				bw.write("\r\n");
				logging.startPeriodic(kPlayback.LOGGING_SPEED);
			} else {
				logging.stop();
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
		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			try {
				bw.write(Util.dateTime(true) + ",");
				bw.write(String.valueOf(

						// SPEED
						Robot.drive.mIO.left_encoder_rotations + ","
								+ Robot.drive.mIO.right_encoder_rotations + "," +

								// LEFT CURRENT
								Robot.drive.mIO.L1_amp + "," + Robot.drive.mIO.L2_amp + ","
								+ Robot.drive.mIO.L3_amp + "," + Robot.drive.mIO.L4_amp + ","

								// LEFT VOLTAGE
								+ Robot.drive.mIO.L1_volt + "," + Robot.drive.mIO.L2_volt
								+ "," + Robot.drive.mIO.L3_volt + ","
								+ Robot.drive.mIO.L4_volt + ","

								// RIGHT CURRENT
								+ Robot.drive.mIO.R1_amp + "," + Robot.drive.mIO.R2_amp + ","
								+ Robot.drive.mIO.R3_amp + "," + Robot.drive.mIO.R4_amp + ","

								// RIGHT VOLTAGE
								+ Robot.drive.mIO.R1_volt + "," + Robot.drive.mIO.R2_volt
								+ "," + Robot.drive.mIO.R3_volt + ","
								+ Robot.drive.mIO.R4_volt + ","

								// ELEVATOR CURRENT
								+ Robot.elevator.mIO.elevator_1_amp + ","
								+ Robot.elevator.mIO.elevator_2_amp + ","

								// ELEVATOR VOLTAGE
								+ Robot.elevator.mIO.elevator_1_volt + ","
								+ Robot.elevator.mIO.elevator_2_volt + ","

								// INTAKE PDP SLOTS
								+ Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_L) + ","
								+ Robot.drive.getPDPChannelCurrent(Constants.kPDP.INTAKE_R) + ","

								// CLIMBER PDP SLOTS
								+ Robot.drive.getPDPChannelCurrent(Constants.kPDP.CLIMBER_1) + ","
								+ Robot.drive.getPDPChannelCurrent(Constants.kPDP.CLIMBER_2) + ","

								// BATTERY
								+ DriverStation.getInstance().getBatteryVoltage()));
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

	/**
	 * @author max
	 * @param fileName
	 *            String
	 * @param readwrite
	 *            fileState
	 * @param usb
	 *            boolean
	 */
	private void createFile(String fileName, String folder, fileState readwrite, boolean usb) {
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
				fw = new FileWriter(f);
				bw = new BufferedWriter(fw);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("File " + readwrite + " from " + (usb ? "USB" : "RIO") + "failed!");
		}
	}

	/**
	 * @author max
	 * @param readwrite
	 *            fileState
	 */
	private void closeFile(fileState readwrite) {
		try {
			switch (readwrite) {
			case READ:
				// CLOSE READING
				scnr.close();
				fr.close();
				break;
			case WRITE:
				// CLOSE WRITING
				bw.close();
				fw.close();
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
	 * @param name
	 *            file name
	 * @param usb
	 *            true|false for writing to usb
	 * @param task
	 * @param state
	 * @see TASK
	 * @see STATE
	 */
	public void control(String name, String folder, boolean usb, TASK task, STATE state) {
		switch (state) {
		case STARTUP:
			switch (task) {
			case Record:
				System.out.println("Opening Record: " + name + ".csv");
				createFile(name, folder, fileState.WRITE, usb);
				writeToProfile(true);
				
				break;

			case Parse:
				System.out.println("Opening Parse: " + name + ".csv");
				createFile(name, folder, fileState.READ, usb);
				parseFile();
				// printValues();
				break;

			case Log:
				System.out.println("Opening Log: " + loggingName(true) + ".csv");
				createFile(loggingName(false), folder, fileState.WRITE, usb);
				writeToLog(true);

				break;
			}
			break;
		case RUNTIME:
			switch (task) {
			case Record:
				break;
			case Parse:
				break;
			case Log:
				break;
			}
			break;
		case FINISH:
			switch (task) {
			case Record:
				System.out.println("Closing " + task + ": " + name + ".csv");
				writeToProfile(false);
				closeFile(fileState.WRITE);
				break;
			case Parse:
				System.out.println("Closing " + task + ": " + name + ".csv");
				closeFile(fileState.READ);
				break;
			case Log:
				System.out.println("Closing " + task + ": " + loggingName(false) + ".csv");
				writeToLog(false);
				closeFile(fileState.WRITE);
				break;
			}
			break;
		}
	}

	private String loggingName(boolean returnCurrent) {
		if (returnCurrent) {
			String retval = (DriverStation.getInstance().isFMSAttached() ? "FIELD_" : "") + Util.dateTime(false);
			prevDateTimeString = retval;
			return retval;
		} else {
			return prevDateTimeString;
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
	 * startup, runtime, finish
	 * 
	 * @author max
	 */
	public enum STATE {
		STARTUP, RUNTIME, FINISH
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