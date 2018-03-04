package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.commands.signal.LoggerUpdate;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

public class AutonSelector extends Subsystem {

	private AnalogInput as_A;
	private AnalogInput as_B;

	private int m_prev_as1, m_prev_as2;
	private int m_asA, m_asB;

	public String autoCommandName[] = new String[20];
	public boolean m_ControllerOverride = false;

	private String overrideStringPrevious;
	public String overrideString, autonString;

	public void initHardware() {
		as_A = new AnalogInput(Constants.AUTO_SELECTOR_1);
		as_B = new AnalogInput(Constants.AUTO_SELECTOR_2);

		for (int i = 0; i < 20; i++) {
			autoCommandName[i] = "NO COMMAND";
		}
	}

	public void printSelected() {
		m_asA = as_A.getValue();
		m_asB = as_B.getValue();

		// If overriden, print overide
		if (m_ControllerOverride && (overrideString != overrideStringPrevious))
			System.out.println(overrideString);

		if (((m_asA + 8 < m_prev_as1 || m_prev_as1 < m_asA - 8)
				|| (m_asB + 8 < m_prev_as2 || m_prev_as2 < m_asB - 8))) {

			// upon update print out current value
			// switch statement for selector 1

			if (m_asA < Constants.AUTO_1 + Constants.AUTO_V && m_asA > Constants.AUTO_1 - Constants.AUTO_V) {
				if (m_asB > Constants.AUTO_1_L && m_asB < Constants.AUTO_1_H) {
					autonString = "A / 1  " + autoCommandName[1];
				} else if (m_asB > Constants.AUTO_2_L && m_asB < Constants.AUTO_2_H) {
					autonString = "A / 2  " + autoCommandName[2];
				} else if (m_asB > Constants.AUTO_3_L && m_asB < Constants.AUTO_3_H) {
					autonString = "A / 3  " + autoCommandName[3];
				} else if (m_asB > Constants.AUTO_4_L && m_asB < Constants.AUTO_4_H) {
					autonString = "A / 4  " + autoCommandName[4];
				} else if (m_asB > Constants.AUTO_5_L && m_asB < Constants.AUTO_5_H) {
					autonString = "A / 5  " + autoCommandName[5];
				} else if (m_asB > Constants.AUTO_6_L && m_asB < Constants.AUTO_6_H) {
					autonString = "A / 6  " + autoCommandName[6];
				} else if (m_asB > Constants.AUTO_7_L && m_asB < Constants.AUTO_7_H) {
					autonString = "A / 7  " + autoCommandName[7];
				} else if (m_asB > Constants.AUTO_8_L && m_asB < Constants.AUTO_8_H) {
					autonString = "A / 8  " + autoCommandName[8];
				} else if (m_asB > Constants.AUTO_9_L && m_asB < Constants.AUTO_9_H) {
					autonString = "A / 9  " + autoCommandName[9];
				} else if (m_asB > Constants.AUTO_10_L && m_asB < Constants.AUTO_10_H) {
					autonString = "A / 10 " + autoCommandName[10];
				} else {
					autonString = "AUTON NOT SELECTED";
				}
			} else if (m_asA < Constants.AUTO_2 + Constants.AUTO_V && m_asA > Constants.AUTO_2 - Constants.AUTO_V) {
				if (m_asB > Constants.AUTO_1_L && m_asB < Constants.AUTO_1_H) {
					autonString = "B / 1  " + autoCommandName[11];
				} else if (m_asB > Constants.AUTO_2_L && m_asB < Constants.AUTO_2_H) {
					autonString = "B / 2  " + autoCommandName[12];
				} else if (m_asB > Constants.AUTO_3_L && m_asB < Constants.AUTO_3_H) {
					autonString = "B / 3  " + autoCommandName[13];
				} else if (m_asB > Constants.AUTO_4_L && m_asB < Constants.AUTO_4_H) {
					autonString = "B / 4  " + autoCommandName[14];
				} else if (m_asB > Constants.AUTO_5_L && m_asB < Constants.AUTO_5_H) {
					autonString = "B / 5  " + autoCommandName[15];
				} else if (m_asB > Constants.AUTO_6_L && m_asB < Constants.AUTO_6_H) {
					autonString = "B / 6  " + autoCommandName[16];
				} else if (m_asB > Constants.AUTO_7_L && m_asB < Constants.AUTO_7_H) {
					autonString = "B / 7  " + autoCommandName[17];
				} else if (m_asB > Constants.AUTO_8_L && m_asB < Constants.AUTO_8_H) {
					autonString = "B / 8  " + autoCommandName[18];
				} else if (m_asB > Constants.AUTO_9_L && m_asB < Constants.AUTO_9_H) {
					autonString = "B / 9  " + autoCommandName[19];
				} else if (m_asB > Constants.AUTO_10_L && m_asB < Constants.AUTO_10_H) {
					autonString = "B / 10 " + autoCommandName[20];
				} else {
					autonString = "AUTON NOT SELECTED";
				}
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
		} else {
			// ERROR
			return 3452;
		}
	}

	public void initDefaultCommand() {
//		setDefaultCommand(new LoggerUpdate());
	}

	public static class Constants {
		public static final int AUTO_SELECTOR_1 = 3;
		public static final int AUTO_SELECTOR_2 = 2;

		public static final int AUTO_V = 15;
		public static final int AUTO_1 = 2684;
		public static final int AUTO_2 = 2994;
		public static final int AUTO_3 = 3187;
		public static final int AUTO_4 = 3322;
		public static final int AUTO_5 = 3427;
		public static final int AUTO_6 = 3507;
		public static final int AUTO_7 = 3568;
		public static final int AUTO_8 = 3660;
		public static final int AUTO_9 = 3722;
		public static final int AUTO_10 = 3782;

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
