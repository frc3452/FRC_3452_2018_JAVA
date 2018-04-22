package org.usfirst.frc.team3452.robot.subsystems;

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
	public Spark climb1;
	public int climbCounter = 0;

	/**
	 * one direction control of climber
	 * 
	 * @author max
	 * @param speed 
	 * @since
	 */
	public void control(double speed) {
		climb1.set(Math.abs(speed));
	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public void initHardware() {
		climb1 = new Spark(Constants.CLIMBER_1);
		climb1.setInverted(Constants.CLIMBER_1_INVERT);

		climb1.setSubsystem("Climber");
		climb1.setName("Climber Motor");
	}

	public void initDefaultCommand() {
	}

	/**
	 * @author max
	 *
	 */
	public static class Constants {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;
	}
}
