package org.usfirst.frc.team3452.robot.util;

import java.util.List;


//thx 254
public class GZSubsystemManager {

	private final List<GZSubsystem> allSystems;

	public GZSubsystemManager(List<GZSubsystem> allSubsystems) {
		allSystems = allSubsystems;
	}

	public void loop() {
		allSystems.forEach((s) -> s.loop());
	}

	public void outputToSmartDashboard() {
		allSystems.forEach((s) -> s.outputSmartDashboard());
	}

	public void stop() {
		allSystems.forEach((s) -> s.stop());
	}

	public void zeroSensors() {
		allSystems.forEach((s) -> s.zeroSensors());
	}
}
