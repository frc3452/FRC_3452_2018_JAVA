
package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kAutonSelector;

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
	 */
	public AutonSelector() {
		as_A = new AnalogInput(Constants.kAutonSelector.AUTO_SELECTOR_1);
		as_B = new AnalogInput(Constants.kAutonSelector.AUTO_SELECTOR_2);

		as_A.setSubsystem("AutonSelector");
		as_B.setSubsystem("AutonSelector");
		as_A.setName("Selector A");
		as_B.setName("Selector B");

		for (int i = 0; i < 41; i++)
			autoCommandName[i] = "NO COMMAND";
	}

	public boolean isSaftey(){
		return uglyAnalog() == kAutonSelector.SAFTEY_SWITCH;
	}
	
	public void printSelected() {
		m_asA = as_A.getValue();
		m_asB = as_B.getValue();

		// System.out.println(as_A.getValue() + "\t\t\t" + as_B.getValue());

		// If overriden, print overide
		if (controllerOverride && (!overrideString.equals(overrideStringPrevious)))
			System.out.println(overrideString);

		if (((m_asA + 8 < m_prev_as1 || m_prev_as1 < m_asA - 8)
				|| (m_asB + 8 < m_prev_as2 || m_prev_as2 < m_asB - 8))) {

			if ((uglyAnalog() >= 1) && (uglyAnalog() <= 10)) {
				autonString = "A / " + uglyAnalog() + ": " + autoCommandName[uglyAnalog()];
			} else if ((uglyAnalog() >= 11) && (uglyAnalog() <= 20)) {
				autonString = "B / " + (uglyAnalog() - 10) + ": " + autoCommandName[uglyAnalog()] + " - "
						+ uglyAnalog();
			} else if ((uglyAnalog() >= 21) && (uglyAnalog() <= 30)) {
				autonString = "C / " + (uglyAnalog() - 20) + ": " + autoCommandName[uglyAnalog()] + " - "
						+ uglyAnalog();
			} else if ((uglyAnalog() >= 31) && (uglyAnalog() <= 40)) {
				autonString = "D / " + (uglyAnalog() - 30) + ": " + autoCommandName[uglyAnalog()] + " - "
						+ uglyAnalog();
			} else {
				autonString = "AUTON NOT SELECTED: " + uglyAnalog();
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
	 * @return Number between 1 - 100, A1 = 1, A10 = 10, B1 = 11, B10 = 20, or 3452 as error
	 */
	public int uglyAnalog() {
		if (m_asA < Constants.kAutonSelector.AUTO_1 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_1 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(0);

		} else if (m_asA < Constants.kAutonSelector.AUTO_2 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_2 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(1);

		} else if (m_asA < Constants.kAutonSelector.AUTO_3 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_3 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(2);

		} else if ((m_asA < Constants.kAutonSelector.AUTO_4 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_4 - Constants.kAutonSelector.AUTO_V)) {
			return selectorB(3);

		} else if ((m_asA < Constants.kAutonSelector.AUTO_5 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_5 - Constants.kAutonSelector.AUTO_V)) {
			return selectorB(4);

		} else if (m_asA < Constants.kAutonSelector.AUTO_6 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_6 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(5);

		} else if (m_asA < Constants.kAutonSelector.AUTO_7 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_7 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(6);

		} else if (m_asA < Constants.kAutonSelector.AUTO_8 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_8 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(7);
		} else if (m_asA < Constants.kAutonSelector.AUTO_9 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_9 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(8);

		} else if (m_asA < Constants.kAutonSelector.AUTO_10 + Constants.kAutonSelector.AUTO_V
				&& m_asA > Constants.kAutonSelector.AUTO_10 - Constants.kAutonSelector.AUTO_V) {
			return selectorB(9);

		} else {
			// ERROR
			return 3452;
		}

	}

	private int selectorB(int selectorA) {
		if (m_asB > Constants.kAutonSelector.AUTO_1_L && m_asB < Constants.kAutonSelector.AUTO_1_H) {
			return 1 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_2_L && m_asB < Constants.kAutonSelector.AUTO_2_H) {
			return 2 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_3_L && m_asB < Constants.kAutonSelector.AUTO_3_H) {
			return 3 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_4_L && m_asB < Constants.kAutonSelector.AUTO_4_H) {
			return 4 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_5_L && m_asB < Constants.kAutonSelector.AUTO_5_H) {
			return 5 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_6_L && m_asB < Constants.kAutonSelector.AUTO_6_H) {
			return 6 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_7_L && m_asB < Constants.kAutonSelector.AUTO_7_H) {
			return 7 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_8_L && m_asB < Constants.kAutonSelector.AUTO_8_H) {
			return 8 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_9_L && m_asB < Constants.kAutonSelector.AUTO_9_H) {
			return 9 + (selectorA * 10);
		} else if (m_asB > Constants.kAutonSelector.AUTO_10_L && m_asB < Constants.kAutonSelector.AUTO_10_H) {
			return 10 + (selectorA * 10);
		} else {
			// ERROR
			return 3452;
		}
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
	public enum AV {
		SEASON, FOREST_HILLS, CURRENT
	}

	/**
	 * Autonomous options enum
	 * 
	 * @author max
	 *
	 */
	public enum AO {
		SWITCH, SCALE, SWITCH_PRIORITY_NO_CROSS, SCALE_PRIORITY_NO_CROSS, SWITCH_ONLY, SCALE_ONLY, DEFAULT
	}

}
