package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToCube;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToStop;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderDrive;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderFrom;
import org.usfirst.frc.team3452.robot.commands.drive.EncoderReset;
import org.usfirst.frc.team3452.robot.commands.drive.GyroReset;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorTime;
import org.usfirst.frc.team3452.robot.commands.elevator.ElevatorWhileDrive;
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeTime;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AO;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AV;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class MiddleAuton extends CommandGroup {

	/**
	 * @param option
	 * @param version
	 * @see AutonSelector
	 */
	public MiddleAuton(AO option, AV version) {
		addSequential(new EncoderReset());
		addSequential(new GyroReset());

		//IF DATA FOUND
		if (Robot.autonSelector.gameMsg != "NOT") {

			switch (option) {
			case SWITCH:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(version);
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'R') {
					switchR(version);
				} else {
					defaultAuton();
				}

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
			//if game data bad
			defaultAuton();
		}

		addSequential(new DriveTime(0, 0, 16));
	}

	private void switchL(AV version) {
		switch (version) {
		case CURRENT:

			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderDrive(1, 2.2, .8, .8, .6)); //init curve

			//drive to switch
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .2/*.8*/));
					addSequential(new EncoderFrom(4.2, 3.6, .6, .6, .7));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55)); //was .45

			addSequential(new IntakeTime(.5, .5)); //first place

			//			back up
			//			addParallel(new DriveTime(-.5, 0, 1.5));
			//			addSequential(new ElevatorTime(-.1, 10));

			//backup
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(-15, .75));
					addSequential(new EncoderFrom(-3.8, -3.8, .5, .5, .6));
				}
			});

			addSequential(new EncoderReset());

			//TURN AND GRAB CUBE
			addSequential(new CommandGroup() {
				{
					addSequential(new EncoderDrive(.45, -.4, .5, .5, .6)); //turn to switch
					addSequential(new DriveToCube(.58, 5)); //was .45
					addParallel(new IntakeTime(-.2, .75));
					addSequential(new EncoderDrive(0, 0, .6, .6, .65));
				}
			});

			//DRIVE TO SWITCH
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderFrom(3, 3, .5, .5, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55)); //was .45

			addSequential(new IntakeTime(.5, .5)); //second place

			//back up
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(-.1, 10));

			break;
		default:
			defaultAuton();
			break;
		}

	}

	private void switchR(AV version) {
		switch (version) {
		case CURRENT:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderDrive(2.5, 1, .8, .8, .6));

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .3));
					addSequential(new EncoderFrom(3.3 /*+ .15*/, 4.2 + .38, .6, .6, .7));
				}
			});

			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55));

			addSequential(new IntakeTime(.5, .5)); //PLACE 1

			//back up
			//			addParallel(new DriveTime(-.5, 0, 1.5));
			//			addSequential(new ElevatorTime(-.1, 10));

			//backup
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(-15, .75));
					addSequential(new EncoderFrom(-3.8, -3.8, .6, .6, .6));
				}
			});

			//lower and reset encoders
			addSequential(new EncoderReset());

			//TURN AND GRAB CUBE
			addSequential(new CommandGroup() {
				{
					addSequential(new EncoderFrom(-.4, .35, .5, .5, .6)); //turn to switch
					addSequential(new DriveToCube(.58, 5));
					addParallel(new IntakeTime(-.5, .75));
					addSequential(new EncoderDrive(0, 0, .6, .6, .65));
				}
			});

			//DRIVE TO SWITCH
			addSequential(new CommandGroup() {
				{
					addSequential(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderFrom(3, 3, .5, .5, .6));
				}
			});

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55));

			addSequential(new IntakeTime(.5, 1.5)); //second place

			//back up
			addParallel(new DriveTime(-.5, 0, 1.5));
			addSequential(new ElevatorTime(-.1, 10));

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

		addSequential(new EncoderFrom(7.2, 2, .35, .35, .5));
		addSequential(new DriveTime(-.4, 0, 7));
	}
}
