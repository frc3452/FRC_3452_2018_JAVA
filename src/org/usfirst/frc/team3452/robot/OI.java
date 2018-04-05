package org.usfirst.frc.team3452.robot;

import org.usfirst.frc.team3452.robot.commands.drive.SetModify;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorManual;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.OverrideSet;
import org.usfirst.frc.team3452.robot.commands.playback.Record;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeManual;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeSpin;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;
import org.usfirst.frc.team3452.robot.triggers.DriveSafteyOverriden;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
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

	private static HIDPOVButton driverUp, driverDown, driverLeft, driverRight;

	@SuppressWarnings("unused")
	private static Button opJoyA, opJoyB, opJoyX, opJoyY, opJoyLB, opJoyRB, opJoyBack, opJoyStart, opJoyLClick,
			opJoyRClick;

	private static HIDPOVButton opUp, opDown, opLeft, opRight;

	public static void init() {
		buttonInit();

		// 				DRIVER JOY
		driverJoyA.whenPressed(new SetModify(-1));

		driverJoyX.whileHeld(new IntakeManual(-.8));
		driverJoyB.whileHeld(new IntakeManual(.8));

//		driverJoyY.whileHeld(new Climb(1));
		//TODO PLAYBACK
		driverJoyY.whileHeld(new Record("Recording1", TASK.PARSE));
		
		driverJoyRB.whileHeld(new ElevatorManual(driverJoy));

		driverJoyBack.whenPressed(new OverrideSet(-1));

		//				DPAD
		driverDown.whenPressed(new ElevatorPosition(-15));
		driverUp.whenPressed(new ElevatorPosition(8.2));
		driverRight.whenPressed(new ElevatorPosition(3.5));
		driverLeft.whenPressed(new ElevatorPosition(.6));

		// 				OP JOY
		opJoyLB.whileHeld(new ElevatorManual(opJoy));
		opJoyX.whileHeld(new IntakeManual(-.8));
		opJoyB.whileHeld(new IntakeManual(.8));
		opJoyY.whileHeld(new IntakeManual(.3));

		opJoyBack.whileHeld(new IntakeSpin(.35, true));
		opJoyStart.whileHeld(new IntakeSpin(.35, false));

		//				DPAD
		opDown.whenPressed(new ElevatorPosition(-15));
		opUp.whenPressed(new ElevatorPosition(8.2));
		opRight.whenPressed(new ElevatorPosition(3.5));
		opLeft.whenPressed(new ElevatorPosition(.6));

		//				TRIGGERS
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

		driverUp = new HIDPOVButton(driverJoy, 0);
		driverDown = new HIDPOVButton(driverJoy, 180);
		driverLeft = new HIDPOVButton(driverJoy, 270);
		driverRight = new HIDPOVButton(driverJoy, 90);

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

		opUp = new HIDPOVButton(opJoy, 0);
		opDown = new HIDPOVButton(opJoy, 180);
		opLeft = new HIDPOVButton(opJoy, 270);
		opRight = new HIDPOVButton(opJoy, 90);

	}

	public static void rumble(int controller, double intensity) {
		if (controller == 1) {
			driverJoy.setRumble(RumbleType.kLeftRumble, intensity);
			driverJoy.setRumble(RumbleType.kRightRumble, intensity);
		} else if (controller == 2) {
			opJoy.setRumble(RumbleType.kLeftRumble, intensity);
			opJoy.setRumble(RumbleType.kRightRumble, intensity);
		} else if (controller == 3) {
			rumble(1, intensity);
			rumble(2, intensity);
		}
	}
}
