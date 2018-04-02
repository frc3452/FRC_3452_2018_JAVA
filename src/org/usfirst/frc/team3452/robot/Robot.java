package org.usfirst.frc.team3452.robot;

import org.usfirst.frc.team3452.robot.commands.auton.DefaultAutonomous;
import org.usfirst.frc.team3452.robot.commands.auton.LeftAuton;
import org.usfirst.frc.team3452.robot.commands.auton.MiddleAuton;
import org.usfirst.frc.team3452.robot.commands.auton.RightAuton;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.Camera;
import org.usfirst.frc.team3452.robot.subsystems.Climber;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Intake;
import org.usfirst.frc.team3452.robot.subsystems.Lights;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
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

	public static OI _oi;

	//auto selector init
	Command autonomousCommand = null;
	Command autoCommand[] = new Command[31];
	Command defaultCommand = null;

	//flag for teleop 
	boolean wasTele = false;

	//init timer
	private static Timer autoTimer = new Timer();

	@Override
	public void robotInit() {
		//init timer
		autoTimer.stop();
		autoTimer.reset();
		autoTimer.start();

		for (int i = 0; i < 31; i++) {
			autoCommand[i] = null;
		}

		Robot.drive.initHardware();
		Robot.elevator.initHardware();
		Robot.intake.initHardware();
		Robot.climber.initHardware();
		Robot.lights.initHardware();
		Robot.camera.initHardware();
		Robot.autonSelector.initHardware();

		_oi = new OI();
		OI.init();

		defaultCommand = (new DefaultAutonomous());

		//naming for commands 
		Robot.autonSelector.autoCommandName[1] = "Middle: Switch";
		Robot.autonSelector.autoCommandName[2] = "Left: Switch";
		Robot.autonSelector.autoCommandName[3] = "Left: Scale";
		Robot.autonSelector.autoCommandName[4] = "Right: Switch";
		Robot.autonSelector.autoCommandName[5] = "Right: Scale";
		Robot.autonSelector.autoCommandName[6] = "Left: Switch Priority";
		Robot.autonSelector.autoCommandName[7] = "Left: Scale Priority";
		Robot.autonSelector.autoCommandName[8] = "Right: Switch Priority";
		Robot.autonSelector.autoCommandName[9] = "Right: Scale Priority";

		Robot.autonSelector.autoCommandName[12] = "(MISJO) Left: Switch";
		Robot.autonSelector.autoCommandName[13] = "(MISJO) Left: Scale";
		Robot.autonSelector.autoCommandName[14] = "(MISJO) Right: Switch";
		Robot.autonSelector.autoCommandName[15] = "(MISJO) Right: Scale";
		Robot.autonSelector.autoCommandName[16] = "(MISJO) Left: Switch Priority";
		Robot.autonSelector.autoCommandName[17] = "(MISJO) Left: Scale Priority";
		Robot.autonSelector.autoCommandName[18] = "(MISJO) Right: Switch Priority";
		Robot.autonSelector.autoCommandName[19] = "(MISJO) Right: Scale Priority";
	}

	public void robotPeriodic() {
		handleLEDs();
		Robot.drive.LoggerUpdate();
	}

	@Override
	public void disabledInit() {
		//first time enabled set to coast, after tele brake
		//				Robot.drive.BrakeCoast((!wasTele) ? NeutralMode.Coast : NeutralMode.Brake);
		Robot.drive.BrakeCoast(NeutralMode.Coast);
	}

	@Override
	public void disabledPeriodic() {
		autonChooser();
		Robot.autonSelector.printSelected();

		Scheduler.getInstance().run();

	}

	@Override
	public void autonomousInit() {
		autoTimer.reset();
		autoTimer.start();
		//timer start

		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			Robot.lights.pulse(0, 1, .01, .1, 0.004);
		else
			Robot.lights.pulse(120, 1, .01, .1, 0.004);

		//keep overriding while game data bad or controller override not set
		do {
			Robot.autonSelector.gameMsg = Robot.lights.gsm();

			autoCommand[1] = (new MiddleAuton("SWITCH", 1));
			autoCommand[2] = (new LeftAuton("SWITCH", 1));
			autoCommand[3] = (new LeftAuton("SCALE", 1));
			autoCommand[4] = (new RightAuton("SWITCH", 1));
			autoCommand[5] = (new RightAuton("SCALE", 1));
			autoCommand[6] = (new LeftAuton("L_SWITCH_P", 1));
			autoCommand[7] = (new LeftAuton("L_SCALE_P", 1));
			autoCommand[8] = (new RightAuton("R_SWITCH_P", 1));
			autoCommand[9] = (new RightAuton("R_SCALE_P", 1));

			autoCommand[12] = (new LeftAuton("SWITCH", 3620));
			autoCommand[13] = (new LeftAuton("SCALE", 3620));
			autoCommand[14] = (new RightAuton("SWITCH", 3620));
			autoCommand[15] = (new RightAuton("SCALE", 3620));
			autoCommand[16] = (new LeftAuton("L_SWITCH_P", 3620));
			autoCommand[17] = (new LeftAuton("L_SCALE_P", 3620));
			autoCommand[18] = (new RightAuton("R_SWITCH_P", 3620));
			autoCommand[19] = (new RightAuton("R_SCALE_P", 3620));

			if (Robot.autonSelector.controllerOverride) {
				autonomousCommand = autoCommand[Robot.autonSelector.overrideValue];
				Robot.autonSelector.confirmOverride = true;
			}

		} while ((Robot.autonSelector.gameMsg == "NOT" && autoTimer.get() < 3)
				|| (Robot.autonSelector.controllerOverride && !Robot.autonSelector.confirmOverride));

		//by the time it gets to this, the time shouldn't be over 5 seconds
		//if so, error occured and we need to set the default
		if (autoTimer.get() > 3) {
			autonomousCommand = (new DefaultAutonomous());
		}

		Robot.drive.BrakeCoast(NeutralMode.Brake);

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
		//GREEN LOW BRIGHTNESS
		Robot.lights.hsv(250, 1, .2);

		Robot.drive.BrakeCoast(NeutralMode.Coast);

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		wasTele = true;
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}

	public void handleLEDs() {

		if (DriverStation.getInstance().isDisabled()) {
			//IF CONNECTED LOW GREEN
			if (DriverStation.getInstance().isDSAttached()) {
				Robot.lights.pulse(258, 1, 0.01, .12, 0.0015);
			} else {

				//IF AT EVENT PULSE RED, FADE HUE IF NOT
				if (DriverStation.getInstance().isFMSAttached()) {
					//			increase pulsing speed while not connected
					Robot.lights.pulse(0, 1, 0.01, .15, 0.15 / 10 * (autoTimer.get() / 110));
				} else {
					Robot.lights.hsv(Robot.lights.m_hue, 1, .18);
					Robot.lights.m_hue++;
					if (Robot.lights.m_hue > 360)
						Robot.lights.m_hue = 0;
				}
			}

			//TEST
		} else if (DriverStation.getInstance().isTest()) {
			Robot.lights.pulse(55, 1, .01, .2, .08);
		}
	}

	public void controllerChooser() {
		// if LB & RB
		if (OI.driverJoy.getRawButton(5) && OI.driverJoy.getRawButton(6)) {

			// A BUTTON
			if (OI.driverJoy.getRawButton(1)) {
				Robot.autonSelector.overrideValue = 6;
				Robot.autonSelector.overrideString = "Controller override 1:\t"
						+ Robot.autonSelector.autoCommandName[6];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				Robot.autonSelector.overrideValue = 7;
				Robot.autonSelector.overrideString = "Controller override 2:\t"
						+ Robot.autonSelector.autoCommandName[7];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				Robot.autonSelector.overrideValue = 8;
				Robot.autonSelector.overrideString = "Controller override 3:\t"
						+ Robot.autonSelector.autoCommandName[8];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				Robot.autonSelector.overrideValue = 9;
				Robot.autonSelector.overrideString = "Controller override 4:\t"
						+ Robot.autonSelector.autoCommandName[9];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				Robot.autonSelector.overrideValue = 1;
				Robot.autonSelector.overrideString = "Controller override 5:\t"
						+ Robot.autonSelector.autoCommandName[1];
				Robot.autonSelector.controllerOverride = true;
				Robot.autonSelector.confirmOverride = false;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				Robot.autonSelector.overrideValue = 2;
				Robot.autonSelector.overrideString = "Controller override 6:\t"
						+ Robot.autonSelector.autoCommandName[2];
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
			if (Robot.autonSelector.uglyAnalog() <= 20 && Robot.autonSelector.uglyAnalog() >= 1) {
				autonomousCommand = autoCommand[Robot.autonSelector.uglyAnalog()];
			} else {
				autonomousCommand = defaultCommand;
			}
		}
	}
}
