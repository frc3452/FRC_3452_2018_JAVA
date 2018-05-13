
package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <h1>AutonSelector Subsystem</h1> Handles autonomous selector case statements
 * and printing.
 * 
 * @author max
 *
 */
public class AutonSelector extends Subsystem {

	public AnalogInput as_A;
	public AnalogInput as_B;

	private int m_prev_as1, m_prev_as2;
	private int m_asA, m_asB;

	public String autoCommandName[] = new String[41];
	public boolean controllerOverride = false;
	public boolean confirmOverride = false;

	public int overrideValue = 1;
	private String overrideStringPrevious = "";
	public String overrideString = "", autonString = "";
	public String gameMsg = "NOT";

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public AutonSelector() {
		as_A = new AnalogInput(Constants.AutonSelector.AUTO_SELECTOR_1);
		as_B = new AnalogInput(Constants.AutonSelector.AUTO_SELECTOR_2);

		as_A.setSubsystem("AutonSelector");
		as_B.setSubsystem("AutonSelector");
		as_A.setName("Selector A");
		as_B.setName("Selector B");

		for (int i = 0; i < 41; i++) {
			autoCommandName[i] = "NO COMMAND";
		}
	}

	/**
	 * @author max
	 * @since
	 */
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
			} else if ((uglyAnalog() >= 31) && (uglyAnalog() <= 40)) {
				autonString = "D / " + (uglyAnalog() - 30) + ": " + autoCommandName[uglyAnalog()];
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

	/**
	 * @author max
	 * @return int <b>1 - 40</b> or 3452 as error
	 * @since
	 */
	public int uglyAnalog() {
		if (m_asA < Constants.AutonSelector.AUTO_1 + Constants.AutonSelector.AUTO_V
				&& m_asA > Constants.AutonSelector.AUTO_1 - Constants.AutonSelector.AUTO_V) {
			if (m_asB > Constants.AutonSelector.AUTO_1_L && m_asB < Constants.AutonSelector.AUTO_1_H) {
				return 1;
			} else if (m_asB > Constants.AutonSelector.AUTO_2_L && m_asB < Constants.AutonSelector.AUTO_2_H) {
				return 2;
			} else if (m_asB > Constants.AutonSelector.AUTO_3_L && m_asB < Constants.AutonSelector.AUTO_3_H) {
				return 3;
			} else if (m_asB > Constants.AutonSelector.AUTO_4_L && m_asB < Constants.AutonSelector.AUTO_4_H) {
				return 4;
			} else if (m_asB > Constants.AutonSelector.AUTO_5_L && m_asB < Constants.AutonSelector.AUTO_5_H) {
				return 5;
			} else if (m_asB > Constants.AutonSelector.AUTO_6_L && m_asB < Constants.AutonSelector.AUTO_6_H) {
				return 6;
			} else if (m_asB > Constants.AutonSelector.AUTO_7_L && m_asB < Constants.AutonSelector.AUTO_7_H) {
				return 7;
			} else if (m_asB > Constants.AutonSelector.AUTO_8_L && m_asB < Constants.AutonSelector.AUTO_8_H) {
				return 8;
			} else if (m_asB > Constants.AutonSelector.AUTO_9_L && m_asB < Constants.AutonSelector.AUTO_9_H) {
				return 9;
			} else if (m_asB > Constants.AutonSelector.AUTO_10_L && m_asB < Constants.AutonSelector.AUTO_10_H) {
				return 10;
			} else {
				// ERROR
				return 3452;
			}
		} else if (m_asA < Constants.AutonSelector.AUTO_2 + Constants.AutonSelector.AUTO_V
				&& m_asA > Constants.AutonSelector.AUTO_2 - Constants.AutonSelector.AUTO_V) {
			if (m_asB > Constants.AutonSelector.AUTO_1_L && m_asB < Constants.AutonSelector.AUTO_1_H) {
				return 11;
			} else if (m_asB > Constants.AutonSelector.AUTO_2_L && m_asB < Constants.AutonSelector.AUTO_2_H) {
				return 12;
			} else if (m_asB > Constants.AutonSelector.AUTO_3_L && m_asB < Constants.AutonSelector.AUTO_3_H) {
				return 13;
			} else if (m_asB > Constants.AutonSelector.AUTO_4_L && m_asB < Constants.AutonSelector.AUTO_4_H) {
				return 14;
			} else if (m_asB > Constants.AutonSelector.AUTO_5_L && m_asB < Constants.AutonSelector.AUTO_5_H) {
				return 15;
			} else if (m_asB > Constants.AutonSelector.AUTO_6_L && m_asB < Constants.AutonSelector.AUTO_6_H) {
				return 16;
			} else if (m_asB > Constants.AutonSelector.AUTO_7_L && m_asB < Constants.AutonSelector.AUTO_7_H) {
				return 17;
			} else if (m_asB > Constants.AutonSelector.AUTO_8_L && m_asB < Constants.AutonSelector.AUTO_8_H) {
				return 18;
			} else if (m_asB > Constants.AutonSelector.AUTO_9_L && m_asB < Constants.AutonSelector.AUTO_9_H) {
				return 19;
			} else if (m_asB > Constants.AutonSelector.AUTO_10_L && m_asB < Constants.AutonSelector.AUTO_10_H) {
				return 20;
			} else {
				// ERROR
				return 3452;
			}
		} else if (m_asA < Constants.AutonSelector.AUTO_3 + Constants.AutonSelector.AUTO_V
				&& m_asA > Constants.AutonSelector.AUTO_3 - Constants.AutonSelector.AUTO_V) {
			if (m_asB > Constants.AutonSelector.AUTO_1_L && m_asB < Constants.AutonSelector.AUTO_1_H) {
				return 21;
			} else if (m_asB > Constants.AutonSelector.AUTO_2_L && m_asB < Constants.AutonSelector.AUTO_2_H) {
				return 22;
			} else if (m_asB > Constants.AutonSelector.AUTO_3_L && m_asB < Constants.AutonSelector.AUTO_3_H) {
				return 23;
			} else if (m_asB > Constants.AutonSelector.AUTO_4_L && m_asB < Constants.AutonSelector.AUTO_4_H) {
				return 24;
			} else if (m_asB > Constants.AutonSelector.AUTO_5_L && m_asB < Constants.AutonSelector.AUTO_5_H) {
				return 25;
			} else if (m_asB > Constants.AutonSelector.AUTO_6_L && m_asB < Constants.AutonSelector.AUTO_6_H) {
				return 26;
			} else if (m_asB > Constants.AutonSelector.AUTO_7_L && m_asB < Constants.AutonSelector.AUTO_7_H) {
				return 27;
			} else if (m_asB > Constants.AutonSelector.AUTO_8_L && m_asB < Constants.AutonSelector.AUTO_8_H) {
				return 28;
			} else if (m_asB > Constants.AutonSelector.AUTO_9_L && m_asB < Constants.AutonSelector.AUTO_9_H) {
				return 29;
			} else if (m_asB > Constants.AutonSelector.AUTO_10_L && m_asB < Constants.AutonSelector.AUTO_10_H) {
				return 30;
			} else {
				// ERROR
				return 3452;
			}
		} else if ((m_asA < Constants.AutonSelector.AUTO_4 + Constants.AutonSelector.AUTO_V
				&& m_asA > Constants.AutonSelector.AUTO_4 - Constants.AutonSelector.AUTO_V)) {
			if (m_asB > Constants.AutonSelector.AUTO_1_L && m_asB < Constants.AutonSelector.AUTO_1_H) {
				return 31;
			} else if (m_asB > Constants.AutonSelector.AUTO_2_L && m_asB < Constants.AutonSelector.AUTO_2_H) {
				return 32;
			} else if (m_asB > Constants.AutonSelector.AUTO_3_L && m_asB < Constants.AutonSelector.AUTO_3_H) {
				return 33;
			} else if (m_asB > Constants.AutonSelector.AUTO_4_L && m_asB < Constants.AutonSelector.AUTO_4_H) {
				return 34;
			} else if (m_asB > Constants.AutonSelector.AUTO_5_L && m_asB < Constants.AutonSelector.AUTO_5_H) {
				return 35;
			} else if (m_asB > Constants.AutonSelector.AUTO_6_L && m_asB < Constants.AutonSelector.AUTO_6_H) {
				return 36;
			} else if (m_asB > Constants.AutonSelector.AUTO_7_L && m_asB < Constants.AutonSelector.AUTO_7_H) {
				return 37;
			} else if (m_asB > Constants.AutonSelector.AUTO_8_L && m_asB < Constants.AutonSelector.AUTO_8_H) {
				return 38;
			} else if (m_asB > Constants.AutonSelector.AUTO_9_L && m_asB < Constants.AutonSelector.AUTO_9_H) {
				return 39;
			} else if (m_asB > Constants.AutonSelector.AUTO_10_L && m_asB < Constants.AutonSelector.AUTO_10_H) {
				return 40;
			} else {
				// ERROR
				return 3452;
			}
		} else if (m_asA < Constants.AutonSelector.AUTO_9 + Constants.AutonSelector.AUTO_V
				&& m_asA > Constants.AutonSelector.AUTO_9 - Constants.AutonSelector.AUTO_V) {
			if (m_asB > Constants.AutonSelector.AUTO_1_L && m_asB < Constants.AutonSelector.AUTO_1_H) {
				return 91;
			} else if (m_asB > Constants.AutonSelector.AUTO_2_L && m_asB < Constants.AutonSelector.AUTO_2_H) {
				return 92;
			} else if (m_asB > Constants.AutonSelector.AUTO_3_L && m_asB < Constants.AutonSelector.AUTO_3_H) {
				return 93;
			} else if (m_asB > Constants.AutonSelector.AUTO_4_L && m_asB < Constants.AutonSelector.AUTO_4_H) {
				return 94;
			} else if (m_asB > Constants.AutonSelector.AUTO_5_L && m_asB < Constants.AutonSelector.AUTO_5_H) {
				return 95;
			} else if (m_asB > Constants.AutonSelector.AUTO_6_L && m_asB < Constants.AutonSelector.AUTO_6_H) {
				return 96;
			} else if (m_asB > Constants.AutonSelector.AUTO_7_L && m_asB < Constants.AutonSelector.AUTO_7_H) {
				return 97;
			} else if (m_asB > Constants.AutonSelector.AUTO_8_L && m_asB < Constants.AutonSelector.AUTO_8_H) {
				return 98;
			} else if (m_asB > Constants.AutonSelector.AUTO_9_L && m_asB < Constants.AutonSelector.AUTO_9_H) {
				return 99;
			} else if (m_asB > Constants.AutonSelector.AUTO_10_L && m_asB < Constants.AutonSelector.AUTO_10_H) {
				return 100;
			} else {
				// ERROR
				return 3452;
			}
		} else {
			// ERROR
			return 3452;
		}

		/*
		 * @return Number between 1 - 40, A1 = 1, A10 = 10, B1 = 11, B10 = 20
		 * 
		 */
	}

	@Override
	public void initDefaultCommand() {
	}

	/**
	 * Autonomous versions enum
	 * 
	 * @author max
	 *
	 */
	public static enum AV {
		FOREST_HILLS, CURRENT;
	}

	/**
	 * Autonomous options enum
	 * 
	 * @author max
	 *
	 */
	public static enum AO {
		SWITCH, SCALE, SWITCH_PRIORITY_NO_CROSS, SCALE_PRIORITY_NO_CROSS, SWITCH_ONLY, SCALE_ONLY, DEFAULT;
	}

}
