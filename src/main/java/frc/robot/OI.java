package frc.robot;

import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.Constants.kElevator;
import frc.robot.Constants.kIntake;
import frc.robot.commands.elevator.ElevatorManual;
import frc.robot.commands.elevator.ElevatorPosition;
import frc.robot.commands.elevator.OverrideSet;
import frc.robot.commands.pwm.Climb;
import frc.robot.commands.pwm.IntakeManual;
import frc.robot.commands.pwm.IntakeSpin;
import frc.robot.subsystems.Elevator.ESO;
import frc.robot.triggers.DriveSafteyOverriden;
import frc.robot.util.DPad;
import frc.robot.util.GZJoystick;

public class OI {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	public static DriveSafteyOverriden driveSafteyOverriden = new DriveSafteyOverriden();

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

		// DRIVER JOY
		driverJoyX.whileHeld(new IntakeManual(kIntake.Speeds.INTAKE));
		driverJoyB.whileHeld(new IntakeManual(kIntake.Speeds.SHOOT));

		driverJoyY.whileHeld(new Climb(1));
		driverJoyRB.whileHeld(new ElevatorManual(driverJoy));
		driverJoyBack.whenPressed(new OverrideSet(ESO.TOGGLE));

		// DPAD
		driverDown.whenPressed(new ElevatorPosition(kElevator.Heights.Floor));
		driverUp.whileHeld(new IntakeManual(kIntake.Speeds.SLOW));
		driverLeft.whenPressed(new ElevatorPosition(kElevator.Heights.Scale));
		driverRight.whenPressed(new ElevatorPosition(kElevator.Heights.Switch));

		// OP JOY
		opJoyLB.whileHeld(new ElevatorManual(opJoy));
		// opJoyRB.whenPressed(new JustGonnaSendIt());

		opJoyA.whileHeld(new IntakeManual(kIntake.Speeds.PLACE));
		opJoyX.whileHeld(new IntakeManual(kIntake.Speeds.INTAKE));
		opJoyB.whileHeld(new IntakeManual(kIntake.Speeds.SHOOT));
		opJoyY.whileHeld(new IntakeManual(kIntake.Speeds.SLOW));

		opJoyBack.whileHeld(new IntakeSpin(kIntake.Speeds.SPIN, true));
		opJoyStart.whileHeld(new IntakeSpin(kIntake.Speeds.SPIN, false));

		// DPAD
		opDown.whenPressed(new ElevatorPosition(kElevator.Heights.Floor));
		opRight.whenPressed(new ElevatorPosition(kElevator.Heights.Switch));

		// TRIGGERS
		driveSafteyOverriden.whenActive(new OverrideSet(ESO.ON));
		driveSafteyOverriden.whenInactive(new OverrideSet(ESO.OFF));
	}

	private static void buttonInit() {

		driverJoy = new GZJoystick(0);
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

		driverUp = new DPad(driverJoy, 0);
		driverDown = new DPad(driverJoy, 180);
		driverLeft = new DPad(driverJoy, 270);
		driverRight = new DPad(driverJoy, 90);

		opJoy = new GZJoystick(1);
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

		opUp = new DPad(opJoy, 0);
		opDown = new DPad(opJoy, 180);
		opLeft = new DPad(opJoy, 270);
		opRight = new DPad(opJoy, 90);
	}
}