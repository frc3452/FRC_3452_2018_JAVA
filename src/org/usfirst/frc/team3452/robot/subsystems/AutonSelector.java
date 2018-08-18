
package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kAutonSelector;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.auton.LeftAuton;
import org.usfirst.frc.team3452.robot.commands.auton.MiddleAuton;
import org.usfirst.frc.team3452.robot.commands.auton.RightAuton;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Command;
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
	public Command autoCommand[] = new Command[41];
	public Command autonomousCommand = null;
	public Command defaultCommand = null;
	
	public boolean controllerOverride = false;

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
		{
			autoCommandName[i] = "NO COMMAND";
			autoCommand[i] = null;
		}
		
			
		setAutonNames();
	}

	public void autonChooser() {
		controllerChooser();

		// if not overriden
		if (!controllerOverride) {

			// If selector feedback nominal
			if (uglyAnalog() <= 40 && uglyAnalog() >= 1) {
				autonomousCommand = autoCommand[Robot.autonSelector.uglyAnalog()];
			} else {
				autonomousCommand = defaultCommand;
			}
		}
	}
	
	private void controllerChooser() {
		// if LB & RB
		if (OI.driverJoy.getRawButton(5) && OI.driverJoy.getRawButton(6)) {

			// A BUTTON
			if (OI.driverJoy.getRawButton(1)) {
				overrideValue = 2;
				overrideString = "Controller override 1:\t"
						+ autoCommandName[2];
				controllerOverride = true;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				overrideValue = 3;
				overrideString = "Controller override 2:\t"
						+ autoCommandName[3];
				controllerOverride = true;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				overrideValue = 4;
				overrideString = "Controller override 3:\t"
						+ autoCommandName[4];
				controllerOverride = true;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				overrideValue = 5;
				overrideString = "Controller override 4:\t"
						+ autoCommandName[5];
				controllerOverride = true;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				overrideValue = 13;
				overrideString = "Controller override 5:\t"
						+ autoCommandName[13];
				controllerOverride = true;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				overrideValue = 17;
				overrideString = "Controller override 6:\t"
						+ autoCommandName[17];
				controllerOverride = true;
			}

			// LEFT CLICK
			else if (OI.driverJoy.getRawButton(9)) {
				autonomousCommand = defaultCommand;
				overrideString = "Controller override: DEFAULT AUTO SELECTED";
				controllerOverride = true;
			}

			// RIGHT CLICK
			else if (OI.driverJoy.getRawButton(10)) {
				controllerOverride = false;
				System.out.println(autonString);
			}
		}

	}
	
	
	public void setAutonNames() {
		// naming for commands
		autoCommandName[1] = "Middle - Switch";
		autoCommandName[2] = "Left - Switch";
		autoCommandName[3] = "Left - Scale";
		autoCommandName[4] = "Right - Switch";
		autoCommandName[5] = "Right - Scale";

		autoCommandName[11] = "Left Only - Switch Priority";
		autoCommandName[12] = "Left Only - Scale Priority";
		autoCommandName[13] = "Left Only - Switch Only";
		autoCommandName[14] = "Left Only - Scale Only";

		autoCommandName[15] = "Right Only - Switch Priority";
		autoCommandName[16] = "Right Only - Scale Priority";
		autoCommandName[17] = "Right Only - Switch Only";
		autoCommandName[18] = "Right Only - Scale Only";

		autoCommandName[19] = "Left - Default";
		autoCommandName[20] = "Right - Default";

		autoCommandName[21] = "(MIFOR) Middle - Switch";
		autoCommandName[22] = "(MIFOR) Left - Switch";
		autoCommandName[23] = "(MIFOR) Left - Scale";
		autoCommandName[24] = "(MIFOR) Right - Switch";
		autoCommandName[25] = "(MIFOR) Right - Scale";

		autoCommandName[26] = "(MIFOR) Left Only - Switch Priority";
		autoCommandName[27] = "(MIFOR) Left Only - Scale Priority";
		autoCommandName[28] = "(MIFOR) Right Only - Switch Priority";
		autoCommandName[29] = "(MIFOR) Right Only - Scale Priority";
	}

	public void setAutons()
	{
		autoCommand[1] = (new MiddleAuton(AO.SWITCH, AV.CURRENT));
		autoCommand[2] = (new LeftAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		autoCommand[3] = (new LeftAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));
		autoCommand[4] = (new RightAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		autoCommand[5] = (new RightAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));
		
		autoCommand[11] = (new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[12] = (new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[13] = (new LeftAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		autoCommand[14] = (new LeftAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));
		
		autoCommand[15] = (new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[16] = (new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[17] = (new RightAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		autoCommand[18] = (new RightAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));
		
		autoCommand[19] = (new LeftAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));
		autoCommand[20] = (new RightAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));
		
		autoCommand[21] = (new MiddleAuton(AO.SWITCH, AV.FOREST_HILLS));
		autoCommand[22] = (new LeftAuton(AO.SWITCH, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[23] = (new LeftAuton(AO.SCALE, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[24] = (new RightAuton(AO.SWITCH, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[25] = (new RightAuton(AO.SCALE, AV.FOREST_HILLS, AV.CURRENT));
		
		autoCommand[26] = (new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[27] = (new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[28] = (new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.FOREST_HILLS, AV.CURRENT));
		autoCommand[29] = (new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.FOREST_HILLS, AV.CURRENT));
		
		if (Robot.autonSelector.controllerOverride)
			autonomousCommand = autoCommand[Robot.autonSelector.overrideValue];
	}
	
	public boolean isDemo() {
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
	 * @return Number between 1 - 100, A1 = 1, A10 = 10, B1 = 11, B10 = 20, or 3452
	 *         as error
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
