package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.PDP;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <b>Playback subsystem</b> Also used for file writing, logging, etc.
 * 
 * @author max
 * @since 4-18-2018
 *
 */
public class Playback extends Subsystem {

	private File f;
	private BufferedWriter bw;
	private FileWriter fw;
	
	private FileReader fr;
	private Scanner br;

	//POSITION, VEL, DURATION
	/**
	 * variable for storing left position for motion profile
	 */
	public ArrayList<Double> mpLP = new ArrayList<Double>();
	/**
	 * variable for storing left speed for motion profile
	 */
	public ArrayList<Double> mpLS = new ArrayList<Double>();
	
	/**
	 * variable for storing right position for motion profile
	 */
	public ArrayList<Double> mpRP = new ArrayList<Double>();
	
	/**
	 * variable for storing right speed for motion profile
	 */
	public ArrayList<Double> mpRS = new ArrayList<Double>();

	public int mpDur;

	/**
	 * Time string converted to numbers for parsing
	 */
	private double n_timeString = 0, p_timeString = 0;
	private String prevDateTimeString = "";

	private boolean hasPrintedLogFailed = false;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public void initHardware() {
	}

	/**
	 * parse file and convert to array
	 * 
	 * @author max
	 * @since
	 */
	private void parseFile() {
		String st;
		try {
			//Skip first line of text
			br.nextLine();
			//take time var
			mpDur = Integer.parseInt(br.nextLine().split(",")[0]);

			//loop through each line
			while (br.hasNextLine()) {
				st = br.nextLine();

				String[] ar = st.split(",");

				mpLP.add(Double.parseDouble(ar[0]));
				mpLS.add(Double.parseDouble(ar[1]));
				mpRP.add(Double.parseDouble(ar[2]));
				mpRS.add(Double.parseDouble(ar[3]));
			}
		} catch (Exception e) { e.printStackTrace();
			System.out.println("Parse failed!");
		}
	}

	/**
	 * print array
	 * 
	 * @author max
	 * @since
	 */
	@SuppressWarnings("unused")
	private void printValues() {
		try {

			for (int i = 0; i < mpLP.size(); i++)
				System.out.println(
						mpLP.get(i) + "\t" + mpLS.get(i) + "\t" + mpRP.get(i) + "\t" + mpRS.get(i) + "\t" + mpDur);

		} catch (Exception e) {
			System.out.println("Printing failed!");
		}
	}

	/**
	 * write time + L&R drivetrain speeds to file
	 * 
	 * @author max
	 * @param boolean startup
	 * @since
	 */
	private void writeToProfile(boolean startup) {
		try {

			String timeString = String.format("%8s", roundToFraction(Robot.drive.timer.get(), 50));
			timeString = timeString.replace(' ', '0');
			n_timeString = Double.valueOf(timeString);

			//ONLY PRINT EVERY ROUNDING
			if (n_timeString != p_timeString) {
				if (!startup) {

					double leftPos = Robot.drive.L1.getSelectedSensorPosition(0);
					double leftSpeed = Robot.drive.L1.getSelectedSensorVelocity(0);

					double rightPos = Robot.drive.R1.getSelectedSensorPosition(0);
					double rightSpeed = Robot.drive.R1.getSelectedSensorVelocity(0);

					bw.write(String.valueOf(leftPos + ","));
					bw.write(String.valueOf(leftSpeed + ","));
					bw.write(String.valueOf(rightPos + ","));
					bw.write(String.valueOf(rightSpeed + ","));
					bw.write("\r\n");

				} else {
					bw.write("leftPos,leftSpeed,rightPos,rightSpeed,");
					bw.write("\r\n");
					bw.write("20,0,0,0,");
					bw.write("\r\n");
				}
			} else {
			}

			//Prevent duplicates
			p_timeString = n_timeString;

		} catch (Exception e) {
			//			e.printStackTrace();
			System.out.println("Mapping failed!");
		}
	}

	/**
	 * logging system
	 * 
	 * @author max
	 * @param startup
	 * @since
	 */
	@SuppressWarnings("deprecation")
	private void writeToLog(boolean startup) {
		try {

			//ON STARTUP, PRINT NAMES
			if (startup) {
				bw.write(dateTime(false) + "," + "L-RPM,R-RPM," + "L1-AMP,L2-AMP,L3-AMP,L4-AMP,"
						+ "L1-V,L2-V,L3-V,L4-V," + "R1-AMP,R2-AMP,R3-AMP,R4-AMP," + "R1-V,R2-V,R3-V,R4-V,"
						+ "Elev_1-AMP,Elev_2-AMP," + "Elev_1-V,Elev_2-V," + "Intake_L-AMP,Intake_R-AMP,"
						+ "Climber_1-AMP,Climber_2-AMP," + "BATTERY");
				bw.write("\r\n");

			} else {

				//TIME VALUE (ROUNDED)
				String timeString = String.format("%8s", roundToFraction(Robot.drive.timer.get(), 20));
				timeString = timeString.replace(' ', '0');

				n_timeString = Double.valueOf(timeString);

				//ONLY PRINT EVERY ROUNDING
				if (n_timeString != p_timeString) {

					//PRINT VALUES
					bw.write(timeString + ",");
					bw.write(String.valueOf(

							//SPEED
							((double) Robot.drive.L1.getSelectedSensorVelocity(0) / 4096) + ","
									+ ((double) -Robot.drive.R1.getSelectedSensorVelocity(0) / 4096) + "," +

									//LEFT CURRENT
									Robot.drive.L1.getOutputCurrent() + "," + Robot.drive.L2.getOutputCurrent() + ","
									+ Robot.drive.L3.getOutputCurrent() + "," + Robot.drive.L4.getOutputCurrent() + ","

									//LEFT VOLTAGE
									+ Robot.drive.L1.getMotorOutputVoltage() + ","
									+ Robot.drive.L2.getMotorOutputVoltage() + ","
									+ Robot.drive.L3.getMotorOutputVoltage() + ","
									+ Robot.drive.L4.getMotorOutputVoltage() + ","

									//RIGHT CURRENT
									+ Robot.drive.R1.getOutputCurrent() + "," + Robot.drive.R2.getOutputCurrent() + ","
									+ Robot.drive.R3.getOutputCurrent() + "," + Robot.drive.R4.getOutputCurrent() + ","

									//RIGHT VOLTAGE
									+ Robot.drive.R1.getMotorOutputVoltage() + ","
									+ Robot.drive.R2.getMotorOutputVoltage() + ","
									+ Robot.drive.R3.getMotorOutputVoltage() + ","
									+ Robot.drive.R4.getMotorOutputVoltage() + ","

									//ELEVATOR CURRENT
									+ Robot.elevator.Elev_1.getOutputCurrent() + ","
									+ Robot.elevator.Elev_2.getOutputCurrent() + ","

									//ELEVATOR VOLTAGE
									+ Robot.elevator.Elev_1.getMotorOutputVoltage() + ","
									+ Robot.elevator.Elev_2.getMotorOutputVoltage() + ","

									//INTAKE PDP SLOTS
									+ Robot.drive.pdp.getCurrent(PDP.INTAKE_L) + ","
									+ Robot.drive.pdp.getCurrent(PDP.INTAKE_R) + ","

									//CLIMBER PDP SLOTS
									+ Robot.drive.pdp.getCurrent(PDP.CLIMBER_1) + "," + Robot.drive.pdp.getCurrent(PDP.CLIMBER_2) + ","

									//BATTERY
									+ DriverStation.getInstance().getBatteryVoltage()));

					bw.write("\r\n");

				} else {
					//PREVENT DUPLICATE PRINTS
				}
				p_timeString = n_timeString;
			}

		} catch (Exception e) {
			if (!hasPrintedLogFailed) {
				System.out.println("Log failed!");
				hasPrintedLogFailed = true;
			}
		}
	}

	/**
	 * @author max
	 * @param fileName
	 * @param readwrite
	 * @param usb
	 * @since
	 */
	private void createFile(String fileName, String folder, fileState readwrite, boolean usb) {
		try {
			switch (readwrite) {
			case READ:
				//SET UP FILE READING
				f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");

				fr = new FileReader(f);
				br = new Scanner(fr);

				break;
			case WRITE:
				new File(((usb) ? "/u/" : "/home/lvuser") + folder).mkdirs();

				//SETUP FILE WRITING
				f = new File(((usb) ? "/u/" : "/home/lvuser/") + folder + "/" + fileName + ".csv");

				//if it isn't there, create it
				if (!f.exists())
					f.createNewFile();

				//create file writing vars
				fw = new FileWriter(f);
				bw = new BufferedWriter(fw);
			}
		} catch (Exception e) {
			System.out.println("File " + readwrite + " from " + (usb ? "USB" : "RIO") + "failed!");
		}
	}

	/**
	 * @author max
	 * @param readwrite
	 * @since
	 */
	private void closeFile(fileState readwrite) {
		try {
			switch (readwrite) {
			case READ:
				//CLOSE READING
				br.close();
				fr.close();
				break;
			case WRITE:
				//CLOSE WRITING
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
	 * @since
	 */
	public void control(String name, String folder, boolean usb, TASK task, STATE state) {
		switch (state) {
		case STARTUP:
			switch (task) {
			case Record:

				Robot.drive.timer.stop();
				Robot.drive.timer.reset();
				Robot.drive.timer.start();

				System.out.println("Opening Record: " + name + ".csv");
				createFile(name, folder, fileState.WRITE, usb);
				writeToProfile(true);

				break;

			case Parse:
				System.out.println("Opening Parse: " + name + ".csv");
				createFile(name, folder, fileState.READ, usb);
				parseFile();
				//				printValues();

				break;
			case Log:
				Robot.drive.timer.stop();
				Robot.drive.timer.reset();
				Robot.drive.timer.start();

				System.out.println("Opening Log: " + loggingName(name, true) + ".csv");
				createFile(loggingName(name, false), folder, fileState.WRITE, usb);
				writeToLog(true);

				break;
			case Play:
				break;
			}
			break;
		case RUNTIME:
			switch (task) {

			case Record:
				writeToProfile(false);

				break;
			case Parse:

				break;
			case Log:
				writeToLog(false);

				break;
			case Play:

				break;
			}
			break;
		case FINISH:
			switch (task) {
			case Record:
				System.out.println("Closing " + task + ": " + name + ".csv");
				closeFile(fileState.WRITE);
				break;
			case Parse:
				System.out.println("Closing " + task + ": " + name + ".csv");
				closeFile(fileState.READ);
				break;
			case Log:
				System.out.println("Closing " + task + ": " + loggingName("", false) + ".csv");
				closeFile(fileState.WRITE);
				break;
			case Play:
				System.out.println("Closing " + task + ": " + name + ".csv");
				break;
			}
			break;
		}
	}

	public String loggingName(String name, boolean returnCurrent) {
		if (returnCurrent) {
			String retval = "";
			retval = (DriverStation.getInstance().isFMSAttached() ? "FIELD_" : "") + dateTime(true);
			prevDateTimeString = retval;
			return retval;
		} else {
			return prevDateTimeString;
		}
	}

	/**
	 * toRound = 2.342, wanting to round to nearest .05 1/<b>20</b> is .05
	 * roundToFraction(2.342,20)
	 * 
	 * @author max
	 * @param value
	 * @param demoninator
	 * @return double
	 * @since
	 */
	public static double roundToFraction(double value, double demoninator) {
		return Math.round(value * demoninator) / demoninator;
	}

	@Override
	public void initDefaultCommand() {
		//		setDefaultCommand(new Record("LOG", TASK.LOG));
	}

	/**
	 * 
	 * <p>
	 * Returns current date in format
	 * </p>
	 * <p>
	 * <b>yyyy.MM.dd.HH.mm.ss</b>
	 * </p>
	 * <p>
	 * or
	 * </p>
	 * <p>
	 * <b>MM.dd.HH.mm</b>
	 * </p>
	 * 
	 * @author max
	 * @param full
	 * @return string
	 * @since
	 */
	public String dateTime(boolean full) {
		String temp;
		if (full)
			temp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		else
			temp = new SimpleDateFormat("MM.dd.HH.mm").format(new Date());
		return temp;
	}

	/**
	 * reading or writing
	 * 
	 * @author max
	 * 
	 */
	public enum fileState {
		READ, WRITE;
	}

	/**
	 * startup, runtime, finish
	 * 
	 * @author max
	 */
	public enum STATE {
		STARTUP, RUNTIME, FINISH;
	}

	/**
	 * record, log, parse, play
	 * 
	 * @author max
	 */
	public enum TASK {
		Record, Log, Parse, Play;
	}

	/**
	 * @author max
	 *
	 */
	public class Constants {
	}
}
