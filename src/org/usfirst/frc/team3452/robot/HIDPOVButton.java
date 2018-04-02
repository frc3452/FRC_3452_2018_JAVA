package org.usfirst.frc.team3452.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.buttons.Button;

//https://github.com/Aztechs157/FRC-2017/blob/master/src/org/usfirst/frc157/ProtoBot2017/HIDPOVButton.java

public class HIDPOVButton  extends Button{

	private GenericHID stick;
	private int povAngle;
	
	  /**
	   * Enable POV stick as a set of buttons based on direction
	   *
	   * @param stick - the stick with the axis to use as a button
	   * @param angle - POV stick angle to treat as a button press (e.g. 0,45,90,135 etc...) 
	   **/

	HIDPOVButton(GenericHID stick, int povAngle)
	{
		this.stick = stick;
		this.povAngle = povAngle; 				
	}	
	
	/**
	 * Gets the value of the joystick button
	 *
	 * @return The value of the joystick button
	 */
	@Override
	public boolean get() {
			return (stick.getPOV() == povAngle);
	}
}

