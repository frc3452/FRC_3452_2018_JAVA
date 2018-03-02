package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {
	public static Intake instance = new Intake();

	public static Spark intake_l = new Spark(Constants.INTAKE_L);
	public static Spark intake_r = new Spark(Constants.INTAKE_R);

	public void initHardware()
	{
		intake_l.setInverted(Constants.INTAKE_L_INVERT);
		intake_r.setInverted(Constants.INTAKE_R_INVERT);
		
		intake_l.setSubsystem("Intake");
		intake_r.setSubsystem("Intake");
		
		intake_l.setName("Intake L");
		intake_r.setName("Intake R");;
	}
	
	public void manual(double speed)
	{
		intake_l.set(speed);
		intake_r.set(speed);
	}
	
	public void initDefaultCommand() {
	}

	public static class Constants {
		public static final int INTAKE_L = 0;
		public static final int INTAKE_R = 1;

		public static final boolean INTAKE_L_INVERT = false;
		public static final boolean INTAKE_R_INVERT = true;

		// COMP BOT
		// public static final boolean INTAKE_L_INVERT = true;
		// public static final boolean INTAKE_R_INVERT = false;
	}

	public static Intake getInstance() {
		return instance;
	}
}
