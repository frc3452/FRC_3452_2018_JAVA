
package org.usfirst.frc.team3452.robot;

import org.usfirst.frc.team3452.robot.commands.drive.SetModify;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorManual;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.OverrideSet;
import org.usfirst.frc.team3452.robot.commands.pwm.Climb;
import org.usfirst.frc.team3452.robot.commands.pwm.ClimbRelease;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeManual;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeSpin;
import org.usfirst.frc.team3452.robot.triggers.DriveSafteyOverriden;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class OI {
	public static Joystick driverJoy = new Joystick(0);
	public static Joystick opJoy = new Joystick(1);

	public static DriveSafteyOverriden driveSafteyOverriden = new DriveSafteyOverriden();

	@SuppressWarnings("unused")
	private static Button driverJoyA, driverJoyB, driverJoyX, driverJoyY, driverJoyLB, driverJoyRB, driverJoyBack,
			driverJoyStart, driverJoyLClick, driverJoyRClick;

	@SuppressWarnings("unused")
	private static Button opJoyA, opJoyB, opJoyX, opJoyY, opJoyLB, opJoyRB, opJoyBack, opJoyStart, opJoyLClick,
			opJoyRClick;

	public static void init() {
		buttonInit();

		//		// DRIVER JOY
		driverJoyA.whenPressed(new SetModify(-1));

		driverJoyX.whileHeld(new IntakeManual(-.75));
		driverJoyB.whileHeld(new IntakeManual(.75));
		driverJoyY.whileHeld(new Climb(.7));

		driverJoyRB.whileHeld(new ElevatorManual(driverJoy));

		driverJoyBack.whenPressed(new OverrideSet(-1));
		driverJoyStart.whenPressed(new ClimbRelease(1, .5));

		// OP JOY
		opJoyLB.whileHeld(new ElevatorManual(opJoy));

		opJoyBack.whileHeld(new IntakeSpin(.35, true));
		opJoyStart.whileHeld(new IntakeSpin(.35, false));

		opJoyX.whileHeld(new IntakeManual(-.75));
		opJoyB.whileHeld(new IntakeManual(.75));
		opJoyY.whileHeld(new IntakeManual(.3));
		opJoyA.whenPressed(new ElevatorPosition(3.5));

		//		opJoyLClick.whenPressed(new CameraSwitch(0));
		//		opJoyRClick.whenPressed(new CameraSwitch(1));

		driveSafteyOverriden.whenActive(new OverrideSet(1));
		driveSafteyOverriden.whenInactive(new OverrideSet(0));
	}

	private static void buttonInit() {
		driverJoy = new Joystick(0);
		driverJoyA = new JoystickButton(driverJoy, 1);
		driverJoyB = new JoystickButton(driverJoy, 2);
		driverJoyX = new JoystickButton(driverJoy, 3);
		driverJoyY = new JoystickButton(driverJoy, 4);
		driverJoyLB = new JoystickButton(driverJoy, 5);
		driverJoyRB = new JoystickButton(driverJoy, 6);
		driverJoyBack = new JoystickButton(driverJoy, 7);
		driverJoyStart = new JoystickButton(driverJoy, 8);
		driverJoyLClick = new JoystickButton(driverJoy, 9);
		driverJoyRClick = new JoystickButton(driverJoy, 10);

		opJoy = new Joystick(1);
		opJoyA = new JoystickButton(opJoy, 1);
		opJoyB = new JoystickButton(opJoy, 2);
		opJoyX = new JoystickButton(opJoy, 3);
		opJoyY = new JoystickButton(opJoy, 4);
		opJoyLB = new JoystickButton(opJoy, 5);
		opJoyRB = new JoystickButton(opJoy, 6);
		opJoyBack = new JoystickButton(opJoy, 7);
		opJoyStart = new JoystickButton(opJoy, 8);
		opJoyLClick = new JoystickButton(opJoy, 9);
		opJoyRClick = new JoystickButton(opJoy, 10);
	}
}
