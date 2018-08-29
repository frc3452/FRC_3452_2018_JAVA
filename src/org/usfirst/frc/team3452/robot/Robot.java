package org.usfirst.frc.team3452.robot;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.Constants.kAuton;
import org.usfirst.frc.team3452.robot.Constants.kLights;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.subsystems.Auton;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.DriveState;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement.STATE;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement.TASK;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.util.GZSubsystemManager;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {

	public static final Drivetrain drive = new Drivetrain();
	public static final Elevator elevator = new Elevator();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();

	public static final Auton auton = new Auton();
	public static final Camera camera = new Camera();
	public static final Lights lights = new Lights();
	public static final FileManagement fileManager = new FileManagement();

	private static final GZSubsystemManager mSubsystems = new GZSubsystemManager(
			Arrays.asList(drive, elevator, intake, climber));

	@SuppressWarnings("unused")
	private static final OI oi = new OI();

	// Flags
	private boolean wasTele = false, readyForMatch = false, wasTest = false;

	// LOGGING CONTROL
	private boolean logging = true, logToUsb = true;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
	}

	@Override
	public void robotPeriodic() {
		mSubsystems.loop();

		handleLEDs();
	}

	@Override
	public void disabledInit() {
		mSubsystems.stop();

		log(false);

		Robot.drive.brake((!wasTele) ? NeutralMode.Coast : NeutralMode.Brake);
		// Robot.drive.brake(NeutralMode.Coast);
	}

	@Override
	public void disabledPeriodic() {
		Robot.auton.autonChooser();
		Robot.auton.printSelected();

		if (wasTest)
			OI.rumble(CONTROLLER.BOTH, 1);
		else
			OI.rumble(CONTROLLER.BOTH, 0);

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		log(true);

		// timer start
		Robot.auton.resetAutonTimer();
		Robot.auton.startAutonTimer();

		// Loop while game data is bad and timer is acceptable
		do {
			Robot.auton.gameMsg = Robot.lights.gsm();
		} while ((Robot.auton.gameMsg.equals("NOT") && Robot.auton.getAutonTimer() < 3));

		// Set autons, regardless of good game message
		Robot.auton.setAutons();

		// SET COLOR ACCORDING TO ALLIANCE
		Robot.lights.hsv(Robot.auton.isRed() ? kLights.RED : kLights.BLUE, 1, .5);

		// BRAKE MODE DURING AUTO
		Robot.drive.brake(NeutralMode.Brake);

		Robot.auton.autonChooser();
		Robot.auton.printSelected();

		if (Robot.auton.autonomousCommand != null)
			Robot.auton.autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		log(true);

		Robot.drive.setWantedState(DriveState.OPEN_LOOP_DRIVER);

		Robot.lights.hsv(kLights.GREEN, 1, .5);

		Robot.drive.brake(NeutralMode.Coast);

		if (Robot.auton.autonomousCommand != null) {
			Robot.auton.autonomousCommand.cancel();
		}

		wasTele = true;
	}

	@Override
	public void teleopPeriodic() {
		if (!wasTest)
			Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		log(true);

		wasTest = true;
	}

	@Override
	public void testPeriodic() {
//		Robot.climber.control(-.35);
	}

	private void handleLEDs() {
		if (OI.driverJoy.getRawButton(2) && OI.driverJoy.getRawButton(7) && OI.driverJoy.getRawButton(8))
			readyForMatch = true;

		if (wasTest) {
			Robot.lights.pulse(kLights.PURPLE, 1, .2, .8, .1);
		} else {
			switch (Robot.auton.uglyAnalog()) {
			case 100:

				// OFF
				Robot.lights.off();

				break;

			case kAuton.SAFTEY_SWITCH:

				// FADE
				Robot.lights.hsv(Robot.lights.m_hue, 1, .25);
				Robot.lights.m_hue++;

				break;
			case 97:

				// POLICE
				if (Robot.lights.m_hue > 180)
					Robot.lights.hsv(kLights.RED, 1, 1);
				else
					Robot.lights.hsv(kLights.BLUE, 1, 1);
				Robot.lights.m_hue += 30;

				break;
			default:

				if (DriverStation.getInstance().isDisabled()) {
					// IF CONNECTED LOW GREEN
					if (DriverStation.getInstance().isDSAttached()) {

						if (readyForMatch)
							Robot.lights.pulse(kLights.GREEN, 1, 0.1, .4, 0.025 / 3.5);
						else
							Robot.lights.pulse(kLights.YELLOW, 1, 0.1, .4, 0.025 / 3.5);

					} else {
						// IF NOT CONNECTED DO AGGRESSIVE RED PULSE
						Robot.lights.pulse(0, 1, 0.2, .8, 0);
					}
				}
				break;
			}
		}
	}

	private void log(boolean start) {
		if (logging)
			Robot.fileManager.control("Log", loggingLocation, logToUsb, TASK.Log,
					(start) ? STATE.STARTUP : STATE.FINISH);
	}
}
