package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
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
			} else {
				defaultAuton();
			}
		} else if (priority == "SCALE") {
			//TODO Left Scale
			if (Robot.lights.gsm().charAt(1) == 'L') {
				scaleL(selector);
			} else if (Robot.lights.gsm().charAt(1) == 'R') {
				scaleR(selector);
			} else {
				defaultAuton();
			}
		} else {
			defaultAuton();
		}
	}

	private void switchL(int mode) {
		if (mode == 1) {
			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderFrom(7.91, 7.91, .3, .3, .4)); // drive to side of switch
			addParallel(new ElevatorPosition(3)); // raise arm

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch
			addSequential(new EncoderFrom(.65, .65, .5, .5, .5)); // drive and drop
			addSequential(new IntakeTime(1, .5));

		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void switchR(int mode) {
		if (mode == 1) {
			//FIXME LEFT POS RIGHT SWITCH
			addSequential(new ResetGyro());

			addParallel(new DriveTime(.25, 0, .5));
			addSequential(new ElevatorTime(.5, .15));
			addSequential(new DriveTime(-.25, 0, .225)); // jog forward backwards to drop arm

			addParallel(new ElevatorPosition(1));
			addSequential(new EncoderFrom(11.3, 11.3, .35, .35, .5)); // drive to far side of switch

			addSequential(new EncoderFrom(.75, -1.5, .5, .5, .5)); // turn

			addSequential(new GyroPos(90, .2, 1)); // fix turn
			addSequential(new DriveTime(0, 0, .25));

			addSequential(new EncoderFrom(5.5, 5.5, .3, .3, .4)); // drive around switch

			addParallel(new ElevatorPosition(3.5)); // raise elevator and turns
			addSequential(new GyroPos(135, .3, 1));

			addSequential(new EncoderFrom(1, 1, .5, .5, .5)); // place
			addSequential(new IntakeTime(1, .5));
			addSequential(new EncoderFrom(-1, -1, .5, .5, .5));

		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void scaleL(int mode) {
		if (mode == 1) {

		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void scaleR(int mode) {
		if (mode == 1) {

		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void defaultAuton() {
		//TODO add default
		
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(0, 0, 1));
		addSequential(new ElevatorPosition(2));
		addSequential(new DriveTime(.2, 0, 3));
	}

}
