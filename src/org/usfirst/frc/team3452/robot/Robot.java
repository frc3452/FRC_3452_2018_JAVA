package org.usfirst.frc.team3452.robot;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.subsystems.Auton;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drive;
import org.usfirst.frc.team3452.robot.subsystems.Drive.DriveState;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement.TASK;
import org.usfirst.frc.team3452.robot.subsystems.Health;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.util.GZSubsystemManager;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;

public class Robot extends TimedRobot {
	public static final Health health = new Health();
	public static final FileManagement fileManager = new FileManagement();
	
	public static final Drive drive = new Drive();
	public static final Elevator elevator = new Elevator();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();

	public static final Lights lights = new Lights();
	public static final Auton auton = new Auton();
	public static final Camera camera = new Camera();
	public static final GZOI gzOI = new GZOI();

	public static final GZSubsystemManager mSubsystems = new GZSubsystemManager(
			Arrays.asList(gzOI, drive, elevator, intake, climber, lights));

	private static final OI oi = new OI();

	// Flags

	// LOGGING CONTROL
	private boolean logging = false, logToUsb = true;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		Robot.auton.fillAutonArray();

		// TODO ISSUE #14
	}

	@Override
	public void robotPeriodic() {
		mSubsystems.loop();
	}

	@Override
	public void disabledInit() {
		mSubsystems.stop();

		log(false);
	}

	@Override
	public void disabledPeriodic() {
		Robot.auton.autonChooser();

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		log(true);

		// timer start
		Robot.auton.matchTimer.oneTimeStart();

		// Loop while game data is bad and timer is acceptable
		do {
		} while ((Robot.auton.gsm().equals("NOT") && Robot.auton.matchTimer.get() < 3));

		// Fill auton array and set values, regardless of good game message
		Robot.auton.startAuton();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		mSubsystems.enableFollower();

		log(true);

		if (Robot.auton.autonomousCommand != null) {
			Robot.auton.autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		// TODO ISSUE #19
		Robot.drive.setWantedState(DriveState.OPEN_LOOP_DRIVER);
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
			Robot.fileManager.csvControl("Log", loggingLocation, logToUsb, TASK.Log, startup);
	}
}
