package org.usfirst.frc.team3452.robot;

import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.elevator.OverrideSet;
import org.usfirst.frc.team3452.robot.triggers.DriveSafteyOverriden;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj.command.Command;

public class OI {
	public static Joystick driverJoy = new Joystick(0);
	public static Joystick operatorJoy = new Joystick(1);
	public static DriveSafteyOverriden driveSafteyOverriden = new DriveSafteyOverriden();

	private static Button driverJoyA = new JoystickButton(driverJoy, 1), driverJoyB = new JoystickButton(driverJoy, 2),
			driverJoyX = new JoystickButton(driverJoy, 3), driverJoyY = new JoystickButton(driverJoy, 4),
			driverJoyLB = new JoystickButton(driverJoy, 5), driverJoyRB = new JoystickButton(driverJoy, 6),
			driverJoyBack = new JoystickButton(driverJoy, 7), driverJoyStart = new JoystickButton(driverJoy, 8),
			driverJoyLClick = new JoystickButton(driverJoy, 9), driverJoyRClick = new JoystickButton(driverJoy, 10);

	public static void init() {
		 driverJoyA.whenPressed(new DriveTime(1,0,.5));

		driveSafteyOverriden.whenActive(new OverrideSet(true));
		driveSafteyOverriden.whenInactive(new OverrideSet(false));
	}
}
