package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorPosition;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.commands.signal.WaitForGameData;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MiddleAuton extends CommandGroup {

	public MiddleAuton(String priority, int selector) {
		addSequential(new GyroReset());
		addSequential(new EncoderReset());
		addSequential(new WaitForGameData());

		if (priority == "D") {
			defaultAuton();
		} else {

			//IF DATA FOUND
			if (Robot.autonSelector.gameMsg != "NOT") {

				if (priority == "SWITCH") {
					if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
						switchL(selector);
						addSequential(new DriveTime(0, 0, 16));
					} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
						switchR(selector);
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
	}

	private void switchL(int mode) {
		//COMPLETE
		switch (mode) {
		case 1:
			break;
		case 2:
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void switchR(int mode) {
		//COMPLETE
		switch (mode) {
		case 1:
			break;
		case 2:
			break;
		default:
			defaultAuton();
			break;
		}
	}

	private void defaultAuton() {
		addSequential(new EncoderFrom(1.8, 6.7, .5, .5, .5));
		addSequential(new EncoderFrom(-3.15, -3.15, .5, .5, .5));

		addSequential(new DriveTime(.25, 0, .5));
		addSequential(new ElevatorTime(.5, .15));
		addSequential(new DriveTime(.125, 0, 1));
	}
}
