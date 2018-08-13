package org.usfirst.frc.team3452.robot.util;

import java.util.List;

//thx 254
public  class GZSubsystemManager  {

	private final List<GZSubsystem> allSystems;

	public GZSubsystemManager(List<GZSubsystem> allSubsystems) {
		allSystems = allSubsystems;
	}

	public void loop() {
		allSystems.forEach((s) -> s.loop());
	}

	public void outputSmartDashboard() {
		allSystems.forEach((s) -> s.outputSmartDashboard());
	}

	public void stop() {
		allSystems.forEach((s) -> s.stop());
	}
	
	public void disable(boolean toDisable)
	{
		allSystems.forEach((s) -> s.disable(toDisable));
	}
	
	public boolean[] isDisabled()
	{
		boolean temp[] = null;

		for (int i = 0; i < allSystems.size(); i++) 
			temp[i] = allSystems.get(i).isDisabed();
		
		
		//TODO 3) IMPLEMENT ABILITY TO READ WHICH SUBSYSTEMS ARE DISABLED EASILY
//		allSystems.get(1).getName();
		//Arrays.toString(arr);
		
		return temp;
	}

	public void zeroSensors() {
		allSystems.forEach((s) -> s.zeroSensors());
	}
}
