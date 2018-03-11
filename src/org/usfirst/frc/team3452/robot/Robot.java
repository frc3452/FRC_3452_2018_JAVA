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
\\TODO encoder percent done, retune , elevator ramp limit speed, restructure auto selecotr
	Command autonomousCommand = null;
	Command autoCommand[] = new Command[20];
	Command defaultCommand = null;

	boolean wasTele = false;

	Timer timer = new Timer();

	@Override
	public void robotInit() {
		timer.stop();
		timer.reset();

		for (int i = 0; i < 20; i++) {
			autoCommand[i] = null;
		}

		Robot.drive.initHardware();
		Robot.elevator.initHardware();
		Robot.intake.initHardware();
		Robot.climber.initHardware();
		//		Robot.lights.initHardware();
		Robot.camera.initHardware();
		Robot.autonSelector.initHardware();

		_oi = new OI();
		OI.init();

		defaultCommand = (new DefaultAutonomous());

		Robot.autonSelector.autoCommandName[1] = "Middle: Switch";
		Robot.autonSelector.autoCommandName[2] = "Left: Switch";
		Robot.autonSelector.autoCommandName[3] = "Left: Scale";
		Robot.autonSelector.autoCommandName[4] = "Right: Switch";
		Robot.autonSelector.autoCommandName[5] = "Right: Scale";
		Robot.autonSelector.autoCommandName[6] = "Left: Switch Priority";
		Robot.autonSelector.autoCommandName[7] = "Left: Scale Priority";
		Robot.autonSelector.autoCommandName[8] = "Right: Switch Priority";
		Robot.autonSelector.autoCommandName[9] = "Right: Scale Priority";
	}

	@Override
	public void disabledInit() {
		Robot.drive.BrakeCoast((!wasTele) ? NeutralMode.Coast : NeutralMode.Brake);
	}

	@Override
	public void disabledPeriodic() {
		autonChooser();
		Robot.autonSelector.printSelected();

		Robot.drive.LoggerUpdate();

		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		
		
		// Start waiting for timer to run out
		timer.reset();
		timer.start();
		
		/*
		
		// assume we aren't going to find field data 
		int foundFieldData = 0;
		
		// keep looping until we have field data for 5s have gone by being careful to not
		// use too much CPU time (using a delay of 100ms between the loops).
		while(foundFieldData == 0 && timer.get() < 5)
		{
			// Wait for timer
			try 
			{
				// Wait 100ms
				Thread.sleep(100);
			}
			catch (InterruptedException e) 
			{
				// Only get this if someone else poked our thread.  Just ignoring this. (
				// Verified by a 
				
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		
			// We have field data if we read anything except for "NOT"
			if(Robot.autonSelector.gameMsg != "NOT")
			{				
				foundFieldData = 1;
			}
		}
 		
		// Reset our priorities if field data was found successfully
		if(foundFieldData == 1)
		{
			autoCommand[1] = (new MiddleAuton("SWITCH", 1));
			autoCommand[2] = (new LeftAuton("SWITCH", 1));
			autoCommand[3] = (new LeftAuton("SCALE", 1));
			autoCommand[4] = (new RightAuton("SWITCH", 1));
			autoCommand[5] = (new RightAuton("SCALE", 1));
			autoCommand[6] = (new LeftAuton("L_SWITCH_P", 1));
			autoCommand[7] = (new LeftAuton("L_SCALE_P", 1));
			autoCommand[8] = (new RightAuton("R_SWITCH_P", 1));
			autoCommand[9] = (new RightAuton("R_SCALE_P", 1));	
		}
		else
		{			
			// Didn't get field data so just do default
			autonomousCommand = (new DefaultAutonomous());			
		}

		*/
		
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

		} while (Robot.autonSelector.gameMsg == "NOT" && timer.get() < 5);


		if (timer.get() > 5) {
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

		Robot.drive.LoggerUpdate();
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		Robot.drive.BrakeCoast(NeutralMode.Coast);

		if (autonomousCommand != null) {
			autonomousCommand.cancel();
		}

		wasTele = true;
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		Robot.drive.LoggerUpdate();
	}

	@Override
	public void testPeriodic() {
		Robot.drive.LoggerUpdate();
	}

	public void controllerChooser() {
		// if LB & RB
		if (OI.driverJoy.getRawButton(5) && OI.driverJoy.getRawButton(6)) {

			// A BUTTON
			if (OI.driverJoy.getRawButton(1)) {
				autonomousCommand = autoCommand[6];
				Robot.autonSelector.overrideString = "Controller override 1:\t"
						+ Robot.autonSelector.autoCommandName[6];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// B BUTTON
			else if (OI.driverJoy.getRawButton(2)) {
				autonomousCommand = autoCommand[7];
				Robot.autonSelector.overrideString = "Controller override 2:\t"
						+ Robot.autonSelector.autoCommandName[7];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// X BUTTON
			else if (OI.driverJoy.getRawButton(3)) {
				autonomousCommand = autoCommand[8];
				Robot.autonSelector.overrideString = "Controller override 3:\t"
						+ Robot.autonSelector.autoCommandName[8];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// Y BUTTON
			else if (OI.driverJoy.getRawButton(4)) {
				autonomousCommand = autoCommand[9];
				Robot.autonSelector.overrideString = "Controller override 4:\t"
						+ Robot.autonSelector.autoCommandName[9];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// BACK BUTTON
			else if (OI.driverJoy.getRawButton(7)) {
				autonomousCommand = autoCommand[1];
				Robot.autonSelector.overrideString = "Controller override 5:\t"
						+ Robot.autonSelector.autoCommandName[1];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// START BUTTON
			else if (OI.driverJoy.getRawButton(8)) {
				autonomousCommand = autoCommand[2];
				Robot.autonSelector.overrideString = "Controller override 6:\t"
						+ Robot.autonSelector.autoCommandName[2];
				Robot.autonSelector.m_ControllerOverride = true;
			}

			// LEFT CLICK
			else if (OI.driverJoy.getRawButton(9)) {
				autonomousCommand = defaultCommand;
				Robot.autonSelector.overrideString = "Controller override: DEFAULT AUTO SELECTED";

				Robot.autonSelector.m_ControllerOverride = true;
			}

			// RIGHT CLICK
			else if (OI.driverJoy.getRawButton(10)) {
				Robot.autonSelector.m_ControllerOverride = false;
				System.out.println("Auto override disengaged. " + Robot.autonSelector.autonString);
			}
		}

	}

	public void autonChooser() {
		controllerChooser();

		// if not overriden
		if (Robot.autonSelector.m_ControllerOverride == false) {

			switch (Robot.autonSelector.uglyAnalog()) {
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
				break;
			case 10:
				autonomousCommand = autoCommand[10];
				break;
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
				break;
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
