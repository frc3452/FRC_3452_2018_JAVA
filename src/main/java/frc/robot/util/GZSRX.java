package frc.robot.util;

import frc.robot.Robot;
import frc.robot.subsystems.Health.AlertLevel;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class GZSRX extends WPI_TalonSRX {

	private Breaker mBreaker = Breaker.NO_INFO;
	private Side mSide = Side.NO_INFO;
	private Master mMaster = Master.NO_INFO;

	public final static int TIMEOUT = 10;
	public final static int FIRMWARE = 778;

	// Drivetrain
	public GZSRX(GZSubsystem sub, int deviceNumber, Breaker breaker, Side side, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mMaster = master;

		checkFirmware(sub);
	}

	// Other subsystem
	public GZSRX(GZSubsystem sub, int deviceNumber, Breaker breaker, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mMaster = master;

		checkFirmware(sub);
	}

	// Basic
	public GZSRX(GZSubsystem sub, int deviceNumber, Breaker breaker) {
		super(deviceNumber);

		mBreaker = breaker;
		checkFirmware(sub);
	}

	public static void logError(ErrorCode errorCode, GZSubsystem subsystem, AlertLevel level, String message) {
		if (errorCode != ErrorCode.OK)
			Robot.health.addAlert(subsystem, level, message);
	}

	private void checkFirmware(GZSubsystem sub)
	{
		int firm = this.getFirmwareVersion();
	
		if(firm != FIRMWARE)
		{
			int id = this.getDeviceID();

			if (mSide != Side.NO_INFO)
				Robot.health.addAlert(sub, AlertLevel.ERROR, "Talon " + id + "(" + mSide + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else if (mMaster != Master.NO_INFO)
				Robot.health.addAlert(sub, AlertLevel.ERROR, "Talon " + id + "(" + mMaster + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else
				Robot.health.addAlert(sub, AlertLevel.ERROR, "Talon " + id + " firmware is " + firm + ", should be " + FIRMWARE);
		}
	}

	public Breaker getBreakerSize() {
		return mBreaker;
	}

	public Side getSide() {
		return mSide;
	}

	public Master getMaster() {
		return mMaster;
	}

	public enum Side {
		LEFT, RIGHT, NO_INFO
	}

	
	public enum Master {
		MASTER, FOLLOWER, NO_INFO
	}

	public enum Breaker {
		AMP_20, AMP_30, AMP_40, NO_INFO
	}

}
