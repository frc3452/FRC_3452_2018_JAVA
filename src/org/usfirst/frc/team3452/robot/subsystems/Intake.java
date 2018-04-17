package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	public Spark Intake_L, Intake_R;

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

	public void manual(double speed) {
		Intake_L.set(speed);
		Intake_R.set(speed);
	}

	public void initDefaultCommand() {
	}

	public static class Speeds 
	{
		public static final double INTAKE = -.8;
		public static final double OUT = .75;
		public static final double SLOW = .3;
		public static final double SPIN = 3.5;
	}
	
	public static class Constants {
		public static final int INTAKE_L = 0;
		public static final int INTAKE_R = 1;

		public static final boolean INTAKE_L_INVERT = true;
		public static final boolean INTAKE_R_INVERT = false;
	}
}
