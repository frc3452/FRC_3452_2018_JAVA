package org.usfirst.frc.team3452.robot.motionprofiles;

/**
 * Contains everything a motion profile needs
 * @author Max
 * @since 5/29/18
 */
public interface Path {
	public double[][] mpL();
	public double[][] mpR();
	public Integer mpDur();
}
