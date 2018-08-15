package org.usfirst.frc.team3452.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class GZSRX extends WPI_TalonSRX {

	private Breaker mBreaker = Breaker.NO_INFO;
	private Side mSide = Side.NO_INFO;
	private Master mMaster = Master.NO_INFO;

	public GZSRX(int deviceNumber, Breaker breaker, Side side, Master master) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mMaster = master;
	}
	
	public GZSRX(int deviceNumber, Breaker breaker, Master master)
	{
		super(deviceNumber);
		
		mBreaker = breaker;
		mMaster = master;
	}
	
	public GZSRX(int deviceNumber, Breaker breaker)
	{
		super(deviceNumber);
		
		mBreaker = breaker;
	}
	

	public Breaker getBreakerSize() {
		return mBreaker;
	}

	public Side getSide() {
		return mSide;
	}

	public Master isMaster() {
		return mMaster;
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
