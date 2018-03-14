package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderGyro;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroPos;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.commands.signal.WaitForGameData;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAuton extends CommandGroup {

	public LeftAuton(String priority, int selector) {
		addSequential(new EncoderReset());
		addSequential(new GyroReset());

		addSequential(new WaitForGameData());

		//IF DATA FOUND
		if (Robot.autonSelector.gameMsg != "NOT") {

			if (priority == "SWITCH") {

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);
					addSequential(new DriveTime(0, 0, 16));

				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(selector);
					addSequential(new DriveTime(0, 0, 16));
				}

			} else if (priority == "SCALE") {
				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {

					scaleL(selector);
					addSequential(new DriveTime(0, 0, 16));

				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
					scaleR(selector);
					addSequential(new DriveTime(0, 0, 16));
				}

			} else if (priority == "L_SWITCH_P") {
				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else {
					defaultAuton();
				}
			} else if (priority == "L_SCALE_P") {
				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(selector);
					addSequential(new DriveTime(0, 0, 16));
				} else {
					defaultAuton();
				}
			} else {
				System.out.println("ERROR Auto priority " + priority + " not accepted; running default");
				defaultAuton();
			}

		} else {
			System.out.println("ERROR Game data not found; running default");
			defaultAuton();
		}
	}

	private void switchL(int mode) {
		//COMPLETE
		switch (mode) {
		case 1:
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(3.5)); // raise arm
			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch
			addSequential(new EncoderFrom(1.1, 1.1, .5, .5, .5)); // drive and drop
			addSequential(new IntakeTime(1, .5));
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

			addSequential(new EncoderGyro(11.2, 11.2, .4, .4, .4, 0, .017)); // drive to side of switch

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch 
			addParallel(new ElevatorPosition(2)); //raise

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(6.95, 6.95, .4, .4, .4, 90, 0.021)); //drive back of switch

			addSequential(new ElevatorPosition(4.5)); //raise and turn to switch
			addSequential(new EncoderFrom(.55, -.42, .6, .6, .65));
			addSequential(new DriveTime(.65, 0, .5));

			addSequential(new IntakeTime(.3, 2));
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
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new ElevatorPosition(1.5)); //lift for drive
			//			addSequential(new ElevatorTime(.75, .75));
			addSequential(new EncoderGyro(12.3, 12.3, .5, .5, .6, 0, .016)); // drive to far side of switch
			addSequential(new EncoderFrom(.2, -.4, .4, .4, .5)); //turn to switch

			addSequential(new ElevatorPosition(15)); //raise and forward
			addSequential(new DriveTime(.4, 0, 1.7));

			addSequential(new IntakeTime(.4, 1)); //shoot, back up down and spin
			addSequential(new DriveTime(-.4, 0, 1.6));
			addSequential(new ElevatorTime(-.4, 10));
			addSequential(new GyroPos(135, .4, 1));

			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void scaleR(int mode) {
		switch (mode) {
		case 1:
			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.7, 11.7, .6, .6, .65, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(2)); //raise
			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(10.1, 10.1, .5, .5, .5, 90, 0.011)); //drive front of scale

			addSequential(new EncoderFrom(-1.5, .75, .5, .5, .5));

			addSequential(new ElevatorPosition(15)); //raise and turn to switch

			addSequential(new EncoderFrom(2.41, 2.61, .1, .1, .15));

			addSequential(new IntakeTime(.5, 1));

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
		addSequential(new ElevatorPosition(3.5)); // raise arm
	}

}
