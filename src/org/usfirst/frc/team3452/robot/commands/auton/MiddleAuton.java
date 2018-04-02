package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToCube;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToStop;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderDrive;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroPos;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeManual;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MiddleAuton extends CommandGroup {

	public MiddleAuton(String priority, int selector) {
		addSequential(new EncoderReset());
		addSequential(new GyroReset());

		//IF DATA FOUND
		if (Robot.autonSelector.gameMsg != "NOT") {

			if (priority == "SWITCH") {

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else {
					defaultAuton();
					addSequential(new DriveTime(0, 0, 16));
				}
			} else {
				System.out.println("ERROR Auto priority " + priority + " not accepted; running default");
				defaultAuton();
			}
		} else {
			//			System.out.println("ERROR Game data not found; running default");
			defaultAuton();
		}
	}

	private void switchL(int mode) {
		switch (mode) {
		case 1:
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderDrive(1, 2.2, .8, .8, .6)); //init curve

			//drive to switch
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .15, true));
					addSequential(new EncoderFrom(4.2, 3.6, .7, .7, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, .5)); //first place

			//backup
			addSequential(new EncoderFrom(-3.8, -3.8, .45, .45, .6));

			//lower and reset encoders
			addParallel(new ElevatorPosition(-15));
			addSequential(new EncoderReset());

			//TODO Swap gyro for encoder movement
			//TURN AND GRAB CUBE
			addSequential(new CommandGroup() {
				{
					addSequential(new GyroPos(40, .4, 1));
					addSequential(new DriveToCube(.43));
					addParallel(new IntakeTime(.5, 2.5));
					addSequential(new EncoderDrive(0, 0, .5, .5, .6));
				}
			});

			//DRIVE TO SWITCH
			addSequential(new CommandGroup() {
				{
					addSequential(new ElevatorWhileDrive(3.5, .5, true));
					addSequential(new EncoderFrom(3, 3, .45, .45, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, 1.5)); //second place

			//back up
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(.1, 10));

			break;
		default:
			defaultAuton();
			break;
		}

	}

	private void switchR(int mode) {
		switch (mode) {
		case 1:
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderDrive(2.2, 1, .8, .8, .6));

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .2, false));
					addSequential(new EncoderFrom(3.5, 4.2, .7, .7, .6));
				}
			});

			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(-.5, 1.5));
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(.1, 10));

			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void defaultAuton() {
		addSequential(new EncoderFrom(7.87, 3.1, .35, .35, .5));
		addSequential(new DriveTime(-.4, 0, 5));

		addSequential(new DriveTime(.25, 0, .5));
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(.125, 0, 1));
	}
}
