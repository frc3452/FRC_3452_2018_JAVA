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
			// TODO RIGHT POS LEFT SWITCH
			addSequential(new EncoderReset());
		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void switchR(int mode) {
		if (mode == 1) {
			//FIXME Test Robot Right Switch Right 
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

		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void scaleL(int mode) {
		if (mode == 1) {
			addSequential(new EncoderReset());
		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void scaleR(int mode) {
		if (mode == 1) {
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

		} else if (mode == 2) {
			addSequential(new EncoderReset());
		} else {
			defaultAuton();
		}
	}

	private void defaultAuton() {
		// TODO add default
		addSequential(new EncoderReset());
		
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(0, 0, 1));
		addSequential(new ElevatorPosition(2));
		addSequential(new DriveTime(.2, 0, 3));
	}

}
