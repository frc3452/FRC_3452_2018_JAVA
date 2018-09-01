package org.usfirst.frc.team3452.robot.util;

import java.util.List;

import edu.wpi.first.wpilibj.Joystick;

public class GZJoystick extends Joystick {

	public GZJoystick(int port) {
		super(port);
	}

	public double getLeftAnalogY() {
		return -this.getRawAxis(Axises.LEFT_ANALOG_Y);
	}

	public double getLeftAnalogX() {
		return this.getRawAxis(Axises.LEFT_ANALOG_X);
	}

	public double getRightAnalogY() {
		return -this.getRawAxis(Axises.RIGHT_ANALOG_Y);
	}

	public double getRightAnalogX() {
		return this.getRawAxis(Axises.RIGHT_ANALOG_X);
	}
	
	public double getLeftTrigger()
	{
		return this.getRawAxis(Axises.LEFT_TRIGGER);
	}
	
	public double getRightTrigger()
	{
		return this.getRawAxis(Axises.RIGHT_TRIGGER);
	}
	
	public boolean areButtonsPressed(List<Integer> buttons)
	{
		boolean retval = true;
		
		for (Integer b : buttons)
			retval &= this.getRawButton(b);
		
		return retval;
	}

	public static class Axises {
		public static int LEFT_ANALOG_X = 0;
		public static int LEFT_ANALOG_Y = 1;
		public static int RIGHT_ANALOG_X = 4;
		public static int RIGHT_ANALOG_Y = 5;
		public static int LEFT_TRIGGER = 2;
		public static int RIGHT_TRIGGER = 3;
	}

	public static class Buttons {
		public static int A = 1;
		public static int B = 2;
		public static int X = 3;
		public static int Y = 4;
		public static int LB = 5;
		public static int RB = 6;
		public static int BACK = 7;
		public static int START = 8;
		public static int LEFT_CLICK = 9;
		public static int RIGHT_CLICK = 10;
	}
	
	public void rumble(double intensity)
	{
		this.setRumble(RumbleType.kLeftRumble, intensity);
		this.setRumble(RumbleType.kRightRumble, intensity);
	}

}
