package frc.robot.util;

import java.util.List;

import frc.robot.util.Util.Directions;

import edu.wpi.first.wpilibj.Joystick;

public class GZJoystick extends Joystick {

	DPad mUp, mDown, mRight, mLeft;
	private LatchedBoolean a,b,x,y,lb,rb,back,start,lclick,rclick;

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

	public Double getLeftAnalogY() {
		return -this.getRawAxis(Axises.LEFT_ANALOG_Y);
	}

	public Double getLeftAnalogX() {
		return this.getRawAxis(Axises.LEFT_ANALOG_X);
	}

	public Double getRightAnalogY() {
		return -this.getRawAxis(Axises.RIGHT_ANALOG_Y);
	}

	public Double getRightAnalogX() {
		return this.getRawAxis(Axises.RIGHT_ANALOG_X);
	}

	public Double getLeftTrigger() {
		return this.getRawAxis(Axises.LEFT_TRIGGER);
	}

	public Double getRightTrigger() {
		return this.getRawAxis(Axises.RIGHT_TRIGGER);
	}

	public Boolean isAPressed()
	{
		return a.update(this.getRawButton(Buttons.A));
	} 

	public Boolean isBPressed()
	{
		return b.update(this.getRawButton(Buttons.B));
	}

	public Boolean isXPressed()
	{
		return x.update(this.getRawButton(Buttons.X));
	}

	public Boolean isYPressed()
	{
		return y.update(this.getRawButton(Buttons.Y));
	}

	public Boolean isLBPressed()
	{
		return lb.update(this.getRawButton(Buttons.LB));
	}

	public Boolean isRBPressed()
	{
		return rb.update(this.getRawButton(Buttons.RB));
	}

	public Boolean isBackPressed()
	{
		return back.update(this.getRawButton(Buttons.BACK));
	}

	public Boolean isStartPressed()
	{
		return start.update(this.getRawButton(Buttons.START));
	}

	public Boolean isLClickPressed()
	{
		return lclick.update(this.getRawButton(Buttons.LEFT_CLICK));
	}

	public Boolean isRClickPressed()
	{
		return rclick.update(this.getRawButton(Buttons.RIGHT_CLICK));
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

	public void rumble(Double intensity) {
		this.setRumble(RumbleType.kLeftRumble, intensity);
		this.setRumble(RumbleType.kRightRumble, intensity);
	}

}
