
package org.usfirst.frc.team3452.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Constants.kAuton;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.auton.LeftAuton;
import org.usfirst.frc.team3452.robot.commands.auton.MiddleAuton;
import org.usfirst.frc.team3452.robot.commands.auton.RightAuton;
import org.usfirst.frc.team3452.robot.util.GZCommand;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;

/**
 * <h1>AutonSelector Subsystem</h1> Handles autonomous selector case statements
 * and printing.
 * 
 * @author max
 *
 */
public class Auton {

	public AnalogInput as_A;
	public AnalogInput as_B;

	private int m_prev_as1, m_prev_as2;
	private int m_asA, m_asB;

	public GZCommand autoCommand[] = new GZCommand[41];
	public Command autonomousCommand = null;
	public Command defaultCommand = null;

	public boolean controllerOverride = false;

	public int overrideValue = 1;
	private String overrideStringPrevious = "";
	public String overrideString = "", autonString = "";
	public String gameMsg = "NOT";

	private Timer mAutoTimer = new Timer();
	
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

		Arrays.fill(autoCommand, null);
	}
	
	public void startAutonTimer()
	{
		mAutoTimer.start();
	}
	
	public Double getAutonTimer()
	{
		return mAutoTimer.get();
	}
	
	public void resetAutonTimer()
	{
		mAutoTimer.stop();
		mAutoTimer.reset();
	}

	public void autonChooser() {
		controllerChooser();

		// if not overriden
		if (!controllerOverride) {

			// If selector feedback nominal
			if (uglyAnalog() <= 40 && uglyAnalog() >= 1) {
				autonomousCommand = autoCommand[Robot.auton.uglyAnalog()].getCommand();
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
				overrideString = "Controller override 1:\t" + autoCommand[2].getName();
				controllerOverride = true;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				overrideValue = 3;
				overrideString = "Controller override 2:\t" + autoCommand[3].getName();
				controllerOverride = true;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				overrideValue = 4;
				overrideString = "Controller override 3:\t" + autoCommand[3].getName();
				controllerOverride = true;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				overrideValue = 5;
				overrideString = "Controller override 4:\t" + autoCommand[5].getName();
				controllerOverride = true;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				overrideValue = 13;
				overrideString = "Controller override 5:\t" + autoCommand[13].getName();
				controllerOverride = true;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				overrideValue = 17;
				overrideString = "Controller override 6:\t" + autoCommand[17].getName();
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

	public void setAutons() {
		autoCommand[1] = new GZCommand("Middle - Switch", new MiddleAuton(AO.SWITCH, AV.CURRENT));
		autoCommand[2] = new GZCommand("Left - Switch", new LeftAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		autoCommand[3] = new GZCommand("Left - Scale", new LeftAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));
		autoCommand[4] = new GZCommand("Right - Switch", new RightAuton(AO.SWITCH, AV.CURRENT, AV.CURRENT));
		autoCommand[5] = new GZCommand("Right - Scale", new RightAuton(AO.SCALE, AV.CURRENT, AV.CURRENT));

		autoCommand[11] = new GZCommand("Left Only - Switch Priority",
				new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[12] = new GZCommand("Left Only - Scale Priority",
				new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[13] = new GZCommand("Left Only - Switch Only",
				new LeftAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		autoCommand[14] = new GZCommand("Left Only - Scale Only", new LeftAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));

		autoCommand[15] = new GZCommand("Right Only - Switch Priority",
				new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[16] = new GZCommand("Right Only - Switch Priority",
				new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT, AV.CURRENT));
		autoCommand[17] = new GZCommand("Right Only - Switch Only",
				new RightAuton(AO.SWITCH_ONLY, AV.CURRENT, AV.CURRENT));
		autoCommand[18] = new GZCommand("Right Only - Scale Only",
				new RightAuton(AO.SCALE_ONLY, AV.CURRENT, AV.CURRENT));

		autoCommand[19] = new GZCommand("Left - Default", new LeftAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));
		autoCommand[20] = new GZCommand("Right - Default", new RightAuton(AO.DEFAULT, AV.CURRENT, AV.CURRENT));

		if (Robot.auton.controllerOverride)
			autonomousCommand = autoCommand[Robot.auton.overrideValue].getCommand();
	}

	public boolean isDemo() {
		return uglyAnalog() == kAuton.SAFTEY_SWITCH;
	}

	public boolean isFMS() {
		return DriverStation.getInstance().isFMSAttached();
	}

	public boolean isRed()
	{
		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			return true;

		return false;
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
				autonString = "A / " + uglyAnalog() + ": " + autoCommand[uglyAnalog()].getName();
			} else if ((uglyAnalog() >= 11) && (uglyAnalog() <= 20)) {
				autonString = "B / " + (uglyAnalog() - 10) + ": " + autoCommand[uglyAnalog()].getName() + " ("
						+ uglyAnalog() + ")";
			} else if ((uglyAnalog() >= 21) && (uglyAnalog() <= 30)) {
				autonString = "C / " + (uglyAnalog() - 20) + ": " + autoCommand[uglyAnalog()].getName() + " ("
						+ uglyAnalog() + ")";

			} else if ((uglyAnalog() >= 31) && (uglyAnalog() <= 40)) {
				autonString = "D / " + (uglyAnalog() - 30) + ": " + autoCommand[uglyAnalog()].getName() + " ("
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
