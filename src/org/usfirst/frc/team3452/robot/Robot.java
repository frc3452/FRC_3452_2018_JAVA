package org.usfirst.frc.team3452.robot;

import org.usfirst.frc.team3452.robot.commands.auton.DefaultAutonomous;
import org.usfirst.frc.team3452.robot.commands.auton.LeftAuton;
import org.usfirst.frc.team3452.robot.commands.auton.MiddleAuton;
import org.usfirst.frc.team3452.robot.commands.auton.RightAuton;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AO;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AV;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain.CONTROLLER;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;
import org.usfirst.frc.team3452.robot.subsystems.Playback;
import org.usfirst.frc.team3452.robot.subsystems.Playback.STATE;
import org.usfirst.frc.team3452.robot.subsystems.Playback.TASK;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
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
	public static OI _oi;

	//auto selector init
	Command autonomousCommand = null;
	Command autoCommand[] = new Command[41];
	Command defaultCommand = null;

	//flags
	boolean wasTele = false, readyForMatch = false, wasTest = false, safeToLog = false, lightModeComp = true;

	//LOGGING CONTROL
	boolean logging = false, logToUsb = true;
	String loggingLocation = "Logging/Offseason";

	@Override
	public void robotInit() {
		for (int i = 0; i < 41; i++) {
			autoCommand[i] = null;
		}

		_oi = new OI();
		OI.init();

		//Light mode using auto selectors (Position 10-5)
		if (Robot.autonSelector.uglyAnalog() == 95)
			lightModeComp = false;

		defaultCommand = new DefaultAutonomous();

		//naming for commands 
		Robot.autonSelector.autoCommandName[1] = "Middle - Switch";
		Robot.autonSelector.autoCommandName[2] = "Left - Switch";
		Robot.autonSelector.autoCommandName[3] = "Left - Scale";
		Robot.autonSelector.autoCommandName[4] = "Right - Switch";
		Robot.autonSelector.autoCommandName[5] = "Right - Scale";

		Robot.autonSelector.autoCommandName[11] = "Left Only - Switch Priority";
		Robot.autonSelector.autoCommandName[12] = "Left Only - Scale Priority";
		Robot.autonSelector.autoCommandName[13] = "Left Only - Switch Only";
		Robot.autonSelector.autoCommandName[14] = "Left Only - Scale Only";

		Robot.autonSelector.autoCommandName[15] = "Right Only - Switch Priority";
		Robot.autonSelector.autoCommandName[16] = "Right Only - Scale Priority";
		Robot.autonSelector.autoCommandName[17] = "Right Only - Switch Only";
		Robot.autonSelector.autoCommandName[18] = "Right Only - Scale Only";

		Robot.autonSelector.autoCommandName[19] = "Left - Default";
		Robot.autonSelector.autoCommandName[20] = "Right - Default";

		Robot.autonSelector.autoCommandName[21] = "(MIFOR) Middle - Switch";
		Robot.autonSelector.autoCommandName[22] = "(MIFOR) Left - Switch";
		Robot.autonSelector.autoCommandName[23] = "(MIFOR) Left - Scale";
		Robot.autonSelector.autoCommandName[24] = "(MIFOR) Right - Switch";
		Robot.autonSelector.autoCommandName[25] = "(MIFOR) Right - Scale";

		Robot.autonSelector.autoCommandName[26] = "(MIFOR) Left Only - Switch Priority";
		Robot.autonSelector.autoCommandName[27] = "(MIFOR) Left Only - Scale Priority";
		Robot.autonSelector.autoCommandName[28] = "(MIFOR) Right Only - Switch Priority";
		Robot.autonSelector.autoCommandName[29] = "(MIFOR) Right Only - Scale Priority";
	}

	@Override
	public void robotPeriodic() {
		handleLEDs();
		Robot.drive.loggerUpdate();

		//LOGGING FLAG SET IN AUTOINIT, TELEINIT, TESTINIT
		//LOOPED HERE
		if (safeToLog && logging)
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.RUNTIME);
	}

	@Override
	public void disabledInit() {
		if (safeToLog && logging) {
			safeToLog = false;
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.FINISH);
		}

		Robot.drive.brake((!wasTele) ? NeutralMode.Coast : NeutralMode.Brake);
		//		Robot.drive.brake(NeutralMode.Coast);
	}

	@Override
	public void disabledPeriodic() {
		autonChooser();
		Robot.autonSelector.printSelected();

		if (wasTest)
			OI.rumble(CONTROLLER.BOTH, 1);
		else
			OI.rumble(CONTROLLER.BOTH, 0);

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		System.out.println("Entering auto");

		if (!safeToLog) {
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.STARTUP);
			safeToLog = true;
		}

		//timer start
		Robot.drive.timer.stop();
		Robot.drive.timer.reset();
		Robot.drive.timer.start();

		//keep overriding while game data bad or controller override not set
		do {
			Robot.autonSelector.gameMsg = Robot.lights.gsm();

			autoCommand[1] = (new MiddleAuton(AO.SWITCH, AV.CURRENT));
			autoCommand[2] = (new LeftAuton(AO.SWITCH, AV.CURRENT));
			autoCommand[3] = (new LeftAuton(AO.SCALE, AV.CURRENT));
			autoCommand[4] = (new RightAuton(AO.SWITCH, AV.CURRENT));
			autoCommand[5] = (new RightAuton(AO.SCALE, AV.CURRENT));

			autoCommand[11] = (new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT));
			autoCommand[12] = (new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT));
			autoCommand[13] = (new LeftAuton(AO.SWITCH_ONLY, AV.CURRENT));
			autoCommand[14] = (new LeftAuton(AO.SCALE_ONLY, AV.CURRENT));

			autoCommand[15] = (new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.CURRENT));
			autoCommand[16] = (new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.CURRENT));
			autoCommand[17] = (new RightAuton(AO.SWITCH_ONLY, AV.CURRENT));
			autoCommand[18] = (new RightAuton(AO.SCALE_ONLY, AV.CURRENT));

			autoCommand[19] = (new LeftAuton(AO.DEFAULT, AV.CURRENT));
			autoCommand[20] = (new RightAuton(AO.DEFAULT, AV.CURRENT));

			autoCommand[21] = (new MiddleAuton(AO.SWITCH, AV.FOREST_HILLS));
			autoCommand[22] = (new LeftAuton(AO.SWITCH, AV.FOREST_HILLS));
			autoCommand[23] = (new LeftAuton(AO.SCALE, AV.FOREST_HILLS));
			autoCommand[24] = (new RightAuton(AO.SWITCH, AV.FOREST_HILLS));
			autoCommand[25] = (new RightAuton(AO.SCALE, AV.FOREST_HILLS));

			autoCommand[26] = (new LeftAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.FOREST_HILLS));
			autoCommand[27] = (new LeftAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.FOREST_HILLS));
			autoCommand[28] = (new RightAuton(AO.SWITCH_PRIORITY_NO_CROSS, AV.FOREST_HILLS));
			autoCommand[29] = (new RightAuton(AO.SCALE_PRIORITY_NO_CROSS, AV.FOREST_HILLS));

			if (Robot.autonSelector.controllerOverride) {
				autonomousCommand = autoCommand[Robot.autonSelector.overrideValue];
				Robot.autonSelector.confirmOverride = true;
			}

		} while ((Robot.autonSelector.gameMsg == "NOT" && Robot.drive.timer.get() < 3)
				|| (Robot.autonSelector.controllerOverride && !Robot.autonSelector.confirmOverride));

		//SET COLOR ACCORDING TO ALLIANCE
		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			Robot.lights.hsv(0, 1, .5);
		else
			Robot.lights.hsv(120, 1, .5);

		//BRAKE MODE DURING AUTO
		Robot.drive.brake(NeutralMode.Brake);

		autonChooser();
		Robot.autonSelector.printSelected();

		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		System.out.println("Entering teleop");

		if (!safeToLog && logging) {
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.STARTUP);
			safeToLog = true;
		}

		//GREEN LOW BRIGHTNESS
		Robot.lights.hsv(250, 1, .5);

		Robot.drive.brake(NeutralMode.Coast);

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		wasTele = true;

		//		Command amp = new AmperageTesting(.02, true, false, false, false);
		//		amp.start();
	}

	@Override
	public void teleopPeriodic() {
		if (!wasTest)
			Scheduler.getInstance().run();
	}

	@Override
	public void testInit() {
		System.out.println("Entering test mode");

		if (!safeToLog && logging) {
			Robot.playback.control("Log", loggingLocation, logToUsb, TASK.Log, STATE.STARTUP);
			safeToLog = true;
		}

		wasTest = true;
	}

	@Override
	public void testPeriodic() {
	}

	public void handleLEDs() {
		if (OI.driverJoy.getRawButton(2) && OI.driverJoy.getRawButton(7) && OI.driverJoy.getRawButton(8))
			readyForMatch = true;

		if (!lightModeComp) {
			//Not competition
			if (wasTest)
				Robot.lights.pulse(55, 1, .2, .8, .1);
			else {
				Robot.lights.hsv(Robot.lights.m_hue, 1, .15);
				Robot.lights.m_hue++;
				if (Robot.lights.m_hue > 360)
					Robot.lights.m_hue = 0;
			}

		} else {
			//Comp
			
			if (wasTest) {
				Robot.lights.pulse(55, 1, .2, .8, .1);
			} else {

				if (DriverStation.getInstance().isDisabled()) {
					//IF CONNECTED LOW GREEN
					if (DriverStation.getInstance().isDSAttached()) {

						if (readyForMatch)
							Robot.lights.pulse(258, 1, 0.1, .4, 0.025 / 3.5);
						else
							Robot.lights.pulse(330, 1, .1, .4, 0.025 / 3.5);

					} else {

						//IF FMS PULSE RED, IF NO FMS RGB FADE
						if (DriverStation.getInstance().isFMSAttached()) {
							//			increase pulsing speed while not connected
							Robot.lights.pulse(0, 1, 0.2, .8, 0.15 / 10 * (Robot.drive.timer.get() / 100));

						} else {
							Robot.lights.hsv(Robot.lights.m_hue, 1, .15);
							Robot.lights.m_hue++;
							if (Robot.lights.m_hue > 360)
								Robot.lights.m_hue = 0;
						}
					}
				}
			}
		}

	}

	public void controllerChooser() {
		// if LB & RB
		if (OI.driverJoy.getRawButton(5) && OI.driverJoy.getRawButton(6)) {

			// A BUTTON
			if (OI.driverJoy.getRawButton(1)) {
				Robot.autonSelector.overrideValue = 2;
				Robot.autonSelector.overrideString = "Controller override 1:\t"
						+ Robot.autonSelector.autoCommandName[2];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				Robot.autonSelector.overrideValue = 3;
				Robot.autonSelector.overrideString = "Controller override 2:\t"
						+ Robot.autonSelector.autoCommandName[3];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				Robot.autonSelector.overrideValue = 4;
				Robot.autonSelector.overrideString = "Controller override 3:\t"
						+ Robot.autonSelector.autoCommandName[4];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				Robot.autonSelector.overrideValue = 5;
				Robot.autonSelector.overrideString = "Controller override 4:\t"
						+ Robot.autonSelector.autoCommandName[5];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				Robot.autonSelector.overrideValue = 13;
				Robot.autonSelector.overrideString = "Controller override 5:\t"
						+ Robot.autonSelector.autoCommandName[13];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				Robot.autonSelector.overrideValue = 17;
				Robot.autonSelector.overrideString = "Controller override 6:\t"
						+ Robot.autonSelector.autoCommandName[17];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// LEFT CLICK
			else if (OI.driverJoy.getRawButton(9)) {
				autonomousCommand = defaultCommand;
				Robot.autonSelector.overrideString = "Controller override: DEFAULT AUTO SELECTED";
				Robot.autonSelector.controllerOverride = true;
			}

			// RIGHT CLICK
			else if (OI.driverJoy.getRawButton(10)) {
				Robot.autonSelector.controllerOverride = false;
				System.out.println("Auto override disengaged. " + Robot.autonSelector.autonString);
			}
		}

	}

	public void autonChooser() {
		controllerChooser();

		// if not overriden
		if (Robot.autonSelector.controllerOverride == false) {

			//If selector feedback nominal
			if (Robot.autonSelector.uglyAnalog() <= 40 && Robot.autonSelector.uglyAnalog() >= 1) {
				autonomousCommand = autoCommand[Robot.autonSelector.uglyAnalog()];
			} else {
				autonomousCommand = defaultCommand;
			}
		}
	}
}
