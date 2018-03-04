package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.commands.pwm.IntakeControl;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	public static Spark Intake_L;
	public static Spark Intake_R;

	public static Intake instance = new Intake();

	public void initHardware() {
		Intake_L = new Spark(Constants.INTAKE_L);
		Intake_R = new Spark(Constants.INTAKE_R);

		Intake_L.setInverted(Constants.INTAKE_L_INVERT);
		Intake_R.setInverted(Constants.INTAKE_R_INVERT);

		Intake_L.setSubsystem("Intake");
		Intake_R.setSubsystem("Intake");

		Intake_L.setName("Intake L");
		Intake_R.setName("Intake R");
		;
	}

	public void manual(double speed) {
		Intake_L.set(speed);
		Intake_R.set(speed);
	}

	public void initDefaultCommand() {
		setDefaultCommand(new IntakeControl(0, 0));
	}

	public static class Constants {
		public static final int INTAKE_L = 0;
		public static final int INTAKE_R = 1;

		 public static final boolean INTAKE_L_INVERT = true;
		 public static final boolean INTAKE_R_INVERT = false;
	}
}
