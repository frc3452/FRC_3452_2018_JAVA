package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	private Spark climber;
	public int climbCounter = 0;

	public void control(double speed) {
		climber.set(Math.abs(speed));
	}

	public void initHardware() {
		climber = new Spark(Constants.CLIMBER_1);
		climber.setInverted(Constants.CLIMBER_1_INVERT);

		climber.setSubsystem("Climber");
		climber.setName("Climber Motor");
	}

	public void initDefaultCommand() {
	}

	public static class Constants {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;
	}
}
