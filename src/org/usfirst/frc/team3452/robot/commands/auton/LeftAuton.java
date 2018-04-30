package org.usfirst.frc.team3452.robot.commands.auton;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.commands.drive.DriveTime;
import org.usfirst.frc.team3452.robot.commands.drive.DriveToCube;
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
import org.usfirst.frc.team3452.robot.commands.pwm.IntakeWhileDrive;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AO;
import org.usfirst.frc.team3452.robot.subsystems.AutonSelector.AV;
import org.usfirst.frc.team3452.robot.subsystems.Intake;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class LeftAuton extends CommandGroup {

	/**
	 * @param option
	 * @param version
	 * @see AutonSelector
	 */
	public LeftAuton(AO option, AV version) {
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
				}

				break;
			case SCALE:
				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(/* version */AV.FOREST_HILLS);

				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'R') {
					scaleR(version);
				}
				break;
			case SWITCH_PRIORITY_NO_CROSS:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(version);
				} else if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(version);
				} else {
					defaultAuton();
				}

				break;
			case SCALE_PRIORITY_NO_CROSS:

				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(version);
				} else if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(version);
				} else {
					defaultAuton();
				}

				break;
			case SCALE_ONLY:

				if (Robot.autonSelector.gameMsg.charAt(1) == 'L') {
					scaleL(version);
				} else {
					defaultAuton();
				}

				break;
			case SWITCH_ONLY:

				if (Robot.autonSelector.gameMsg.charAt(0) == 'L') {
					switchL(version);
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
			//game data not found
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

			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .7));
					addSequential(new EncoderGyro(7.91, 7.91, .6, .6, .7, 0, .017)); // drive to side of switch
				}
			});

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch

			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55)); //hit to switch

			addSequential(new IntakeTime(1, .5)); //drop and back up
			addParallel(new DriveTime(-.5, 0, .8));
			addSequential(new ElevatorTime(-.15, 10));

			break;

		case FOREST_HILLS:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

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

	private void switchR(AV version) {
		switch (version) {
		case CURRENT:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addParallel(new IntakeTime(-.2, 15));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.6, 11.6, .5, .5, .6, 0, .017)); // drive to side of switch

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .6)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new CommandGroup() {
				{
					addParallel(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderGyro(11.25, 11.25, .5, .5, .6, 90, 0.021)); //drive back of switch
				}
			});

			addSequential(new EncoderFrom(1.6, -1.2, .5, .5, .6));

			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55)); //was .45

			addSequential(new IntakeTime(.75, .25));

			addParallel(new ElevatorWhileDrive(-15, .9));
			addSequential(new EncoderFrom(-.85, -.75, .5, .5, .6));

			addSequential(new DriveToCube(.58, 5)); //was .45
			addSequential(new EncoderFrom(-.5, -.5, .5, .5, .6));

			addSequential(new ElevatorTime(1, .6));

			//hit switch
			addSequential(new DriveTime(.5, 0, .5));
			addSequential(new DriveToStop(.55)); //was .45

			addSequential(new IntakeTime(-1, 1));

			break;
		case FOREST_HILLS:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.2, 11.2, .4, .4, .4, 0, .017)); // drive to side of switch

			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());

			addSequential(new CommandGroup() {
				{
					addSequential(new ElevatorWhileDrive(3.5, .6));
					addSequential(new EncoderGyro(6.95, 6.95, .4, .4, .4, 90, 0.021)); //drive back of switch
				}
			});

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

	private void scaleL(AV version) {
		switch (version) {
		case CURRENT:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			//Drive to scale
			addSequential(new EncoderGyro(15.27, 15.27, .6, .6, .7, 0, .017));
			//TURN CHANGED FINALS 3
			addSequential(new EncoderFrom(1.5, -1.15, .6, .6, .6)); //turn to switch

			addSequential(new DriveTime(-.5, 0, .5));
			addSequential(new DriveToStop(-.55)); //was .45
			
			addSequential(new ElevatorPosition(15)); //raise and forward
			addSequential(new EncoderFrom(.5, .5, .4, .4, .6));

			addSequential(new IntakeTime(.8, 4)); //shoot, back up down and spin

			break;
		case FOREST_HILLS:

			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new ElevatorTime(.75, .25));
			addSequential(new EncoderGyro(12.3, 12.3, .5, .5, .6, 0, .016)); // drive to far side of switch

			addSequential(new EncoderFrom(.2, -.4, .4, .4, .5)); //turn to switch
			addSequential(new ElevatorPosition(15)); //raise and forward

			addSequential(new DriveTime(.4, 0, 1.7));

			addSequential(new IntakeTime(.4, .6)); //shoot, back up down and spin
			addSequential(new DriveTime(-.4, 0, 1.6));

			addParallel(new ElevatorTime(-.65, 10));
			addSequential(new GyroPos(135, .4, 1));

			break;
		default:
			defaultAuton();
			break;
		}

	}

	private void scaleR(AV version) {
		switch (version) {
		case CURRENT:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

			addSequential(new EncoderGyro(11.7, 11.7, .6, .6, .65, 0, .017)); // drive to side of switch

			addParallel(new ElevatorPosition(2)); //raise
			addSequential(new EncoderFrom(0.75, -1.5, .5, .5, .5)); // turn to switch 

			addSequential(new EncoderReset());
			addSequential(new EncoderGyro(10.1, 10.1, .5, .5, .5, 90, 0.011)); //drive front of scale

			addSequential(new EncoderFrom(-1.5, .75, .5, .5, .5));

			addSequential(new ElevatorPosition(15)); //raise and turn to switch

			addSequential(new CommandGroup() {
				{
					addParallel(new IntakeWhileDrive(Intake.Speeds.SLOW, .92, 3));
					addSequential(new EncoderFrom(2.41, 2.61, .1, .1, .15));
				}

			});
			addSequential(new DriveTime(-.4, 0, 1.6));

			addParallel(new ElevatorTime(-.65, 10));
			addSequential(new GyroPos(225, .4, 1));

			break;
		case FOREST_HILLS:
			addParallel(new DriveTime(.55, 0, .5));
			addSequential(new ElevatorTime(.5, .1725));
			addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

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
		addParallel(new DriveTime(.55, 0, .5));
		addSequential(new ElevatorTime(.5, .1725));
		addSequential(new DriveTime(-.55, 0, .225)); // jog forward backwards to drop arm

		addSequential(new EncoderGyro(7.91, 7.91, .4, .4, .4, 0, .017)); // drive to side of switch
		addSequential(new ElevatorPosition(3.5)); // raise arm
	}

}
