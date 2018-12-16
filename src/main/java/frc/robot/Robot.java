package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Health;
import frc.robot.util.GZFiles;
import frc.robot.util.GZFiles.TASK;
import frc.robot.util.GZSubsystemManager;
import frc.robot.util.PersistentInfoManager;

public class Robot extends TimedRobot {
	// Force construction of files first
	private GZFiles files = GZFiles.getInstance();

	// This order is crucial! it determines what order logging is added, what order
	// health is generated in, etc
	public static final GZSubsystemManager allSubsystems = new GZSubsystemManager(
			Arrays.asList(Drive.getInstance(), GZOI.getInstance()));

	private Health health = Health.getInstance();

	private PersistentInfoManager infoManager = PersistentInfoManager.getInstance();

	// LOGGING CONTROL
	private final boolean logging = true, logToUsb = true;
	private final String loggingLocation = "GreengineerZLogging/DrivePractice121618";

	@Override
	public void robotInit() {
		health.assignSubsystems(allSubsystems.getSubsystems());

		infoManager.initialize();

		// Gen health file
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
		infoManager.printPersistentSettings();
		infoManager.robotDisabled();
		allSubsystems.stop();
		log(false);
	}

	@Override
	public void disabledPeriodic() {
	}

	private void enabledInits() {
		infoManager.robotEnabled();
		allSubsystems.enableFollower();
		log(true);
	}

	@Override
	public void autonomousInit() {
		enabledInits();
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
