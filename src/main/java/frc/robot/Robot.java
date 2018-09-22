package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Files;
import frc.robot.subsystems.Files.TASK;
import frc.robot.subsystems.Health;
import frc.robot.util.GZSubsystemManager;

public class Robot extends TimedRobot {
	public static final Files files = new Files();

	public static final Drive drive = new Drive();

	public static final GZOI gzOI = new GZOI();

	public static final OI oi = new OI();

	public static final GZSubsystemManager allSubsystems = new GZSubsystemManager(
			Arrays.asList(drive, gzOI));

	public static final Health health = new Health();
	
	// LOGGING CONTROL
	private boolean logging = true, logToUsb = false;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		allSubsystems.construct();
		files.fillLogger();
		health.generateHealth();
	}

	@Override
	public void robotPeriodic() {
		allSubsystems.loop();
	}

	@Override
	public void disabledInit() {
		log(false);
	}

	@Override
	public void disabledPeriodic() {
	}

	@Override
	public void autonomousInit() {
		log(true);
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		allSubsystems.enableFollower();
		log(true);
	}

	@Override
	public void teleopPeriodic() {
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
