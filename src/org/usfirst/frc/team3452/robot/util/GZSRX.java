package org.usfirst.frc.team3452.robot.util;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class GZSRX extends WPI_TalonSRX {

	private Breaker mBreaker = Breaker.NULL;
	private Side mSide = Side.NULL;
	private boolean mIsMaster = true;

	public GZSRX(int deviceNumber, Breaker breaker, Side side, boolean isMaster) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mIsMaster = isMaster;
	}
	
	public GZSRX(int deviceNumber, Breaker breaker, boolean isMaster)
	{
		super(deviceNumber);
		
		mBreaker = breaker;
		mIsMaster = isMaster;
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

	public boolean isMaster() {
		return mIsMaster;
	}

	public enum Side {
		LEFT, RIGHT, NULL
	}

	public enum Breaker {
		AMP_20, AMP_30, AMP_40, NULL
	}

}
