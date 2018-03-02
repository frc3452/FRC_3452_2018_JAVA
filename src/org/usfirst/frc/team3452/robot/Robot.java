package org.usfirst.frc.team3452.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;

import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;
import org.usfirst.frc.team3452.robot.subsystems.Elevator;
import org.usfirst.frc.team3452.robot.subsystems.Intake;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import org.usfirst.frc.team3452.robot.subsystems.Climber;

public class Robot extends TimedRobot {
	public static final Drivetrain drivetrain = new Drivetrain();
	public static final Elevator elevator = new Elevator();
	public static final Intake intake = new Intake();
	public static final Climber climber = new Climber();
	public static final AutonSelector autonSelector = new AutonSelector();

	public static Drivetrain _drivetrain;
	public static Elevator _elevator;
	public static Intake _intake;
	public static Climber _climber;
	public static AutonSelector _autonSelector;
	public static OI _oi;

	Command autonomousCommand = null;
	Command autoCommand[] = { null };
	Command defaultCommand = null;

	@Override
	public void robotInit() {
		_drivetrain = new Drivetrain();
		Drivetrain.getInstance().initHardware();

		_elevator = new Elevator();
		Elevator.getInstance().initHardware();

		_intake = new Intake();
		Intake.getInstance().initHardware();

		_climber = new Climber();
		Climber.getInstance().initHardware();

		_autonSelector = new AutonSelector();

		_oi = new OI();
		OI.init();

		defaultCommand = (new DriveTime(.25, 0, 3));

		autoCommand[1] = (new DriveTime(-.25, 0, 1));
		AutonSelector.getInstance().autoCommandName[1] = "backwards";

		autoCommand[2] = (new DriveTime(.25, 0, 1));
		AutonSelector.getInstance().autoCommandName[2] = "forwards";
	}

	@Override
	public void disabledInit() {
		Drivetrain.getInstance().BrakeCoast(NeutralMode.Coast);
	}

	@Override
	public void disabledPeriodic() {
		autonChooser();

		AutonSelector.getInstance().printSelected();
		Drivetrain.getInstance().LoggerUpdate();

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		Drivetrain.getInstance().BrakeCoast(NeutralMode.Brake);

		autonChooser();
		AutonSelector.getInstance().printSelected();

		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();

		Drivetrain.getInstance().LoggerUpdate();
		Elevator.getInstance().setDriveLimit();
	}

	@Override
	public void teleopInit() {
		Drivetrain.getInstance().BrakeCoast(NeutralMode.Coast);

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();

		Elevator.getInstance().setDriveLimit();
		Drivetrain.getInstance().LoggerUpdate();
	}

	@Override
	public void testPeriodic() {
		Drivetrain.getInstance().LoggerUpdate();
	}

	public void controllerChooser() {
		// if LB & RB
		if (OI.driverJoy.getRawButton(5) && OI.driverJoy.getRawButton(6)) {

			// A BUTTON
			if (OI.driverJoy.getRawButton(1)) {
				autonomousCommand = autoCommand[1];
				AutonSelector.getInstance().overrideString = "Controller override 1:\t"
						+ AutonSelector.getInstance().autoCommandName[1];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				autonomousCommand = autoCommand[2];
				AutonSelector.getInstance().overrideString = "Controller override 2:\t"
						+ AutonSelector.getInstance().autoCommandName[2];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				autonomousCommand = autoCommand[3];
				AutonSelector.getInstance().overrideString = "Controller override 3:\t"
						+ AutonSelector.getInstance().autoCommandName[3];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				autonomousCommand = autoCommand[4];
				AutonSelector.getInstance().overrideString = "Controller override 4:\t"
						+ AutonSelector.getInstance().autoCommandName[4];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				autonomousCommand = autoCommand[5];
				AutonSelector.getInstance().overrideString = "Controller override 5:\t"
						+ AutonSelector.getInstance().autoCommandName[5];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				autonomousCommand = autoCommand[6];
				AutonSelector.getInstance().overrideString = "Controller override 6:\t"
						+ AutonSelector.getInstance().autoCommandName[6];
				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// LEFT CLICK
			else if (OI.driverJoy.getRawButton(9)) {
				autonomousCommand = defaultCommand;
				AutonSelector.getInstance().overrideString = "Controller override: DEFAULT AUTO SELECTED";

				AutonSelector.getInstance().m_ControllerOverride = true;
			}

			// RIGHT CLICK
			else if (OI.driverJoy.getRawButton(10)) {
				AutonSelector.getInstance().m_ControllerOverride = false;
				System.out.println("Auto override disengaged. " + AutonSelector.getInstance().autonString);
			}
		}

	}

	public void autonChooser() {
		controllerChooser();

		// if not overriden
		if (AutonSelector.getInstance().m_ControllerOverride == false) {
			switch (AutonSelector.getInstance().uglyAnalog()) {
			case 1:
				autonomousCommand = autoCommand[1];
				break;
			case 2:
				autonomousCommand = autoCommand[2];
				break;
			case 3:
				autonomousCommand = autoCommand[3];
				break;
			case 4:
				autonomousCommand = autoCommand[4];
				break;
			case 5:
				autonomousCommand = autoCommand[5];
				break;
			case 6:
				autonomousCommand = autoCommand[6];
				break;
			case 7:
				autonomousCommand = autoCommand[7];
				break;
			case 8:
				autonomousCommand = autoCommand[8];
				break;
			case 9:
				autonomousCommand = autoCommand[9];
			case 10:
				autonomousCommand = autoCommand[10];
			case 11:
				autonomousCommand = autoCommand[11];
				break;
			case 12:
				autonomousCommand = autoCommand[12];
				break;
			case 13:
				autonomousCommand = autoCommand[13];
				break;
			case 14:
				autonomousCommand = autoCommand[14];
				break;
			case 15:
				autonomousCommand = autoCommand[15];
				break;
			case 16:
				autonomousCommand = autoCommand[16];
				break;
			case 17:
				autonomousCommand = autoCommand[17];
				break;
			case 18:
				autonomousCommand = autoCommand[18];
				break;
			case 19:
				autonomousCommand = autoCommand[19];
				break;
			case 20:
				autonomousCommand = autoCommand[20];
			case 3452:
				autonomousCommand = defaultCommand;
				break;
			default:
				autonomousCommand = defaultCommand;
				break;
			}
		}
	}
}
