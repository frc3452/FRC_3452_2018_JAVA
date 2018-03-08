package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Climber extends Subsystem {
	private Spark climb_1, climb_release;

	public void control(double speed) {
		climb_1.set(Math.abs(speed));
	}

	public void release(double release) {
		climb_release.set(Math.abs(release));
	}

	public void initHardware() {
		climb_1 = new Spark(Constants.CLIMBER_1);
		climb_1.setInverted(Constants.CLIMBER_1_INVERT);
		
		climb_release = new Spark(Constants.CLIMBER_RELEASE);
		climb_release.setInverted(Constants.CLIMBER_RELEASE_INVERT);

		climb_1.setSubsystem("Climber");
		climb_1.setName("climb_1");

		climb_release.setSubsystem("Climber");
		climb_release.setName("Climb_release");
	}

	public void initDefaultCommand() {
	}

	public static class Constants {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;

		public static final int CLIMBER_RELEASE = 3;
		public static final boolean CLIMBER_RELEASE_INVERT = false;

	}
}
