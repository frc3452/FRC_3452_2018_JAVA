package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <h1>Climber subsystem</h1>
 * Handles climber.
 * 
 * @author max
 *
 */
public class Climber extends Subsystem {
	private Spark Climber;
	public int climbCounter = 0;
	
	public void control(double speed) {
		Climber.set(Math.abs(speed));
	}

	public Climber() {
		Climber = new Spark(Constants.kClimber.CLIMBER_1);
		Climber.setInverted(Constants.kClimber.CLIMBER_1_INVERT);

		Climber.setSubsystem("Climber");
		Climber.setName("Climber Motor");
	}

	@Override
	public void initDefaultCommand() {
	}

}
