package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	public Spark Intake_L, Intake_R;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public void initHardware() {
		Intake_L = new Spark(Constants.INTAKE_L);
		Intake_R = new Spark(Constants.INTAKE_R);

		Intake_L.setInverted(Constants.INTAKE_L_INVERT);
		Intake_R.setInverted(Constants.INTAKE_R_INVERT);

		Intake_L.setSubsystem("Intake");
		Intake_R.setSubsystem("Intake");

		Intake_L.setName("Intake L");
		Intake_R.setName("Intake R");
	}

	/**
	 * @author max
	 * @param speed
	 * @since
	 */
	public void manual(double speed) {
		Intake_L.set(speed);
		Intake_R.set(speed);
	}

	@Override
	public void initDefaultCommand() {
	}

	/**
	 * Intake speed constants
	 * 
	 * @author max
	 *
	 */
	public static class Speeds {
		public static final double INTAKE = -.8;
		public static final double SHOOT = .75;
		public static final double SLOW = .3;
		public static final double PLACE = .2;
		public static final double SPIN = .35;
	}

}
