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
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AO;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MiddleAuton extends CommandGroup {

	public MiddleAuton(AO option, int selector) {
		addSequential(new EncoderReset());
		addSequential(new GyroReset());

		//IF DATA FOUND
		if (Robot.autonSelector.gameMsg != "NOT") {

			switch (option) {
			case SWITCH:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
				} else {
					defaultAuton();
				}

				break;
			case DEFAULT:
				defaultAuton();
				break;
			default:
				System.out.println("ERROR Auto priority " + option + " not accepted; running default");
				defaultAuton();
				break;
			}
		} else {
			//if game data bad
			defaultAuton();
		}

		addSequential(new DriveTime(0, 0, 16));
	}

	private void switchL(int mode) {
		switch (mode) {
		case 1:

			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderDrive(1, 2.2, .8, .8, .6)); //init curve

			//drive to switch
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .2));
					addSequential(new EncoderFrom(4.2, 3.6, .6, .6, .7));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, .5)); //first place

			//backup
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(-15, .75));
					addSequential(new EncoderFrom(-3.8, -3.8, .45, .45, .6));
				}
			});

			//lower and reset encoders
			addSequential(new EncoderReset());

			//TURN AND GRAB CUBE
			addSequential(new CommandGroup() {
				{
					addSequential(new GyroPos(35, .4, 1));
					addSequential(new DriveToCube(.55));
					addParallel(new IntakeTime(-.5, 1.5));
					addSequential(new EncoderDrive(0, 0, .5, .5, .6));
				}
			});

			//DRIVE TO SWITCH
			addSequential(new CommandGroup() {
				{
					addSequential(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderFrom(3, 3, .45, .45, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, 1.5)); //second place

			//back up
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(-.1, 10));

			break;
		default:
			defaultAuton();
			break;
		}

	}

	private void switchR(int mode) {
		switch (mode) {
		case 1:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm
			
			addSequential(new DriveTime(0,0,10));
			
			addSequential(new EncoderDrive(2.5, 1, .8, .8, .6));

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .3));
					addSequential(new EncoderFrom(3.3 + .15, 4.2 + .38, .6, .6, .7));
				}
			});

			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, .5)); //PLACE 1

			//backup
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(-15, .5));
					addSequential(new EncoderFrom(-3.8, -3.8, .45, .45, .6));
				}
			});

			//reset encoders
			addSequential(new EncoderReset());

			//TURN AND GRAB CUBE
			addSequential(new CommandGroup() {
				{
					addSequential(new GyroPos(330, .35, 1));
					addSequential(new DriveToCube(.55));
					addParallel(new IntakeTime(-.5, 1.5));
					addSequential(new EncoderDrive(0, 0, .6, .6, .6));
				}
			});

			//DRIVE TO SWITCH
			addSequential(new CommandGroup() {
				{
					addSequential(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderFrom(3, 3, .45, .45, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.3));

			addSequential(new IntakeTime(.5, 1.5)); //second place

			//back up
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(-.1, 10));

			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void defaultAuton() {
		addParallel(new DriveTime(.55, 0, .5));
		addSequential(new ElevatorTime(.5, .1725));
		addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

		addSequential(new EncoderFrom(7.2, 2, .35, .35, .5));
		addSequential(new DriveTime(-.4, 0, 7));
	}
}
