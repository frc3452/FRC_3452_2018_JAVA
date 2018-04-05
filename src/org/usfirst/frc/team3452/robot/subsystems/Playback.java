package org.usfirst.frc.team3452.robot.subsystems;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Playback extends Subsystem {

	File f;
	BufferedWriter bw;
	FileWriter fw;

	FileReader fr;
	BufferedReader br;

	//	 TIME, LEFT VEL, RIGHT VEL
	//TODO PLAYBACK VARS
	double RP1A[] = new double[1]; // = new double[900];
	double RP1B[] = new double[1]; // = new double[900];
	double RP1C[] = new double[1]; // = new double[900];

	public void initHardware() {
	}

	public void parseFile() {
		int posInFile = 0, comma1 = 0, comma2 = 0;

		String st;
		try {
			//loop through each line
			while ((st = br.readLine()) != null) {

				//populate time
				RP1A[posInFile] = Double.parseDouble(st.substring(0, 8));

				//populate left velocity
				for (int i = 0; i < st.length(); i++) {
					if (st.charAt(i) == ',')
						comma1 = i;
				}
				for (int i = comma1; i < st.length() - comma1; i++) {
					if (st.charAt(i) == ',')
						comma2 = i;
				}

				System.out.println(comma1 + "\t" + comma2);
				//				System.out.println(st.charAt(comma1 - 1) + "\t" + st.charAt(comma2));
				//				System.out.println(Double.parseDouble(st.substring(comma1 - 1, comma2)));
				//				RP1B[posInFile] = Double.parseDouble(st.substring(comma1, comma2));

				comma1 = 0;
				comma2 = 0;
				posInFile++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void printTimes() {
		for (int i = 0; i < RP1A.length; i++) {
			System.out.println("Time:" + RP1A[i]);
		}
		for (int i = 0; i < RP1B.length; i++) {
			System.out.println("LAxis: " + RP1B[i]);
		}

	}

	public void writeToFile() {
		try {
			String timeString = String.format("%8s", roundToFraction(Robot.drive.timer.get(), 50));
			timeString = timeString.replace(' ', '0');

			bw.write(timeString);
			bw.write(",");
			bw.write(String.valueOf((double) Robot.drive.L1.getSelectedSensorVelocity(0) / 4096));
			bw.write(",");
			bw.write(String.valueOf((double) Robot.drive.R1.getSelectedSensorVelocity(0) * -1 / 4096));
			bw.write(",");
			bw.write("\r\n");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void createFile(String fileName, boolean readwrite) {
		if (readwrite) {
			//SET UP FILE READING
			try {
				f = new File("/home/lvuser/" + fileName + ".csv");
				fr = new FileReader(f);
				br = new BufferedReader(fr);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} else {

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

	public void closeFile(boolean readwrite) {
		if (readwrite) {
			//CLOSE READING
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			//CLOSE WRITING
			try {
				bw.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static double roundToFraction(double x, double demoninator) {
		return (double) (Math.round(x * demoninator) / demoninator);
	}

	public void initDefaultCommand() {
	}

	public enum TASK {
		RECORD, PARSE, PLAY;
	}
}
