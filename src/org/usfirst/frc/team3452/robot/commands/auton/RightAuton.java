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

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAuton extends CommandGroup {

	public RightAuton(String priority, int selector) {
		addSequential(new GyroReset());
		addSequential(new EncoderReset());

		if (priority == "D") {
			defaultAuton();
		} else {

			//IF DATA FOUND
			if (Robot.autonSelector.gameMsg != "NOT") {

				if (priority == "SWITCH") {

					if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
						switchL(selector);

					} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
						switchR(selector);
					}

				} else if (priority == "SCALE") {

					if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
						scaleL(selector);

					} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
						scaleR(selector);
					}

				} else if (priority == "R_SWITCH_P") {
					if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
						switchR(selector);
					} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
						scaleR(selector);
					} else {
						defaultAuton();
					}
				} else if (priority == "R_SCALE_P") {
					if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
						scaleR(selector);
					} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
						switchR(selector);
					} else {
						defaultAuton();
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
		addSequential(new DriveTime(0, 0, 16));
	}

	private void switchL(int mode) {
		switch (mode) {
		case 1:
			defaultAuton();
			break;
		case 3620:
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

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
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .7, true));
					addSequential(new EncoderGyro(7.91, 7.91, .6, .6, .7, 0, .017)); // drive to side of switch
				}
			});
			addSequential(new EncoderFrom(-1.5, 0.75, .5, .5, .5)); // turn to switch

			addSequential(new DriveTime(.5, 0, .75)); //hit switch
			addSequential(new DriveToStop(.4));

			addSequential(new IntakeTime(1, .5)); //drop and backup
			addParallel(new DriveTime(-.5, 0, .8));
			addSequential(new ElevatorTime(.1, 10));
			break;
		case 3620:
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

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
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.5, 11.5, .6, .6, .65, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(2)); //raise
			addSequential(new EncoderFrom(-1.5, 0.75, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(10.6, 10.6, .5, .5, .5, -90, 0.011)); //drive front of scale

			addSequential(new EncoderFrom(.75, -1.5, .5, .5, .5));

			addSequential(new ElevatorPosition(15)); //raise and turn to switch

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
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

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
		addParallel(new DriveTime(.45, 0, .5));
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

		addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
		addSequential(new ElevatorPosition(5)); // raise arm
	}

}
