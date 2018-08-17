package org.usfirst.frc.team3452.robot;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.Constants.kAutonSelector;
import org.usfirst.frc.team3452.robot.OI.CONTROLLER;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Climber2;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain2;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Elevator2;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Intake2;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.subsystems.Playback;
import org.usfirst.frc.team3452.robot.subsystems.Playback.STATE;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;
import org.usfirst.frc.team3452.robot.util.GZSubsystemManager;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	public static final Drivetrain drive = new Drivetrain();
	public static final Elevator elevator = new Elevator();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();
	public static final AutonSelector autonSelector = new AutonSelector();
	public static final Camera camera = new Camera();
	public static final Lights lights = new Lights();
	public static final Playback playback = new Playback();

	// TODO STATE TESTING CONSTRUCTION
	public static final Climber2 climber2 = new Climber2();
	public static final Intake2 intake2 = new Intake2();
	public static final Drivetrain2 drive2 = new Drivetrain2();
	public static final Elevator2 elevator2 = new Elevator2();
	private static final GZSubsystemManager mSubsystems = new GZSubsystemManager(
			Arrays.asList(climber2, intake2, drive2, elevator2));

	@SuppressWarnings("unused")
	private static final OI oi = new OI();


	// Flags
	private boolean wasTele = false, readyForMatch = false, wasTest = false, safeToLog = false;

	// LOGGING CONTROL
	private boolean logging = true, logToUsb = true;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
	}

	@Override
	public void robotPeriodic() {
		// TODO STATE TESTING CONSTRUCTION
		mSubsystems.loop();

		handleLEDs();
		Robot.drive.loggerUpdate();

		// LOGGING FLAG SET IN AUTOINIT, TELEINIT, TESTINIT
		// LOOPED HERE
		if (safeToLog && logging)
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.RUNTIME);
	}

	@Override
	public void disabledInit() {
		// TODO STATE TESTING CONSTRUCTION
		mSubsystems.stop();
		
		endLog();

		Robot.drive.brake((!wasTele) ? NeutralMode.Coast : NeutralMode.Brake);
		// Robot.drive.brake(NeutralMode.Coast);
	}

	@Override
	public void disabledPeriodic() {

		Robot.autonSelector.autonChooser();
		Robot.autonSelector.printSelected();

		if (wasTest)
			OI.rumble(CONTROLLER.BOTH, 1);
		else
			OI.rumble(CONTROLLER.BOTH, 0);

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		startLog();

		// timer start
		Robot.drive.timer.stop();
		Robot.drive.timer.reset();
		Robot.drive.timer.start();

		// keep overriding while game data bad
		do {
			Robot.autonSelector.gameMsg = Robot.lights.gsm();
		} while ((Robot.autonSelector.gameMsg.equals("NOT") && Robot.drive.timer.get() < 3));

		Robot.autonSelector.setAutons();

		// SET COLOR ACCORDING TO ALLIANCE
		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			Robot.lights.hsv(0, 1, .5);
		else
			Robot.lights.hsv(120, 1, .5);

		// BRAKE MODE DURING AUTO
		Robot.drive.brake(NeutralMode.Brake);

		Robot.autonSelector.autonChooser();
		Robot.autonSelector.printSelected();

		if (Robot.autonSelector.autonomousCommand != null)
			Robot.autonSelector.autonomousCommand.start();

	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		startLog();

		// GREEN LOW BRIGHTNESS
		Robot.lights.hsv(250, 1, .5);

		Robot.drive.brake(NeutralMode.Coast);

		if (Robot.autonSelector.autonomousCommand != null) {
			Robot.autonSelector.autonomousCommand.cancel();
		}

		wasTele = true;
	}

	@Override
	public void teleopPeriodic() {
		//TODO 4) FIND SOME WAY TO BETTER MANAGE TASKS THAN TODOS
		//TODO 2) A - VERIFY STOP AND DISABLE WORK FOR EACH SUBSYSTEM, TRY WITH SUBSYSTEMMANAGER
		
		//TODO 2) B - USE FOR TESTING
		//Comment out, just if joy.getrawbutton(1) disable else enable
		if (!wasTest)
			Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		System.out.println("Entering test mode");
		startLog();

		wasTest = true;
	}

	@Override
	public void testPeriodic() {
	}

	private void handleLEDs() {
		if (OI.driverJoy.getRawButton(2) && OI.driverJoy.getRawButton(7) && OI.driverJoy.getRawButton(8))
			readyForMatch = true;

		if (wasTest) {
			Robot.lights.pulse(55, 1, .2, .8, .1);
		} else {
			switch (Robot.autonSelector.uglyAnalog()) {
			case 100:

				// OFF
				Robot.lights.hsv(0, 0, 0);

				break;

			case kAutonSelector.SAFTEY_SWITCH:

				// FADE
				Robot.lights.hsv(Robot.lights.m_hue, 1, .25);
				Robot.lights.m_hue++;

				break;
			case 97:

				// POLICE
				if (Robot.lights.m_hue > 180)
					Robot.lights.hsv(0, 1, 1);
				else
					Robot.lights.hsv(120, 1, 1);
				Robot.lights.m_hue += 30;

				break;
			default:

				if (DriverStation.getInstance().isDisabled()) {
					// IF CONNECTED LOW GREEN
					if (DriverStation.getInstance().isDSAttached()) {

						if (readyForMatch)
							Robot.lights.pulse(258, 1, 0.1, .4, 0.025 / 3.5);
						else
							Robot.lights.pulse(330, 1, .1, .4, 0.025 / 3.5);

					} else {
						// IF NOT CONNECTED DO AGGRESSIVE RED PULSE
						Robot.lights.pulse(0, 1, 0.2, .8, 0.15 / 10 * (Robot.drive.timer.get() / 100));
					}
				}
				break;
			}
		}
	}

	private void startLog() {
		if (!safeToLog && logging) {
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.STARTUP);
			safeToLog = true;
		}
	}

	private void endLog() {
		if (safeToLog && logging) {
			safeToLog = false;
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.FINISH);
		}
	}
}
