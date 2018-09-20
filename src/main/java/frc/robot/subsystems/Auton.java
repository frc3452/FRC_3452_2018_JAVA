package frc.robot.subsystems;

import java.util.Arrays;

import frc.robot.Constants;
import frc.robot.Constants.kAuton;
import frc.robot.OI;
import frc.robot.commands.auton.DefaultAutonomous;
import frc.robot.commands.auton.LeftAuton;
import frc.robot.commands.auton.MiddleAuton;
import frc.robot.commands.auton.RightAuton;
import frc.robot.util.GZCommand;
import frc.robot.util.GZJoystick.Buttons;
import frc.robot.util.GZTimer;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;

/**
 * <h1>AutonSelector Subsystem</h1> Handles autonomous selector case statements
 * and printing.
 * 
 * @author max
 *
 */
public class Auton {

	private AnalogInput as_A;
	private AnalogInput as_B;

	private int m_prev_as1, m_prev_as2;
	private int m_asA, m_asB;

	public GZCommand commandArray[] = new GZCommand[kAuton.COMMAND_ARRAY_SIZE];
	
	private GZCommand defaultCommand = null;
	public Command autonomousCommand = null;

	private boolean controllerOverride = false;

	private int overrideValue = 1;
	private String overrideStringPrevious = "";
	private String overrideString = "", autonString = "";
	private String gameMsg = "NOT";

	public GZTimer matchTimer = new GZTimer("matchTimer");

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Auton() {
		as_A = new AnalogInput(Constants.kAuton.AUTO_SELECTOR_1);
		as_B = new AnalogInput(Constants.kAuton.AUTO_SELECTOR_2);

		as_A.setSubsystem("AutonSelector");
		as_B.setSubsystem("AutonSelector");
		as_A.setName("Selector A");
		as_B.setName("Selector B");

		Arrays.fill(commandArray, null);
//		fillAutonArray();
	}

	/**
	 * Used to pre-populate values for what auto is to be selected before and after
	 * commands are loaded
	 */
	public void autonChooser() {
		controllerChooser();

		if (controllerOverride) {
			autonomousCommand = commandArray[overrideValue].getCommand();
		} else {
			// Check if auton selectors are returning what they should be
			if (uglyAnalog() <= kAuton.COMMAND_ARRAY_SIZE && uglyAnalog() >= 1) {
				autonomousCommand = commandArray[uglyAnalog()].getCommand();
			} else {
				autonomousCommand = defaultCommand.getCommand();
			}
		}
		
		printSelected();
	}

	/**
	 * Sets the names for the override and the value of the array in which to
	 * override
	 */
	private void controllerChooser() {
		if (OI.driverJoy.getRawButton(Buttons.LB) && OI.driverJoy.getRawButton(Buttons.RB)) {

			if (OI.driverJoy.getRawButton(Buttons.A)) {
				overrideValue = 2;
				overrideString = "Controller override 1:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			else if (OI.driverJoy.getRawButton(Buttons.B)) {
				overrideValue = 3;
				overrideString = "Controller override 2:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(Buttons.X)) {
				overrideValue = 4;
				overrideString = "Controller override 3:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(Buttons.Y)) {
				overrideValue = 5;
				overrideString = "Controller override 4:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(Buttons.BACK)) {
				overrideValue = 13;
				overrideString = "Controller override 5:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(Buttons.START)) {
				overrideValue = 17;
				overrideString = "Controller override 6:\t" + commandArray[overrideValue].getName();
				controllerOverride = true;
			}

			// LEFT CLICK
			else if (OI.driverJoy.getRawButton(Buttons.LEFT_CLICK)) {
				autonomousCommand = defaultCommand.getCommand();
				overrideString = "Controller override: DEFAULT AUTO SELECTED";
				controllerOverride = true;
			}

			// RIGHT CLICK
			else if (OI.driverJoy.getRawButton(Buttons.RIGHT_CLICK)) {
				controllerOverride = false;
				System.out.println(autonString);
			}
		}

	}

	public void fillAutonArray() {
		defaultCommand = new GZCommand("DEFAULT", new DefaultAutonomous());

		commandArray[1] = new GZCommand("Middle - Switch", new MiddleAuton(AO.SWITCH, AV.CURRENT));
		commandArray[2] = new GZCommand("Left - Switch", new LeftAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		commandArray[3] = new GZCommand("Left - Scale", new LeftAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));
		commandArray[4] = new GZCommand("Right - Switch", new RightAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		commandArray[5] = new GZCommand("Right - Scale", new RightAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));

		commandArray[11] = new GZCommand("Left Only - Switch Priority",
				new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		commandArray[12] = new GZCommand("Left Only - Scale Priority",
				new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		commandArray[13] = new GZCommand("Left Only - Switch Only",
				new LeftAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		commandArray[14] = new GZCommand("Left Only - Scale Only",
				new LeftAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));

		commandArray[15] = new GZCommand("Right Only - Switch Priority",
				new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		commandArray[16] = new GZCommand("Right Only - Switch Priority",
				new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		commandArray[17] = new GZCommand("Right Only - Switch Only",
				new RightAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		commandArray[18] = new GZCommand("Right Only - Scale Only",
				new RightAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));

		commandArray[19] = new GZCommand("Left - Default", new LeftAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));
		commandArray[20] = new GZCommand("Right - Default", new RightAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));

		autonChooser();
	}

	public void startAuton()
	{
		fillAutonArray();
		
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	public boolean isDemo() {
		return uglyAnalog() == kAuton.SAFTEY_SWITCH;
	}

	private void printSelected() {
		m_asA = as_A.getValue();
		m_asB = as_B.getValue();

		// If overriden, print overide
		if (controllerOverride && (!overrideString.equals(overrideStringPrevious)))
			System.out.println(overrideString);

		if (((m_asA + 8 < m_prev_as1 || m_prev_as1 < m_asA - 8)
				|| (m_asB + 8 < m_prev_as2 || m_prev_as2 < m_asB - 8))) {

			if ((uglyAnalog() >= 1) && (uglyAnalog() <= 10)) {
				autonString = "A / " + uglyAnalog() + ": " + commandArray[uglyAnalog()].getName();
			} else if ((uglyAnalog() >= 11) && (uglyAnalog() <= 20)) {
				autonString = "B / " + (uglyAnalog() - 10) + ": " + commandArray[uglyAnalog()].getName() + " ("
						+ uglyAnalog() + ")";
			} else if ((uglyAnalog() >= 21) && (uglyAnalog() <= 30)) {
				autonString = "C / " + (uglyAnalog() - 20) + ": " + commandArray[uglyAnalog()].getName() + " ("
						+ uglyAnalog() + ")";
			} else if ((uglyAnalog() >= 31) && (uglyAnalog() <= 40)) {
				autonString = "D / " + (uglyAnalog() - 30) + ": " + commandArray[uglyAnalog()].getName() + " ("
						+ uglyAnalog() + ")";
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
		if (m_asA < Constants.kAuton.AUTO_1 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_1 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(0);

		} else if (m_asA < Constants.kAuton.AUTO_2 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_2 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(1);

		} else if (m_asA < Constants.kAuton.AUTO_3 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_3 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(2);

		} else if ((m_asA < Constants.kAuton.AUTO_4 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_4 - Constants.kAuton.AUTO_VARIANCE)) {
			return selectorB(3);

		} else if ((m_asA < Constants.kAuton.AUTO_5 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_5 - Constants.kAuton.AUTO_VARIANCE)) {
			return selectorB(4);

		} else if (m_asA < Constants.kAuton.AUTO_6 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_6 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(5);

		} else if (m_asA < Constants.kAuton.AUTO_7 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_7 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(6);

		} else if (m_asA < Constants.kAuton.AUTO_8 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_8 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(7);
		} else if (m_asA < Constants.kAuton.AUTO_9 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_9 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(8);

		} else if (m_asA < Constants.kAuton.AUTO_10 + Constants.kAuton.AUTO_VARIANCE
				&& m_asA > Constants.kAuton.AUTO_10 - Constants.kAuton.AUTO_VARIANCE) {
			return selectorB(9);

		} else {
			// ERROR
			return 3452;
		}

	}

	private int selectorB(int selectorA) {
		if (m_asB > Constants.kAuton.AUTO_1_L && m_asB < Constants.kAuton.AUTO_1_H) {
			return 1 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_2_L && m_asB < Constants.kAuton.AUTO_2_H) {
			return 2 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_3_L && m_asB < Constants.kAuton.AUTO_3_H) {
			return 3 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_4_L && m_asB < Constants.kAuton.AUTO_4_H) {
			return 4 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_5_L && m_asB < Constants.kAuton.AUTO_5_H) {
			return 5 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_6_L && m_asB < Constants.kAuton.AUTO_6_H) {
			return 6 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_7_L && m_asB < Constants.kAuton.AUTO_7_H) {
			return 7 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_8_L && m_asB < Constants.kAuton.AUTO_8_H) {
			return 8 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_9_L && m_asB < Constants.kAuton.AUTO_9_H) {
			return 9 + (selectorA * 10);
		} else if (m_asB > Constants.kAuton.AUTO_10_L && m_asB < Constants.kAuton.AUTO_10_H) {
			return 10 + (selectorA * 10);
		} else {
			// ERROR
			return 3452;
		}
	}

	public String gsm() {
		String f;
		f = DriverStation.getInstance().getGameSpecificMessage();

		if (f.length() > 0)
			gameMsg = f;
		else
			gameMsg = "NOT";
		
		return gameMsg;
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
