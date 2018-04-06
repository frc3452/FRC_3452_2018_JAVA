
package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class AutonSelector extends Subsystem {

	public AnalogInput as_A;
	public AnalogInput as_B;

	private int m_prev_as1, m_prev_as2;
	private int m_asA, m_asB;

	public String autoCommandName[] = new String[31];
	public boolean controllerOverride = false;
	public boolean confirmOverride = false;

	public int overrideValue = 1;
	private String overrideStringPrevious = "";
	public String overrideString = "", autonString = "";
	public String gameMsg = "NOT";

	public void initHardware() {
		as_A = new AnalogInput(Constants.AUTO_SELECTOR_1);
		as_B = new AnalogInput(Constants.AUTO_SELECTOR_2);

		as_A.setSubsystem("AutonSelector");
		as_B.setSubsystem("AutonSelector");
		as_A.setName("Selector A");
		as_B.setName("Selector B");

		for (int i = 0; i < 31; i++) {
			autoCommandName[i] = "NO COMMAND";
		}
	}

	public void printSelected() {
		m_asA = as_A.getValue();
		m_asB = as_B.getValue();

		//		System.out.println(as_A.getValue() + "\t\t\t" + as_B.getValue());

		// If overriden, print overide
		if (controllerOverride && (overrideString != overrideStringPrevious))
			System.out.println(overrideString);

		if (((m_asA + 8 < m_prev_as1 || m_prev_as1 < m_asA - 8)
				|| (m_asB + 8 < m_prev_as2 || m_prev_as2 < m_asB - 8))) {

			if ((uglyAnalog() >= 1) && (uglyAnalog() <= 10)) {
				autonString = "A / " + uglyAnalog() + ": " + autoCommandName[uglyAnalog()];
			} else if ((uglyAnalog() >= 11) && (uglyAnalog() <= 20)) {
				autonString = "B / " + (uglyAnalog() - 10) + ": " + autoCommandName[uglyAnalog()];
			} else if ((uglyAnalog() >= 21) && (uglyAnalog() <= 30)) {
				autonString = "C / " + (uglyAnalog() - 20) + ": " + autoCommandName[uglyAnalog()];
			} else {
				autonString = "AUTON NOT SELECTED";
			}
			System.out.println(autonString);
		}

		// update values for one time display
		m_prev_as1 = m_asA;
		m_prev_as2 = m_asB;

		overrideStringPrevious = overrideString;
	}

	public int uglyAnalog() {
		if (m_asA < Constants.AUTO_1 + Constants.AUTO_V && m_asA > Constants.AUTO_1 - Constants.AUTO_V) {
			if (m_asB > Constants.AUTO_1_L && m_asB < Constants.AUTO_1_H) {
				return 1;
			} else if (m_asB > Constants.AUTO_2_L && m_asB < Constants.AUTO_2_H) {
				return 2;
			} else if (m_asB > Constants.AUTO_3_L && m_asB < Constants.AUTO_3_H) {
				return 3;
			} else if (m_asB > Constants.AUTO_4_L && m_asB < Constants.AUTO_4_H) {
				return 4;
			} else if (m_asB > Constants.AUTO_5_L && m_asB < Constants.AUTO_5_H) {
				return 5;
			} else if (m_asB > Constants.AUTO_6_L && m_asB < Constants.AUTO_6_H) {
				return 6;
			} else if (m_asB > Constants.AUTO_7_L && m_asB < Constants.AUTO_7_H) {
				return 7;
			} else if (m_asB > Constants.AUTO_8_L && m_asB < Constants.AUTO_8_H) {
				return 8;
			} else if (m_asB > Constants.AUTO_9_L && m_asB < Constants.AUTO_9_H) {
				return 9;
			} else if (m_asB > Constants.AUTO_10_L && m_asB < Constants.AUTO_10_H) {
				return 10;
			} else {
				// ERROR
				return 3452;
			}
		} else if (m_asA < Constants.AUTO_2 + Constants.AUTO_V && m_asA > Constants.AUTO_2 - Constants.AUTO_V) {
			if (m_asB > Constants.AUTO_1_L && m_asB < Constants.AUTO_1_H) {
				return 11;
			} else if (m_asB > Constants.AUTO_2_L && m_asB < Constants.AUTO_2_H) {
				return 12;
			} else if (m_asB > Constants.AUTO_3_L && m_asB < Constants.AUTO_3_H) {
				return 13;
			} else if (m_asB > Constants.AUTO_4_L && m_asB < Constants.AUTO_4_H) {
				return 14;
			} else if (m_asB > Constants.AUTO_5_L && m_asB < Constants.AUTO_5_H) {
				return 15;
			} else if (m_asB > Constants.AUTO_6_L && m_asB < Constants.AUTO_6_H) {
				return 16;
			} else if (m_asB > Constants.AUTO_7_L && m_asB < Constants.AUTO_7_H) {
				return 17;
			} else if (m_asB > Constants.AUTO_8_L && m_asB < Constants.AUTO_8_H) {
				return 18;
			} else if (m_asB > Constants.AUTO_9_L && m_asB < Constants.AUTO_9_H) {
				return 19;
			} else if (m_asB > Constants.AUTO_10_L && m_asB < Constants.AUTO_10_H) {
				return 20;
			} else {
				// ERROR
				return 3452;
			}
		} else if (m_asA < Constants.AUTO_3 + Constants.AUTO_V && m_asA > Constants.AUTO_3 - Constants.AUTO_V) {
			if (m_asB > Constants.AUTO_1_L && m_asB < Constants.AUTO_1_H) {
				return 21;
			} else if (m_asB > Constants.AUTO_2_L && m_asB < Constants.AUTO_2_H) {
				return 22;
			} else if (m_asB > Constants.AUTO_3_L && m_asB < Constants.AUTO_3_H) {
				return 23;
			} else if (m_asB > Constants.AUTO_4_L && m_asB < Constants.AUTO_4_H) {
				return 24;
			} else if (m_asB > Constants.AUTO_5_L && m_asB < Constants.AUTO_5_H) {
				return 25;
			} else if (m_asB > Constants.AUTO_6_L && m_asB < Constants.AUTO_6_H) {
				return 26;
			} else if (m_asB > Constants.AUTO_7_L && m_asB < Constants.AUTO_7_H) {
				return 27;
			} else if (m_asB > Constants.AUTO_8_L && m_asB < Constants.AUTO_8_H) {
				return 28;
			} else if (m_asB > Constants.AUTO_9_L && m_asB < Constants.AUTO_9_H) {
				return 29;
			} else if (m_asB > Constants.AUTO_10_L && m_asB < Constants.AUTO_10_H) {
				return 30;
			} else {
				// ERROR
				return 3452;
			}
		} else {
			// ERROR
			return 3452;
		}
	}

	public void initDefaultCommand() {
		//		setDefaultCommand(new LoggerUpdate());
	}

	public static enum AO {
		SWITCH, SCALE, SWITCH_PRIORITY_NO_CROSS, SCALE_PRIORITY_NO_CROSS, SWITCH_ONLY, SCALE_ONLY, DEFAULT;
	}

	public static class Constants {
		public static final int AUTO_SELECTOR_1 = 2; //1
		public static final int AUTO_SELECTOR_2 = 3; //0

		//TODO AUTO TUNE
		public static final int AUTO_V = 15;
		public static final int AUTO_1 = 2683; //2661
		public static final int AUTO_2 = 2994; //2963
		public static final int AUTO_3 = 3186; //3150
		public static final int AUTO_4 = 3320; //3280
		public static final int AUTO_5 = 3428; //3885
		public static final int AUTO_6 = 3506; //3463
		public static final int AUTO_7 = 3565; //3521
		public static final int AUTO_8 = 3659; //3612
		public static final int AUTO_9 = 3721; //3673
		public static final int AUTO_10 = 3783; //3734

		public static final int AUTO_1_L = AUTO_1 - AUTO_V;
		public static final int AUTO_1_H = AUTO_1 + AUTO_V;

		public static final int AUTO_2_L = AUTO_2 - AUTO_V;
		public static final int AUTO_2_H = AUTO_2 + AUTO_V;

		public static final int AUTO_3_L = AUTO_3 - AUTO_V;
		public static final int AUTO_3_H = AUTO_3 + AUTO_V;

		public static final int AUTO_4_L = AUTO_4 - AUTO_V;
		public static final int AUTO_4_H = AUTO_4 + AUTO_V;

		public static final int AUTO_5_L = AUTO_5 - AUTO_V;
		public static final int AUTO_5_H = AUTO_5 + AUTO_V;

		public static final int AUTO_6_L = AUTO_6 - AUTO_V;
		public static final int AUTO_6_H = AUTO_6 + AUTO_V;

		public static final int AUTO_7_L = AUTO_7 - AUTO_V;
		public static final int AUTO_7_H = AUTO_7 + AUTO_V;

		public static final int AUTO_8_L = AUTO_8 - AUTO_V;
		public static final int AUTO_8_H = AUTO_8 + AUTO_V;

		public static final int AUTO_9_L = AUTO_9 - AUTO_V;
		public static final int AUTO_9_H = AUTO_9 + AUTO_V;

		public static final int AUTO_10_L = AUTO_10 - AUTO_V;
		public static final int AUTO_10_H = AUTO_10 + AUTO_V;

	}
}
