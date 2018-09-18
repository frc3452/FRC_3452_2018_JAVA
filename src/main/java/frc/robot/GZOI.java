package frc.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import frc.robot.Constants.kOI;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZSubsystem;
import frc.robot.util.Util;

public class GZOI extends GZSubsystem {
	public static GZJoystick driverJoy = new GZJoystick(0);
	public static GZJoystick opJoy = new GZJoystick(1);

	private boolean mWasTele = false, mWasAuto = false, mWasTest = false;

	public GZOI() {

	}

	public synchronized void construct() {
		driverJoy = new GZJoystick(0);
		opJoy = new GZJoystick(1);
	}

	@Override
	public void loop() {
		if (isTele())
			mWasTele = true;
		if (isAuto())
			mWasAuto = true;
		if (isTest())
			mWasTest = true;


		if (isTele()) {
			if (driverJoy.isAPressed())
				Robot.drive.slowSpeed(!Robot.drive.isSlow());
		}

		// controller rumble
		if (Util.between(getMatchTime(), 29.1, 30))
			rumble(Controller.BOTH, kOI.Rumble.ENDGAME);
		else
			rumble(Controller.BOTH, 0);
	}

	private static void rumble(Controller joy, double intensity) {
		switch (joy) {
		case DRIVE:
			driverJoy.setRumble(RumbleType.kLeftRumble, intensity);
			driverJoy.setRumble(RumbleType.kRightRumble, intensity);
			break;
		case OP:
			opJoy.setRumble(RumbleType.kLeftRumble, intensity);
			opJoy.setRumble(RumbleType.kRightRumble, intensity);
			break;
		case BOTH:
			rumble(Controller.DRIVE, intensity);
			rumble(Controller.OP, intensity);
			break;
		}
	}

	private static enum Controller {
		DRIVE, OP, BOTH
	}

	public boolean isFMS() {
		return DriverStation.getInstance().isFMSAttached();
	}

	public boolean isRed() {
		if (DriverStation.getInstance().getAlliance() == Alliance.Red)
			return true;

		return false;
	}

	public double getMatchTime() {
		return DriverStation.getInstance().getMatchTime();
	}

	public boolean isAuto() {
		return DriverStation.getInstance().isAutonomous();
	}

	public boolean isDisabled() {
		return DriverStation.getInstance().isDisabled();
	}

	public boolean isTele() {
		return DriverStation.getInstance().isEnabled() && !isAuto() && !isTest();
	}

	public boolean isTest() {
		return DriverStation.getInstance().isTest();
	}

	public boolean wasTele() {
		return mWasTele;
	}

	public boolean wasAuto() {
		return mWasAuto;
	}

	public boolean wasTest() {
		return mWasTest;
	}

	@Override
	public String getStateString() {
		return "NA";
	}

	public void stop() {
	}

	protected void in() {
	}

	protected void out() {
	}

	protected void initDefaultCommand() {
	}
}