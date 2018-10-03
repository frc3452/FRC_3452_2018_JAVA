package frc.robot;

import java.util.Arrays;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Scheduler;
import frc.robot.subsystems.Auton;
import frc.robot.subsystems.Camera;
import frc.robot.subsystems.Climber;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Elevator;
import frc.robot.subsystems.Files;
import frc.robot.subsystems.Files.TASK;
import frc.robot.subsystems.Health;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Lights;
import frc.robot.util.GZSubsystemManager;

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
	
	// private static final OI oi = new OI();

	// LOGGING CONTROL
	private boolean logging = true, logToUsb = false;
	private String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		/**
		Construction of each subsystem moved out of constructor, into construct(). 
		The rationale for this was because we needed to use the List of all subsystems 
		to setup the map in  health, so it was easiest to instantiate each hardware 
		subsystem object, put them in a list, construct health, then use .construct() 
		on all subsystems to initialize hardware. Health had to be instantiated before 
		each other subsystems construction because to construct, health.addAlert() was 
		necessary.
		**/
		allSubsystems.construct();
	
		files.fillLogger();
		auton.fillAutonArray();
		
		// allSubsystems.startLooping();
	
		health.generateHealth();
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
	}

	@Override
	public void autonomousInit() {
		allSubsystems.enableFollower();

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
