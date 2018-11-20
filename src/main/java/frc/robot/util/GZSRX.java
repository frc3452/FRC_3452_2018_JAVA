package frc.robot.util;

import frc.robot.Robot;
import frc.robot.subsystems.Health;
import frc.robot.subsystems.Health.AlertLevel;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class GZSRX extends WPI_TalonSRX {

	private Breaker mBreaker = Breaker.NO_INFO;
	private Side mSide = Side.NO_INFO;
	private Master mMaster = Master.NO_INFO;

	public final static int TIMEOUT = 10;
	public final static int FIRMWARE = 778;
	private final static AlertLevel mFirmwareLevel = AlertLevel.WARNING;

	// Drivetrain
	public GZSRX(int deviceNumber, Breaker breaker, Side side, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mMaster = master;
	}

	// Other subsystem
	public GZSRX(int deviceNumber, Breaker breaker, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mMaster = master;
	}

	// Basic
	public GZSRX(int deviceNumber, Breaker breaker) {
		super(deviceNumber);

		mBreaker = breaker;
	}


	public boolean isEncoderValid()
	{
		return this.getSensorCollection().getPulseWidthRiseToRiseUs() != 0;
	}
	
	public static void logError(ErrorCode errorCode, GZSubsystem subsystem, AlertLevel level, String message) {
		if (errorCode != ErrorCode.OK)
			Health.getInstance().addAlert(subsystem, level, message);
	}

	public void checkFirmware(GZSubsystem sub)
	{
		int firm = this.getFirmwareVersion();
	
		if(firm != FIRMWARE)
		{
			int id = this.getDeviceID();

			if (mSide != Side.NO_INFO)
				Health.getInstance().addAlert(sub, mFirmwareLevel, "Talon " + id + " (" + mSide + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else if (mMaster != Master.NO_INFO)
				Health.getInstance().addAlert(sub, mFirmwareLevel, "Talon " + id + " (" + mMaster + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else
				Health.getInstance().addAlert(sub, mFirmwareLevel, "Talon " + id + " firmware is " + firm + ", should be " + FIRMWARE);
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
