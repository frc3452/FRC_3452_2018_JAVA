package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team3452.robot.Constants;

/**
 * <h1>Climber subsystem</h1>
 * Handles climber.
 * 
 * @author max
 *
 */
public class Climber extends Subsystem {
	public Spark climb1;
	public int climbCounter = 0;

	/**
	 * one direction control of climber
	 * 
	 * @author max
     * @param speed double
	 */
	public void control(double speed) {
		climb1.set(Math.abs(speed));
	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Climber() {
		climb1 = new Spark(Constants.kClimber.CLIMBER_1);
		climb1.setInverted(Constants.kClimber.CLIMBER_1_INVERT);

		climb1.setSubsystem("Climber");
		climb1.setName("Climber Motor");
	}

	@Override
	public void initDefaultCommand() {
	}

}
