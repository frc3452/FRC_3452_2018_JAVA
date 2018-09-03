package org.usfirst.frc.team3452.robot;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.Constants.kAuton;
import org.usfirst.frc.team3452.robot.Constants.kLights;
import org.usfirst.frc.team3452.robot.subsystems.Auton;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.DriveState;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement;
import org.usfirst.frc.team3452.robot.subsystems.GZOI;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement.STATE;
import org.usfirst.frc.team3452.robot.subsystems.FileManagement.TASK;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.util.GZJoystick.Buttons;
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
	public static final GZOI gzOI = new GZOI();
	public static final Lights lights = new Lights();
	public static final Camera camera = new Camera();
	public static final FileManagement fileManager = new FileManagement();

	private static final GZSubsystemManager mSubsystems = new GZSubsystemManager(
			Arrays.asList(drive, elevator, intake, climber, gzOI));

	private static final OI oi = new OI();

	// Flags

	// LOGGING CONTROL
	private boolean logging = true, logToUsb = true;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		//TODO ISSUE #14
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
		Robot.lights.loop();

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		log(true);

		// timer start
		Robot.auton.matchTimer.oneTimeStart();

		// Loop while game data is bad and timer is acceptable
		do {
			Robot.auton.gsm();
		} while ((Robot.auton.gameMsg.equals("NOT") && Robot.auton.matchTimer.get() < 3));

		// Fill auton array and set values, regardless of good game message
		Robot.auton.setAutons();

		if (Robot.auton.autonomousCommand != null)
			Robot.auton.autonomousCommand.start();
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
		//TODO ISSUE #19
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

	private void log(boolean start) {
		if (logging)
			Robot.fileManager.control("Log", loggingLocation, logToUsb, TASK.Log,
					(start) ? STATE.STARTUP : STATE.FINISH);
	}
}
