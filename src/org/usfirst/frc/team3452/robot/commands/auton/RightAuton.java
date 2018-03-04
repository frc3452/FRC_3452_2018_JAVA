package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
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
			} else {
				defaultAuton();
			}
		} else if (priority == "SCALE") {
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

		} else if (mode == 2) {

		} else {
			defaultAuton();
		}
	}

	private void switchR(int mode) {
		if (mode == 1) {

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
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(0, 0, 1));
		addSequential(new ElevatorPosition(2));
		addSequential(new DriveTime(.2, 0, 3));
	}

}
