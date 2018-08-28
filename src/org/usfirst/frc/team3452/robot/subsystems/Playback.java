package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.Constants.kPlayback;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.util.Util;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Notifier;
import edu.wpi.first.wpilibj.RobotController;

/**
 * <b>Playback subsystem</b> Also used for file writing, logging, etc.
 * 
 * @author max
 * @since 4-18-2018
 *
 */
public class Playback {

	public ArrayList<ArrayList<Double>> mpL = new ArrayList<>();
	public ArrayList<ArrayList<Double>> mpR = new ArrayList<>();
	public int mpDur = 0;

	private FileWriter fw;

	private FileReader fr;
	private Scanner scnr;
	private BufferedWriter bw;

	/**
	 * Time string converted to numbers for parsing
	 */
	private String prevLog = "Empty";

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
	 * @param isStartup startup
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
				// WRITE PROFILES IN TICKS,
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
	 * @param startup boolean
	 */
	private void writeToLog(boolean startup) {
		try {
			// ON STARTUP, PRINT NAMES
			if (startup) {
				// TODO ISSUE #13

				// 1st column is time
				String header = Util.dateTime(false);

				header += getLogs(true);

				bw.write(header);
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
		@Override
		public void run() {
			try {
				//ISSUE #13
				bw.write(Util.dateTime(true) + getLogs(false));
				bw.write("\r\n");

			} catch (Exception e) {
				if (!hasPrintedLogFailed) {
					System.out.println("Logging writing failed!");
					hasPrintedLogFailed = true;
				}
			}

		}
	}

	private synchronized String getLogs(boolean header) {
		String[] left_speed = { "L-RPM", Robot.drive.mIO.left_encoder_speed.toString() };
		String[] right_speed = { "R-RPM", Robot.drive.mIO.right_encoder_speed.toString() };
		String[] l1_amp = { "L1-AMP", Robot.drive.mIO.L1_amp.toString() };
		String[] l2_amp = { "L2-AMP", Robot.drive.mIO.L2_amp.toString() };
		String[] l3_amp = { "L3-AMP", Robot.drive.mIO.L3_amp.toString() };
		String[] l4_amp = { "L4-AMP", Robot.drive.mIO.L4_amp.toString() };
		String[] r1_amp = { "R1-AMP", Robot.drive.mIO.R1_amp.toString() };
		String[] r2_amp = { "R2-AMP", Robot.drive.mIO.R2_amp.toString() };
		String[] r3_amp = { "R3-AMP", Robot.drive.mIO.R3_amp.toString() };
		String[] r4_amp = { "R4-AMP", Robot.drive.mIO.R4_amp.toString() };
		String[] l1_volt = { "L1-VOLT", Robot.drive.mIO.L1_volt.toString() };
		String[] l2_volt = { "L2-VOLT", Robot.drive.mIO.L2_volt.toString() };
		String[] l3_volt = { "L3-VOLT", Robot.drive.mIO.L3_volt.toString() };
		String[] l4_volt = { "L4-VOLT", Robot.drive.mIO.L4_volt.toString() };
		String[] r1_volt = { "R1-VOLT", Robot.drive.mIO.R1_volt.toString() };
		String[] r2_volt = { "R2-VOLT", Robot.drive.mIO.R2_volt.toString() };
		String[] r3_volt = { "R3-VOLT", Robot.drive.mIO.R3_volt.toString() };
		String[] r4_volt = { "R4-VOLT", Robot.drive.mIO.R4_volt.toString() };
		String[] elev_1_amp = { "ELEV-1-AMP", Robot.elevator.mIO.elevator_1_amp.toString() };
		String[] elev_2_amp = { "ELEV-2-AMP", Robot.elevator.mIO.elevator_2_amp.toString() };
		String[] elev_1_volt = { "ELEV-1-VOLT", Robot.elevator.mIO.elevator_1_volt.toString() };
		String[] elev_2_volt = { "ELEV-2-VOLT", Robot.elevator.mIO.elevator_2_volt.toString() };
		String[] elev_fwd_limit = { "ELEV-FWD-LMT", Robot.elevator.mIO.elevator_fwd_lmt.toString() };
		String[] elev_rev_limit = { "ELEV-REV-LMT", Robot.elevator.mIO.elevator_rev_lmt.toString() };
		String[] elev_height = { "ELEV-HEIGHT", Robot.elevator.mIO.encoder_rotations.toString() };
		String[] elev_speed = { "ELEV-SPEED", Robot.elevator.mIO.encoder_speed.toString() };
		String[] intake_l_amp = { "INTAKE-L-AMP", Robot.drive.getPDPChannelCurrent(kPDP.INTAKE_L).toString() };
		String[] intake_r_amp = { "INTAKE-R-AMP", Robot.drive.getPDPChannelCurrent(kPDP.INTAKE_R).toString() };
		String[] climb_1_amp = { "CLIMBER-1-AMP", Robot.drive.getPDPChannelCurrent(kPDP.CLIMBER_1).toString() };
		String[] climb_2_amp = { "CLIMBER-2-AMP", Robot.drive.getPDPChannelCurrent(kPDP.CLIMBER_2).toString() };
		String[] battery_voltage = { "BATTERY-VOLTAGE", String.valueOf(RobotController.getBatteryVoltage()) };
		String[] pdp_temp = { "PDP-TEMP", Robot.drive.getPDPTemperature().toString() };
		String[] pdp_current = { "PDP-CURRENT", Robot.drive.getPDPTotalCurrent().toString() };
		String[] pdp_volt = { "PDP-VOLT", Robot.drive.getPDPVoltage().toString() };
		String[] state_drive = { "DRIVE-STATE", Robot.drive.isDisabed() + "-" + Robot.drive.getStateString() };
		String[] state_elevator = { "ELEV-STATE", Robot.elevator.isDisabed() + "-" + Robot.elevator.getStateString() };
		String[] state_intake = { "INTAKE-STATE", Robot.intake.isDisabed() + "-" + Robot.intake.getStateString() };
		String[] state_climber = { "CLIMBER-STATE", Robot.climber.isDisabed() + "-" + Robot.climber.getStateString() };

		int i = header ? 0 : 1;
		List<String> values = Arrays.asList(left_speed[i], right_speed[i], l1_amp[i], l2_amp[i], l3_amp[i], l4_amp[i],
				r1_amp[i], r2_amp[i], r3_amp[i], r4_amp[i], l1_volt[i], l2_volt[i], l3_volt[i], l4_volt[i], r1_volt[i],
				r2_volt[i], r3_volt[i], r4_volt[i], elev_1_amp[i], elev_2_amp[i], elev_1_volt[i], elev_2_volt[i],
				elev_fwd_limit[i], elev_rev_limit[i], elev_height[i], elev_speed[i], intake_l_amp[i], intake_r_amp[i],
				climb_1_amp[i], climb_2_amp[i], battery_voltage[i], pdp_temp[i], pdp_current[i], pdp_volt[i],
				state_drive[i], state_elevator[i], state_intake[i], state_climber[i]);

		String retval = "";
		for (String valueToAdd : values) {
			retval += valueToAdd;
			retval += ",";
		}

		return retval;
	}

	/**
	 * notifier object for running profile recorder
	 */
	private Notifier logging = new Notifier(new loggingRunnable());

	/**
	 * @author max
	 * @param fileName  String
	 * @param readwrite fileState
	 * @param usb       boolean
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
	 * @param readwrite fileState
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
	 * @param name  file name
	 * @param usb   true|false for writing to usb
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