package org.usfirst.frc.team3452.robot.util;

import java.util.List;

import org.usfirst.frc.team3452.robot.util.Util.Directions;

import edu.wpi.first.wpilibj.Joystick;

public class GZJoystick extends Joystick {

	DPad mUp, mDown, mRight, mLeft;

	public GZJoystick(int port) {
		super(port);

		mUp = new DPad(this, 0);
		mDown = new DPad(this, 180);
		mLeft = new DPad(this, 270);
		mRight = new DPad(this, 90);
	}

	public boolean getDPad(Directions d) {
		switch (d) {
		case DOWN:
			return mDown.get();
		case LEFT:
			return mLeft.get();
		case RIGHT:
			return mRight.get();
		case UP:
			return mRight.get();
		default:
			System.out.println("DEFAULT CASE REACHED FOR " + new Throwable().getStackTrace()[0].getMethodName() + " OF " + this.getClass() + "!!!");
			return false;
		}
	}
	
	public boolean areButtonsPressed(List<Integer> buttons) {
		boolean retval = true;

		for (Integer b : buttons)
			retval &= this.getRawButton(b);

		return retval;
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

	public double getLeftTrigger() {
		return this.getRawAxis(Axises.LEFT_TRIGGER);
	}

	public double getRightTrigger() {
		return this.getRawAxis(Axises.RIGHT_TRIGGER);
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

	public void rumble(double intensity) {
		this.setRumble(RumbleType.kLeftRumble, intensity);
		this.setRumble(RumbleType.kRightRumble, intensity);
	}

}
