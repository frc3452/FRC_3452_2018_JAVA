package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	public static Climber instance = new Climber();

	private static Spark Climb_1 = new Spark(Constants.CLIMBER_1);

	public void Control(double speed) {
		Climb_1.set(Math.abs(speed));
	}

	public void initHardware() {
		Climb_1.setInverted(Constants.CLIMBER_1_INVERT);
		
		Climb_1.setSubsystem("Climber");
		Climb_1.setName("Climb_1");
	}

	public void initDefaultCommand() {
	}

	public static Climber getInstance() {
		return instance;
	}

	public static class Constants {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;

	}
}
