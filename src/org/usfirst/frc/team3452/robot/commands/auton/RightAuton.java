package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToStop;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderGyro;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroPos;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AO;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAuton extends CommandGroup {

	public RightAuton(AO option, int selector) {
		addSequential(new GyroReset());
		addSequential(new EncoderReset());

		//IF DATA FOUND
		if (Robot.autonSelector.gameMsg != "NOT") {

			switch (option) {
			case SWITCH:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);

				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
				}

				break;
			case SCALE:

				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(3620);

				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
					scaleR(3620);
				}

				break;
			case SWITCH_PRIORITY_NO_CROSS:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
					scaleR(3620);
				} else {
					defaultAuton();
				}

				break;
			case SCALE_PRIORITY_NO_CROSS:

				if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
					scaleR(3620);
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
				} else {
					defaultAuton();
				}

				break;
			case SWITCH_ONLY:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'R')
					switchR(1);
				else
					defaultAuton();

				break;
			case SCALE_ONLY:

				if (Robot.autonSelector.gameMsg.charAt(1) == 'R')
					switchR(1);
				else
					defaultAuton();

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
			//game data not found
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

			//add .3?
			addSequential(new EncoderGyro(11, 11, .4, .4, .5, 0, .017)); // drive to side of switch

			addSequential(new EncoderFrom(-1.5, 0.75, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderGyro(10.5, 10.5, .5, .5, .6, 180, 0.021)); //drive back of switch
				}
			});

			addSequential(new GyroPos(172, .35, 1));

			addSequential(new DriveTime(.5, 0, .75)); //hit switch
			addSequential(new DriveToStop(.4));

			addSequential(new IntakeTime(.5, 1));

			addParallel(new DriveTime(-.5, 0, .8));
			addSequential(new ElevatorTime(-.15, 10));

			break;
		case 3620:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.2, 11.2, .4, .4, .4, 0, .017)); // drive to side of switch

			addSequential(new EncoderFrom(-1.5, .75, .5, .5, .5)); // turn to switch 
			addParallel(new ElevatorPosition(2)); //raise

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(6.95, 6.95, .4, .4, .4, -90, 0.021)); //drive back of switch

			addSequential(new ElevatorPosition(4.5)); //raise and turn to switch
			addSequential(new EncoderFrom(-.42, .55, .6, .6, .65));
			addSequential(new DriveTime(.65, 0, .5));

			addSequential(new IntakeTime(.3, 2));
			addSequential(new EncoderFrom(-.5, -.5, .5, .5, .5));

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

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .7));
					addSequential(new EncoderGyro(7.91, 7.91, .6, .6, .7, 0, .017)); // drive to side of switch
				}
			});
			addSequential(new EncoderFrom(-1.5, 0.75, .5, .5, .5)); // turn to switch

			addSequential(new DriveTime(.5, 0, .75)); //hit switch
			addSequential(new DriveToStop(.4));

			addSequential(new IntakeTime(1, .5)); //drop and backup
			addParallel(new DriveTime(-.5, 0, .8));
			addSequential(new ElevatorTime(-.1, 10));
			break;
		case 3620:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(5)); // raise arm
			addSequential(new EncoderFrom(-1.5, .75, .5, .5, .5)); // turn to switch
			addSequential(new EncoderFrom(.8, .8, .5, .5, .5)); // drive and drop
			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-.5, -.5, .5, .5, .5));
			break;
		default:
			defaultAuton();
			break;

		}
	}

	private void scaleL(int mode) {
		switch (mode) {
		case 1:
			defaultAuton();
			break;
		case 3620:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.5, 11.5, .6, .6, .65, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(2)); //raise
			addSequential(new EncoderFrom(-1.5, 0.75, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(10.6, 10.6, .5, .5, .5, -90, 0.011)); //drive front of scale

			addSequential(new EncoderFrom(.75, -1.5, .5, .5, .5));

			addSequential(new ElevatorPosition(15)); //raise and turn to switch

//			addSequential(new CommandGroup() {
//				{
//					addParallel(new IntakeWhileDrive(.5, .9, 1));
//					addSequential(new EncoderFrom(2.61, 2.41, .1, .1, .15));
//				}
//			});
			
			addSequential(new EncoderFrom(2.61, 2.41, .1, .1, .15));
			addSequential(new IntakeTime(.5, 1));
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void scaleR(int mode) {
		switch (mode) {
		case 1:
			defaultAuton();
			break;
		case 3620:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addParallel(new ElevatorPosition(1)); //lift for drive
			addSequential(new EncoderGyro(12.3, 12.3, .5, .5, .6, 0, .016)); // drive to far side of switch
			addSequential(new EncoderFrom(-.4, .2, .4, .4, .5)); //turn to switch

			addSequential(new ElevatorPosition(15)); //raise and forward
			addSequential(new DriveTime(.4, 0, 1.9));

			addSequential(new IntakeTime(.4, 1)); //shoot, back up down and spin
			addSequential(new DriveTime(-.4, 0, 1.6));
			addSequential(new ElevatorTime(-.4, 10));
			addSequential(new GyroPos(225, .4, 1));
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

		addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
		addSequential(new ElevatorPosition(3.5)); // raise arm
	}

}
