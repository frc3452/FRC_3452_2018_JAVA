package frc.robot.util;

import java.util.List;

//thx 254
public class GZSubsystemManager {

	private final List<GZSubsystem> mAllSystems;

	public GZSubsystemManager(List<GZSubsystem> allSubsystems) {
		mAllSystems = allSubsystems;
	}

	/**
	 * Run .loop() for each of the subsystems in the subsystem manager.
	 */
	public void loop() {
		mAllSystems.forEach((s) -> s.loop());
	}
	
	public List<GZSubsystem> getSubsystems()
	{
		return mAllSystems;
	}

	public void construct() {
		mAllSystems.forEach((s) -> s.construct());
	}

	// TODO ISSUE #14, notifier for loop()

	public void outputSmartDashboard() {
		mAllSystems.forEach((s) -> s.outputSmartDashboard());
	}

	public void stop() {
		mAllSystems.forEach((s) -> s.stop());
	}

	public void disable(boolean toDisable) {
		mAllSystems.forEach((s) -> s.disable(toDisable));
	}

	public void enableFollower() {
		mAllSystems.forEach((s) -> s.enableFollower());
	}

	public void whatIsDisabled(boolean printHeader) {
		if (printHeader)
			System.out.println("~~~SUBSYSTEMS DISABLED~~~");

		for (int i = 0; i < mAllSystems.size() - 1; i++)
			System.out.println(mAllSystems.get(i).getName() + ": " + mAllSystems.get(i).isDisabed());
	}

	public void printStates(boolean printHeader) {
		if (printHeader)
			System.out.println("~~~SUBSYSTEM STATES~~~");

		for (int i = 0; i < mAllSystems.size() - 1; i++)
			System.out.println(mAllSystems.get(i).getName() + ": " + mAllSystems.get(i).getStateString());

	}

	public void zeroSensors() {
		mAllSystems.forEach((s) -> s.zeroSensors());
	}
}
