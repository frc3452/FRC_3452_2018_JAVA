package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderGyro;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroPos;
import org.usfirst.frc.team3452.robot.commands.drive.ResetGyro;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.commands.signal.WaitForGameData;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class RightAuton extends CommandGroup {

	public RightAuton(String priority, int selector) {
		addSequential(new WaitForGameData());

		//IF DATA FOUND
		if (Robot.lights.gsm() != "NOT") {

			switch (priority) {

			case "SWITCH":
				switch (Robot.lights.gsm().charAt(0)) {
				case 'L':
					switchL(selector);
					break;
				case 'R':
					switchR(selector);
					break;
				}
				break;
			case "SCALE":
				switch (Robot.lights.gsm().charAt(1)) {
				case 'L':
					scaleL(selector);
					break;
				case 'R':
					scaleR(selector);
					break;
				}
				break;
			case "SIDE":
				//FIXME Investigate if R R L starts switch and then scale auto
				//1. switch R, 2. scale R, 3. switch L
				if (Robot.lights.gsm().charAt(0) == 'R') {
					switchR(selector);
					break;
				} else if (Robot.lights.gsm().charAt(1) == 'R') {
					scaleR(selector);
					break;
				} else if (Robot.lights.gsm().charAt(0) == 'L') {
					switchL(selector);
					break;
				}
				break;
			default:
				System.out.println("ERROR Auto priority " + priority + " not accepted; running default");
				defaultAuton();
				break;
			}

		} else {
			System.out.println("ERROR Game data not found; running default");
			defaultAuton();
		}
	}

	private void switchL(int mode) {
		//FIXME RIGHT POS LEFT SWITCH
		switch (mode) {
		case 1:
			defaultAuton();
			break;
		case 2:
			defaultAuton();
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void switchR(int mode) {
		switch (mode) {
		case 1:
			addSequential(new ResetGyro());
			addSequential(new EncoderReset());

			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
			addSequential(new ElevatorPosition(5)); // raise arm

			addSequential(new EncoderFrom(-1.5, .75, .5, .5, .5)); // turn to switch
			addSequential(new EncoderFrom(.8, .8, .5, .5, .5)); // drive and drop
			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-.5, -.5, .5, .5, .5));
			break;
		case 2:
			addSequential(new EncoderReset());
			break;
		default:
			defaultAuton();
			break;

		}
	}

	private void scaleL(int mode) {
		//FIXME RIGHT POS LEFT SCALE
		switch (mode) {
		case 1:
			defaultAuton();
			break;
		case 2:
			defaultAuton();
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void scaleR(int mode) {
		switch (mode) {
		case 1:
			addSequential(new ResetGyro());
			addSequential(new EncoderReset()); //reset

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
		case 2:
			defaultAuton();
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void defaultAuton() {
		addSequential(new ResetGyro());
		addSequential(new EncoderReset());

		addParallel(new DriveTime(.25, 0, .5));
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

		addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
		addSequential(new ElevatorPosition(5)); // raise arm
	}

}
