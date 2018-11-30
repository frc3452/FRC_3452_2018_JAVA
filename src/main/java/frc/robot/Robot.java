package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Auton;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Health;
import frc.robot.util.GZFiles;
import frc.robot.util.GZUtil;
import frc.robot.util.GZFiles.TASK;
import frc.robot.util.GZSubsystemManager;

public class Robot extends TimedRobot {
	//Force construction of files first
	private GZFiles files = GZFiles.getInstance();

	
	public static final GZSubsystemManager allSubsystems = new GZSubsystemManager(
			Arrays.asList(Drive.getInstance(),GZOI.getInstance()));
	
	private Health health = Health.getInstance();
	private Auton auton = Auton.getInstance();


	// LOGGING CONTROL
	private final boolean logging = true, logToUsb = true;
	private final String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		files.fillLogger();
		auton.fillAutonArray();

		// allSubsystems.startLooping();

		health.generateHealth();
	}

	@Override
	public void robotPeriodic() {
		System.out.println(Drive.getInstance().getTemperatureOfMotor(Constants.kDrivetrain.L1));

		if (!GZOI.getInstance().isTest())
			allSubsystems.loop();
	}

	@Override
	public void disabledInit() {
		allSubsystems.stop();

		log(false);
	}

	@Override
	public void disabledPeriodic() {
		// auton.autonChooser();
	}

	@Override
	public void autonomousInit() {
		allSubsystems.enableFollower();

		log(true);

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
		allSubsystems.enableFollower();

		log(true);

		if (auton.autonomousCommand != null) {
			auton.autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		allSubsystems.loop();
	}

	@Override
	public void testInit() {
		log(true);
	}

	@Override
	public void testPeriodic() {
		Scheduler.getInstance().run();
		// allSubsystems.loop();
	}

	private void log(boolean startup) {
		if (logging)
			files.csvControl("Log", loggingLocation, logToUsb, TASK.Log, startup);
	}
}
