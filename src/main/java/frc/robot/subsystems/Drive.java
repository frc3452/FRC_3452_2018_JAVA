package frc.robot.subsystems;

import java.util.Arrays;
import java.util.List;

import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Constants.kDrivetrain;
import frc.robot.OI;
import frc.robot.commands.drive.DriveTele;
import frc.robot.subsystems.Health.AlertLevel;
import frc.robot.util.GZJoystick;
import frc.robot.util.GZSRX;
import frc.robot.util.GZSRX.Breaker;
import frc.robot.util.GZSRX.Master;
import frc.robot.util.GZSRX.Side;
import frc.robot.util.GZSubsystem;

public class Drive extends GZSubsystem {

	public IO mIO = new IO();

	// PDP
	private PowerDistributionPanel pdp = new PowerDistributionPanel(0);

	// DRIVETRAIN
	private GZSRX L1, L2, L3, L4, R1, R2, R3, R4;
	private DifferentialDrive mDrive;
	private List<GZSRX> controllers;

	private double mModifyPercent = 1;
	private boolean mIsSlow = false;

	public Drive() {
	}

	public synchronized void construct() {
		L1 = new GZSRX(kDrivetrain.L1, Breaker.AMP_40, Side.LEFT, Master.MASTER);
		L2 = new GZSRX(kDrivetrain.L2, Breaker.AMP_40, Side.LEFT, Master.FOLLOWER);
		L3 = new GZSRX(kDrivetrain.L3, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);
		L4 = new GZSRX(kDrivetrain.L4, Breaker.AMP_30, Side.LEFT, Master.FOLLOWER);

		R1 = new GZSRX(kDrivetrain.R1, Breaker.AMP_40, Side.RIGHT, Master.MASTER);
		R2 = new GZSRX(kDrivetrain.R2, Breaker.AMP_40, Side.RIGHT, Master.FOLLOWER);
		R3 = new GZSRX(kDrivetrain.R3, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);
		R4 = new GZSRX(kDrivetrain.R4, Breaker.AMP_30, Side.RIGHT, Master.FOLLOWER);

		mDrive = new DifferentialDrive(L1, R1);

		controllers = Arrays.asList(L1, L2, L3, L4, R1, R2, R3, R4);

		brake(NeutralMode.Coast);

		talonInit();
		enableFollower();

		pdp.setSubsystem("Drive train");

		L1.setName("L1");
		L2.setName("L2");
		L3.setName("L3");
		L4.setName("L4");
		R1.setName("R1");
		R2.setName("R2");
		R3.setName("R3");
		R4.setName("R4");
	}

	private void talonInit() {
		for (GZSRX s : controllers) {
			String name = s.getSide() + " (" + s.getDeviceID() + ")";

			s.checkFirmware(this);

			GZSRX.logError(s.configFactoryDefault(GZSRX.TIMEOUT), this, AlertLevel.ERROR,
					"Could not factory reset Talon " + name);

			s.setInverted((s.getSide() == Side.LEFT) ? kDrivetrain.L_INVERT : kDrivetrain.R_INVERT);

			// CURRENT LIMIT
			GZSRX.logError(s.configContinuousCurrentLimit(
					s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_LIMIT : kDrivetrain.AMP_30_LIMIT,
					GZSRX.TIMEOUT), this, AlertLevel.WARNING, "Could not set current limit for Talon " + name);

			GZSRX.logError(
					s.configPeakCurrentLimit(s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_TRIGGER
							: kDrivetrain.AMP_30_TRIGGER, GZSRX.TIMEOUT),
					this, AlertLevel.WARNING, "Could not set current limit trigger for Talon " + name);

			GZSRX.logError(
					s.configPeakCurrentDuration(
							s.getBreakerSize() == Breaker.AMP_40 ? kDrivetrain.AMP_40_TIME : kDrivetrain.AMP_30_TIME,
							GZSRX.TIMEOUT),
					this, AlertLevel.WARNING, "Could not set current limit time for Talon " + name);

			s.enableCurrentLimit(true);

			GZSRX.logError(s.configOpenloopRamp(kDrivetrain.OPEN_LOOP_RAMP_TIME, GZSRX.TIMEOUT), this,
					AlertLevel.WARNING, "Could not set open loop ramp time for Talon " + name);

			GZSRX.logError(s.configNeutralDeadband(0.05, GZSRX.TIMEOUT), this, AlertLevel.WARNING,
					"Could not set Neutral Deadband for Talon " + name);

			s.setSubsystem("Drive train");
		}
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new DriveTele(OI.driverJoy));
	}

	@Override
	public synchronized void loop() {
		in();
		mModifyPercent = (mIsSlow ? .65 : 1);
	}

	public static class IO {
		public Double L1_amp = Double.NaN, L2_amp = Double.NaN, L3_amp = Double.NaN, L4_amp = Double.NaN,
				R1_amp = Double.NaN, R2_amp = Double.NaN, R3_amp = Double.NaN, R4_amp = Double.NaN;

		public Double L1_volt = Double.NaN, L2_volt = Double.NaN, L3_volt = Double.NaN, L4_volt = Double.NaN,
				R1_volt = Double.NaN, R2_volt = Double.NaN, R3_volt = Double.NaN, R4_volt = Double.NaN;
	}

	@Override
	protected synchronized void in() {
		mIO.L1_amp = L1.getOutputCurrent();
		mIO.L2_amp = L2.getOutputCurrent();
		mIO.L3_amp = L3.getOutputCurrent();
		mIO.L4_amp = L4.getOutputCurrent();
		mIO.R1_amp = R1.getOutputCurrent();
		mIO.R2_amp = R2.getOutputCurrent();
		mIO.R3_amp = R3.getOutputCurrent();
		mIO.R4_amp = R4.getOutputCurrent();

		mIO.L1_volt = L1.getMotorOutputVoltage();
		mIO.L2_volt = L2.getMotorOutputVoltage();
		mIO.L3_volt = L3.getMotorOutputVoltage();
		mIO.L4_volt = L4.getMotorOutputVoltage();
		mIO.R1_volt = R1.getMotorOutputVoltage();
		mIO.R2_volt = R2.getMotorOutputVoltage();
		mIO.R3_volt = R3.getMotorOutputVoltage();
		mIO.R4_volt = R4.getMotorOutputVoltage();
	}


	public synchronized void arcade(GZJoystick joy) {
		arcade(joy.getLeftAnalogY(), (joy.getRightTrigger() - joy.getLeftTrigger()) * .8);
	}

	// called in DEMO state
	public synchronized void alternateArcade(GZJoystick joy) {
		arcade(joy.getLeftAnalogY(), (joy.getRightAnalogX() * .85));
	}

	public synchronized void arcade(double move, double rotate) {
		mDrive.arcadeDrive(move * mModifyPercent, rotate * mModifyPercent);
	}

	public synchronized void tank(double left, double right) {
		mDrive.tankDrive(left * mModifyPercent, right * mModifyPercent);
	}

	public synchronized void tank(GZJoystick joy) {
		tank(joy.getLeftAnalogY(), joy.getRightAnalogY());
	}

	private synchronized void brake(NeutralMode mode) {
		controllers.forEach((s) -> s.setNeutralMode(mode));
	}

	public synchronized void enableFollower() {
		for (GZSRX c : controllers) {
			switch (c.getSide()) {
			case LEFT:
				c.follow(L1);
				break;
			case RIGHT:
				c.follow(R1);
				break;
			case NO_INFO:
				System.out.println("ERROR Drive talon " + c.getName() + " could not enter follower mode!");
			}
		}
	}

	public synchronized Double getPDPChannelCurrent(int channel) {
		return pdp.getCurrent(channel);
	}

	public synchronized Double getPDPTemperature() {
		return pdp.getTemperature();
	}

	public synchronized Double getPDPTotalCurrent() {
		return pdp.getTotalCurrent();
	}

	public synchronized Double getPDPVoltage() {
		return pdp.getVoltage();
	}

	public synchronized void slowSpeed(boolean isSlow)
	{
		mIsSlow = isSlow;
	}
	public Boolean isSlow()
	{
		return mIsSlow;
	}

	public String getStateString(){
		return "NA";
	}

	public void out()
	{
	}

	public void stop()
	{}
}
