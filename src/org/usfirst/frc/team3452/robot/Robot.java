package org.usfirst.frc.team3452.robot;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.subsystems.Auton;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drive;
import org.usfirst.frc.team3452.robot.subsystems.Drive.DriveState;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Files;
import org.usfirst.frc.team3452.robot.subsystems.Files.TASK;
import org.usfirst.frc.team3452.robot.subsystems.Health;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.util.GZSubsystemManager;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	public static final Files files = new Files();

	public static final Drive drive = new Drive();
	public static final Elevator elevator = new Elevator();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();

	public static final Lights lights = new Lights();
	public static final Auton auton = new Auton();
	public static final Camera camera = new Camera();
	public static final GZOI gzOI = new GZOI();

	public static final GZSubsystemManager allSubsystems = new GZSubsystemManager(
			Arrays.asList(drive, elevator, intake, climber, lights, gzOI));

	public static final Health health = new Health();
	
	@SuppressWarnings("unused")
	private static final OI oi = new OI();

	// LOGGING CONTROL
	private boolean logging = false, logToUsb = true;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		allSubsystems.construct();
		auton.fillAutonArray();
		health.generateHealth();
		// TODO ISSUE #14
	}

	@Override
	public void robotPeriodic() {
		allSubsystems.loop();
	}

	@Override
	public void disabledInit() {
		allSubsystems.stop();

		log(false);
	}

	@Override
	public void disabledPeriodic() {
		auton.autonChooser();

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		log(true);

		// timer start
		auton.matchTimer.oneTimeStart();

		// Loop while game data is bad and timer is acceptable
		do {
		} while ((auton.gsm().equals("NOT") && auton.matchTimer.get() < 3));

		// Fill auton array and set values, regardless of good game message
		auton.startAuton();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		allSubsystems.enableFollower();

		log(true);

		if (auton.autonomousCommand != null) {
			auton.autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		// TODO ISSUE #19
		drive.setWantedState(DriveState.OPEN_LOOP_DRIVER);
		Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		log(true);
	}

	@Override
	public void testPeriodic() {
	}

	private void log(boolean startup) {
		if (logging)
			files.csvControl("Log", loggingLocation, logToUsb, TASK.Log, startup);
	}
}
