package org.usfirst.frc.team3452.robot.util;

import java.util.List;

//thx 254
public class GZSubsystemManager {

	private final List<GZSubsystem> allSystems;

	public GZSubsystemManager(List<GZSubsystem> allSubsystems) {
		allSystems = allSubsystems;
	}

	/**
	 * Run .loop() for each of the subsystems in the subsystem manager.
	 */
	public void loop() {
		allSystems.forEach((s) -> s.loop());
	}

	public void outputSmartDashboard() {
		allSystems.forEach((s) -> s.outputSmartDashboard());
	}

	public void stop() {
		allSystems.forEach((s) -> s.stop());
	}

	public void disable(boolean toDisable) {
		allSystems.forEach((s) -> s.disable(toDisable));
	}
	
	public void enableFollower()
	{
		allSystems.forEach((s) -> s.enableFollower());
	}

	public void whatIsDisabled(boolean printHeader) {
		if (printHeader)
			System.out.println("~~~SUBSYSTEMS DISABLED~~~");
		
		for (int i = 0; i < allSystems.size() - 1; i++)
			System.out.println(allSystems.get(i).getName() + ": " + allSystems.get(i).isDisabed());
	}
	
	public void printStates(boolean printHeader)
	{
		if (printHeader)
			System.out.println("~~~SUBSYSTEM STATES~~~");
		
		for (int i = 0; i < allSystems.size() - 1; i++)
			System.out.println(allSystems.get(i).getName() + ": " + allSystems.get(i).getStateString());
		
	}

	public void zeroSensors() {
		allSystems.forEach((s) -> s.zeroSensors());
	}
}
