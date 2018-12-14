package frc.robot.util;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import frc.robot.Constants.kTempSensor;
import frc.robot.subsystems.Health;
import frc.robot.subsystems.Health.AlertLevel;

public class GZSRX extends WPI_TalonSRX {

	public static class Builder {
		private int mDeviceNumber;
		private Breaker mBreaker = Breaker.NO_INFO;
		private String mName = "GZ";

		private GZSubsystem mSub;

		private Side mSide = Side.NO_INFO;
		private Master mMaster = Master.NO_INFO;
		private int mTempSensorPort = -1;

		public Builder(int deviceNumber, GZSubsystem subsystem, String name, Breaker b) {
			this.mDeviceNumber = deviceNumber;
			this.mName = name;
			this.mBreaker = b;
			this.mSub = subsystem;
		}

		public Builder setTempSensorPort(int port) {
			this.mTempSensorPort = port;
			return this;
		}

		public Builder setSide(Side s) {
			this.mSide = s;
			return this;
		}

		public Builder setMaster() {
			if (this.mMaster == Master.NO_INFO)
				this.mMaster = Master.MASTER;
			return this;
		}

		public Builder setFollower() {
			if (this.mMaster == Master.NO_INFO)
				this.mMaster = Master.FOLLOWER;

			return this;
		}

		public GZSRX build() {
			GZSRX g = new GZSRX(this.mDeviceNumber, this.mSub, this.mName, this.mBreaker, this.mSide, this.mMaster,
					this.mTempSensorPort);
			return g;
		}

	}

	private Breaker mBreaker;
	private Side mSide;
	private Master mMaster;
	private String mName;

	public final static int TIMEOUT = 10;
	public final static int FIRMWARE = 778;
	private final static AlertLevel mFirmwareLevel = AlertLevel.WARNING;

	private AnalogInput mTemperatureSensor = null;

	private GZSRX(int deviceNumber, GZSubsystem subsystem, String name, Breaker breaker, Side side, Master master,
			int temperatureSensorPort) {
		super(deviceNumber);

		mBreaker = breaker;
		mSide = side;
		mMaster = master;
		mName = name;

		if (temperatureSensorPort != -1)
			mTemperatureSensor = new AnalogInput(temperatureSensorPort);

		subsystem.mControllers.put(deviceNumber, this);
	}

	public boolean isTemperatureSensorPresent() {
		return this.mTemperatureSensor != null;
	}

	public Double getTemperatureSensor() {
		double retval = -3452;

		if (mTemperatureSensor != null) {
			retval = GZUtil.scaleBetween(this.mTemperatureSensor.getVoltage(), kTempSensor.LOW_TEMP_C,
					kTempSensor.HIGH_TEMP_C, kTempSensor.LOW_VOLT, kTempSensor.HIGH_VOLT);
			retval = GZUtil.celsiusToFahrenheit(retval);
		}

		return retval;
	}

	public String getGZName() {
		return mName;
	}

	public boolean isEncoderValid() {
		return this.getSensorCollection().getPulseWidthRiseToRiseUs() != 0;
	}

	public int getID() {
		return this.getDeviceID();
	}

	public static void logError(ErrorCode errorCode, GZSubsystem subsystem, AlertLevel level, String message) {
		if (errorCode != ErrorCode.OK)
			Health.getInstance().addAlert(subsystem, level, message);
	}

	public void checkFirmware(GZSubsystem sub) {
		int firm = this.getFirmwareVersion();

		if (firm != FIRMWARE) {
			int id = this.getDeviceID();

			if (mSide != Side.NO_INFO)
				Health.getInstance().addAlert(sub, mFirmwareLevel,
						"Talon " + id + " (" + mSide + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else if (mMaster != Master.NO_INFO)
				Health.getInstance().addAlert(sub, mFirmwareLevel,
						"Talon " + id + " (" + mMaster + ")" + " firmware is " + firm + ", should be " + FIRMWARE);
			else
				Health.getInstance().addAlert(sub, mFirmwareLevel,
						"Talon " + id + " firmware is " + firm + ", should be " + FIRMWARE);
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
		LEFT, RIGHT, NO_INFO;
	}

	public enum Master {
		MASTER, FOLLOWER, NO_INFO;
	}

	public enum Breaker {
		AMP_20, AMP_30, AMP_40, NO_INFO
	}

}
