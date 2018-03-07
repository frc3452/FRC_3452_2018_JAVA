package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.commands.signal.WaitForGameData;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MiddleAuton extends CommandGroup {

	public MiddleAuton(String priority, int selector) {
		addSequential(new WaitForGameData());

		if (Robot.lights.gsm() != "NOT") {

			if (priority == "SWITCH") {
				if (Robot.lights.gsm().charAt(0) == 'L') {
					switchL(selector);
				} else if (Robot.lights.gsm().charAt(0) == 'R') {
					switchR(selector);
				}
			} else if (priority == "SCALE") {
				if (Robot.lights.gsm().charAt(1) == 'L') {
					scaleL(selector);
				} else if (Robot.lights.gsm().charAt(1) == 'R') {
					scaleR(selector);
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
		switch (mode) {
		case 1:
			addSequential(new EncoderReset());
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderFrom(1.45, 1.45, .3, .3, .4)); // INIT DRIVE

			addParallel(new ElevatorPosition(4.2));
			addSequential(new EncoderFrom(-0.38, .75, .5, .5, .5)); // turn left

			addSequential(new EncoderFrom(4.9, 3.2, .4, .4, .5)); // drive to scale
			addSequential(new EncoderFrom(.4, .4, .4, .4, .4)); // .5 .5 .4 .4 .5

			addSequential(new IntakeTime(1, 1));
			addSequential(new EncoderFrom(-1.4, -1.4, .3, .3, .2)); // back up  after placing
			break;
		case 2:
			addSequential(new EncoderReset());
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderFrom(1.45, 1.45, .3, .3, .4)); // INIT DRIVE

			addParallel(new ElevatorPosition(4.2));
			addSequential(new EncoderFrom(-0.38, .75, .5, .5, .5)); // turn left

			addSequential(new EncoderFrom(4.9, 3.2, .4, .4, .5)); // drive to scale
			addSequential(new EncoderFrom(.4, .4, .4, .4, .4)); // .5 .5 .4 .4 .5

			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-1.4, -1.4, .3, .3, .2)); // back up after placing

			addSequential(new EncoderFrom(-.43 - .1, .75 + .1, .6, .6, .6)); // spin, drive, around switch and go forward
			addSequential(new EncoderFrom(4.85 - 1.1, 3.35 - .5, .3, .3, .5));
			addSequential(new EncoderFrom(1.5, 1.5, .3, .3, .4));
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void switchR(int mode) {
		switch (mode) {
		case 1:
			addSequential(new EncoderReset());
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .25)); // jog forward backwards to drop arm

			addSequential(new EncoderFrom(1.45, 1.45, .3, .3, .4)); // INIT DRIVE

			addParallel(new ElevatorPosition(4.2));
			addSequential(new EncoderFrom(.75, -0.38, .4, .4, .5)); // turn right

			addSequential(new EncoderFrom(3.05, 5.05, .4, .4, .5)); // drive to scale
			addSequential(new EncoderFrom(.6, .6, .4, .4, .5));

			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-1.4, -1.4, .3, .3, .2)); // back up after placing

			break;
		case 2:
			addSequential(new EncoderReset());
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .25)); // jog forward backwards to drop arm

			addSequential(new EncoderFrom(1.45, 1.45, .3, .3, .4)); // INIT DRIVE

			addParallel(new ElevatorPosition(4.2));
			addSequential(new EncoderFrom(.75, -0.38, .4, .4, .5)); // turn right

			addSequential(new EncoderFrom(3.05, 5.05, .4, .4, .5)); // drive to switch
			addSequential(new EncoderFrom(.6, .6, .4, .4, .5));

			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-1.4, -1.4, .3, .3, .2)); // back up after placing

			addSequential(new EncoderFrom(.75, -.43, .6, .6, .6)); // spin, drive around switch and goforward
			addSequential(new EncoderFrom(3.35, 4.75, .3, .3, .5));
			addSequential(new EncoderFrom(1.5, 1.5, .3, .3, .4));
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void scaleL(int mode) {
		switch (mode) {
		case 1:
			addSequential(new EncoderReset());
			break;
		case 2:
			addSequential(new EncoderReset());
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void scaleR(int mode) {
		switch (mode) {
		case 1:
			addSequential(new EncoderReset());
			break;
		case 2:
			addSequential(new EncoderReset());
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void defaultAuton() {
		//TODO add default
		addSequential(new EncoderReset());

		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(0, 0, 1));
		addSequential(new ElevatorPosition(2));
		addSequential(new DriveTime(.2, 0, 3));

	}
}
