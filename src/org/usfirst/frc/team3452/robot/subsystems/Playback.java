package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Playback extends Subsystem {

	File f;
	BufferedWriter bw;
	FileWriter fw;

	FileReader fr;
	BufferedReader br;

	//	 TIME, LEFT VEL, RIGHT VEL
	double RP1A[] = new double[900]; // = new double[900];
	double RP1B[] = new double[900]; // = new double[900];
	double RP1C[] = new double[900]; // = new double[900];

	public void initHardware() {
	}

	private void parseFile() {
		int posInFile = 0, comma1 = 0, comma2 = 0;

		String st;
		try {
			//loop through each line
			while ((st = br.readLine()) != null) {

				
				//populate time
				RP1A[posInFile] = Double.parseDouble(st.substring(0, 8));

				//find commas
				for (int i = 7; i < st.length(); i++) {
					if (st.charAt(i) == ',')
						comma2 = i;
				}

				//find commas
				for (int i = comma2; i > 0; i--) {
					if (st.charAt(i) == ',')
						comma1 = i;
				}
				
				
				RP1B[posInFile] = Double.parseDouble(st.substring(comma1 + 1, comma2 - 1));
				RP1C[posInFile] = Double.parseDouble(st.substring(comma2 + 1, st.length()));
				//fill variables
				
				comma1 = 1000;
				comma2 = 1000;
				posInFile++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void printTimes() {
		for (int i = 0; i < RP1A.length; i++)
			System.out.println("Time:" + RP1A[i] + "\t\tL: " + RP1B[i] + "\t\tR: " + RP1C[i]);

	}

	private void writeToFile() {
		try {
			String timeString = String.format("%8s", roundToFraction(Robot.drive.timer.get(), 50));
			timeString = timeString.replace(' ', '0');

			double left = (double) Robot.drive.L1.getSelectedSensorVelocity(0) / 4096;
			double right = (double) Robot.drive.R1.getSelectedSensorVelocity(0) * -1 / 4096;

			if (left < 0.01 && left > -0.01)
				left = 0;

			if (right < 0.01 && right > -0.01)
				right = 0;

			bw.write(timeString);
			bw.write(",");
			bw.write(String.valueOf(left));
			bw.write(",");
			bw.write(String.valueOf(right));
			bw.write("\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void writeToLog(boolean startup) {
		try {
			if (startup) {
				bw.write(
						"Time, L1-AMPERAGE, L2-AMPERAGE,L3-AMPERAGE,L4-AMPERAGE,R1-AMPERAGE,R2-AMPERAGE,R3-AMPERAGE,R4-AMPERAGE, BATTERY");
				bw.write("\r\n");
			} else {
				String timeString = String.format("%8s", roundToFraction(Robot.drive.timer.get(), 50));
				timeString = timeString.replace(' ', '0');

				bw.write(timeString);
				bw.write(",");
				bw.write(String.valueOf(Robot.drive.L1.getOutputCurrent() 
						+ "," + Robot.drive.L2.getOutputCurrent()
						+ "," + Robot.drive.L3.getOutputCurrent() 
						+ "," + Robot.drive.L4.getOutputCurrent() 
						+ "," + Robot.drive.R1.getOutputCurrent() 
						+ "," + Robot.drive.R2.getOutputCurrent() 
						+ "," + Robot.drive.R3.getOutputCurrent() 
						+ "," + Robot.drive.R4.getOutputCurrent() 
						+ "," + DriverStation.getInstance().getBatteryVoltage()));
				bw.write("\r\n");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createFile(String fileName, fileState readwrite) {
		switch (readwrite) {
		case READ:
			//SET UP FILE READING
			try {
				f = new File("/home/lvuser/" + fileName + ".csv");
				fr = new FileReader(f);
				br = new BufferedReader(fr);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			break;
		case WRITE:

			//SETUP FILE WRITING
			try {
				f = new File("/home/lvuser/" + fileName + ".csv");

				//if it isn't there, create it
				if (!f.exists()) {
					f.createNewFile();
				}

				//create file writing vars
				fw = new FileWriter(f);
			} catch (IOException e) {
				e.printStackTrace();
			}
			bw = new BufferedWriter(fw);
		}
	}

	private void closeFile(fileState readwrite) {
		switch (readwrite) {
		case READ:
			//CLOSE READING
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case WRITE:
			//CLOSE WRITING
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void playbackControl(String name, TASK task, STATE state) {

		switch (state) {
		case STARTUP:

			switch (task) {
			case RECORD:

				Robot.drive.timer.stop();
				Robot.drive.timer.reset();
				Robot.drive.timer.start();

				System.out.println("Opening RECORD: " + name + ".csv");
				createFile(name, fileState.WRITE);
				break;

			case PARSE:
				System.out.println("Opening PARSE: " + name + ".csv");
				createFile(name, fileState.READ);

				parseFile();
				printTimes();
				break;
			case LOG:
				Robot.drive.timer.stop();
				Robot.drive.timer.reset();
				Robot.drive.timer.start();

				System.out.println("Opening LOG: " + name + ".csv");
				createFile(name, fileState.WRITE);
				writeToLog(true);

				break;
			case PLAY:
				break;
			}
			break;
		case RUNTIME:
			switch (task) {

			case RECORD:
				writeToFile();

				break;
			case PARSE:

				break;
			case LOG:
				writeToLog(false);

				break;
			case PLAY:

				break;
			}
			break;
		case FINISH:
			switch (task) {
			case RECORD:
				System.out.println("Closing RECORD: " + name + ".csv");
				closeFile(fileState.WRITE);
				break;
			case PARSE:
				System.out.println("Closing PARSE: " + name + ".csv");
				closeFile(fileState.READ);
				break;
			case LOG:
				System.out.println("Closing LOG: " + name + ".csv");
				closeFile(fileState.WRITE);
				break;
			case PLAY:
				break;
			}
			break;
		}
	}

	public static double roundToFraction(double x, double demoninator) {
		return (double) (Math.round(x * demoninator) / demoninator);
	}

	public void initDefaultCommand() {
		//		setDefaultCommand(new Record("LOG", TASK.LOG));
	}

	public enum fileState {
		READ, WRITE;
	}

	public enum STATE {
		STARTUP, RUNTIME, FINISH;
	}

	public enum TASK {
		RECORD, LOG, PARSE, PLAY;
	}
}
