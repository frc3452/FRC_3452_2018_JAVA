package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Auton;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Health;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.util.GZFiles;
import frc.robot.util.GZFiles.TASK;
import frc.robot.util.GZSubsystemManager;
import frc.robot.util.GZTimer;

public class Robot extends TimedRobot {
	//Force construction of files first
	private GZFiles files = GZFiles.getInstance();
	
	//This order is crucial! it determines what order logging is added, what order health is generated in, etc
	public static final GZSubsystemManager allSubsystems = new GZSubsystemManager(
			Arrays.asList(Drive.getInstance(), Elevator.getInstance(), Intake.getInstance(), Climber.getInstance(),
					Lights.getInstance(), GZOI.getInstance()));
	
	private Health health = Health.getInstance();
	private Auton auton = Auton.getInstance();


	// LOGGING CONTROL
	private final boolean logging = true, logToUsb = true;
	private final String loggingLocation = "Logging/Programming";

	@Override
	public void robotInit() {
		files.robotTurnedOn();

		//Gen health file
		health.generateHealth();

		allSubsystems.addLoggingValues();
		// allSubsystems.startLooping();
	}

	@Override
	public void robotPeriodic() {
		if (!GZOI.getInstance().isTest())
			allSubsystems.loop();
	}

	@Override
	public void disabledInit() {
		files.robotDisabled();

		allSubsystems.stop();
		log(false);
	}

	@Override
	public void disabledPeriodic() {
		auton.autonChooser();
	}

	private void enabledInits()
	{
		files.robotEnabled();
		allSubsystems.enableFollower();
		log(true);
	}

	@Override
	public void autonomousInit() {
		enabledInits();

		// timer start
		auton.matchTimer.oneTimeStartTimer();

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
		enabledInits();
	}

	@Override
	public void teleopPeriodic() {
	}

	@Override
	public void testInit() {
		enabledInits();
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
	}

	private void log(boolean startup) {
		if (logging)
			files.csvControl("Log", loggingLocation, logToUsb, TASK.Log, startup);
	}
}
