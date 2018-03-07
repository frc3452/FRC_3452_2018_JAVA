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

public class LeftAuton extends CommandGroup {

	public LeftAuton(String priority, int selector) {
		addSequential(new WaitForGameData());

		if (priority == "SWITCH") {
			if (Robot.lights.gsm().charAt(0) == 'L') {
				switchL(selector);
			} else if (Robot.lights.gsm().charAt(0) == 'R') {
				switchR(selector);
			}
		} else if (priority == "SCALE") {
			//TODO Left Scale
			if (Robot.lights.gsm().charAt(1) == 'L') {
				scaleL(selector);
			} else if (Robot.lights.gsm().charAt(1) == 'R') {
				scaleR(selector);
			}
		} else {
			defaultAuton();
		}
	}

	private void switchL(int mode) {
		if (mode == 1) {
			addSequential(new ResetGyro());
			addSequential(new EncoderReset());

			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
			addSequential(new ElevatorPosition(3.5)); // raise arm

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch
			addSequential(new EncoderFrom(.8, .8, .5, .5, .5)); // drive and drop
			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-.5, -.5, .5, .5, .5));

		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void switchR(int mode) {
		if (mode == 1) {
			//FIXME LEFT POS RIGHT SWITCH
			addSequential(new ResetGyro());
			addSequential(new EncoderReset());

			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addParallel(new ElevatorPosition(1));
			addSequential(new EncoderGyro(11.4, 11.4, .35, .35, .4, 0, .017)); // drive to far side of switch

			addSequential(new EncoderFrom(.75, -1.5, .5, .5, .5)); // turn

			addSequential(new EncoderGyro(7.45, 7.45, .3, .3, .4, 90, .02)); // drive around switch

			//			addSequential(new GyroPos(90, .5, 1));

			addSequential(new ElevatorPosition(3.5)); // raise elevator and turns

			//			addSequential(new EncoderFrom(1, 1, .5, .5, .5)); // place
			//			addSequential(new IntakeTime(1, .5));
			//			addSequential(new EncoderFrom(-1, -1, .5, .5, .5));

		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void scaleL(int mode) {
		if (mode == 1) {
			addSequential(new ResetGyro());
			addSequential(new EncoderReset()); //reset

			addParallel(new DriveTime(.45, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.45, 0, .225)); // jog forward backwards to drop arm

//			addSequential(new ElevatorPosition(1)); //lift for drive
			addSequential(new ElevatorTime(.75, .75));
			addSequential(new EncoderGyro(12.3, 12.3, .5, .5, .6, 0, .016)); // drive to far side of switch
			addSequential(new EncoderFrom(.2, -.4, .4, .4, .5)); //turn to switch

			addSequential(new ElevatorPosition(15)); //raise and forward
			addSequential(new DriveTime(.4, 0, 1.7));

			addSequential(new IntakeTime(.4, 1)); //shoot, back up down and spin
			addSequential(new DriveTime(-.4, 0, 1.6));
			addSequential(new ElevatorTime(-.4, 10));
			addSequential(new GyroPos(135, .4, 1));

		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void scaleR(int mode) {
		if (mode == 1) {
			addSequential(new EncoderReset());
		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
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
