package frc.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.drive.SpeedModify;
import frc.robot.util.DPad;
import frc.robot.util.GZJoystick;

public class OI {
	@SuppressWarnings("unused")
	private static Button driverJoyA, driverJoyB, driverJoyX, driverJoyY, driverJoyLB, driverJoyRB, driverJoyBack,
			driverJoyStart, driverJoyLClick, driverJoyRClick;

	private static DPad driverUp, driverDown, driverLeft, driverRight;

	@SuppressWarnings("unused")
	private static Button opJoyA, opJoyB, opJoyX, opJoyY, opJoyLB, opJoyRB, opJoyBack, opJoyStart, opJoyLClick,
			opJoyRClick;

	@SuppressWarnings("unused")
	private static DPad opUp, opDown, opLeft, opRight;

	public OI() {
		buttonInit();

		driverJoyA.whenPressed(new SpeedModify());
	}

	private static void buttonInit() {
		driverJoyA = new JoystickButton(GZOI.driverJoy, 1);
		driverJoyB = new JoystickButton(GZOI.driverJoy, 2);
		driverJoyX = new JoystickButton(GZOI.driverJoy, 3);
		driverJoyY = new JoystickButton(GZOI.driverJoy, 4);
		driverJoyLB = new JoystickButton(GZOI.driverJoy, 5);
		driverJoyRB = new JoystickButton(GZOI.driverJoy, 6);
		driverJoyBack = new JoystickButton(GZOI.driverJoy, 7);
		driverJoyStart = new JoystickButton(GZOI.driverJoy, 8);
		driverJoyLClick = new JoystickButton(GZOI.driverJoy, 9);
		driverJoyRClick = new JoystickButton(GZOI.driverJoy, 10);

		driverUp = new DPad(GZOI.driverJoy, 0);
		driverDown = new DPad(GZOI.driverJoy, 180);
		driverLeft = new DPad(GZOI.driverJoy, 270);
		driverRight = new DPad(GZOI.driverJoy, 90);

		opJoyA = new JoystickButton(GZOI.opJoy, 1);
		opJoyB = new JoystickButton(GZOI.opJoy, 2);
		opJoyX = new JoystickButton(GZOI.opJoy, 3);
		opJoyY = new JoystickButton(GZOI.opJoy, 4);
		opJoyLB = new JoystickButton(GZOI.opJoy, 5);
		opJoyRB = new JoystickButton(GZOI.opJoy, 6);
		opJoyBack = new JoystickButton(GZOI.opJoy, 7);
		opJoyStart = new JoystickButton(GZOI.opJoy, 8);
		opJoyLClick = new JoystickButton(GZOI.opJoy, 9);
		opJoyRClick = new JoystickButton(GZOI.opJoy, 10);

		opUp = new DPad(GZOI.opJoy, 0);
		opDown = new DPad(GZOI.opJoy, 180);
		opLeft = new DPad(GZOI.opJoy, 270);
		opRight = new DPad(GZOI.opJoy, 90);
	}
}