package org.usfirst.frc.team3452.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class GZSRX extends WPI_TalonSRX {

	private Breaker mBreaker = Breaker.NO_INFO;
	private Side mSide = Side.NO_INFO;
	private Master mMaster = Master.NO_INFO;
	private boolean mIsInverted;

	public final static int TIMEOUT = 10;

	// Drivetrain
	public GZSRX(int deviceNumber, Breaker breaker, Side side, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mMaster = master;
	}

	// Other subsystem
	public GZSRX(int deviceNumber, Breaker breaker, boolean isInverted, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mMaster = master;
		mIsInverted = isInverted;
	}

	// Basic
	public GZSRX(int deviceNumber, Breaker breaker) {
		super(deviceNumber);

		mBreaker = breaker;
	}

	//TODO 1C) DELETE?
 	public static void checkError(ErrorCode errorCode, String message) {
		if (errorCode != ErrorCode.OK)
			System.out.println("ERROR COULD NOT SET " + message + ":\t\t" + errorCode);
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

	public boolean isInverted() {
		return mIsInverted;
	}

	public enum Side {
		LEFT, RIGHT, NO_INFO
	}

	public enum Breaker {
		AMP_20, AMP_30, AMP_40, NO_INFO
	}

	public enum Master {
		MASTER, FOLLOWER, NO_INFO
	}

}
