package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kClimber;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Climber2 extends GZSubsystem {

	private static Spark climber_1;

	public static ClimberStates mState = ClimberStates.NEUTRAL;

	//Construction
	public Climber2() {
		climber_1 = new Spark(kClimber.CLIMBER_1);
		climber_1.setInverted(kClimber.CLIMBER_1_INVERT);

		climber_1.setSubsystem(Climber2.class.getName());
		climber_1.setName("climber_1");
	}


	@Override
	public synchronized void loop() {
		switch (mState) {

		case MANUAL:
			Values.climber_output = Values.climber_desired_output;
			break;

		case NEUTRAL:
			Values.climber_output = 0;
			break;

		default:
			System.out.println("Incorrect climber state " + mState + " reached.");
			break;
		}
		
		super.inputOutput();
	}

	@Override
	public void stop() {
		mState = ClimberStates.NEUTRAL;
	}
	
	@Override
	protected void in() {
		Values.climber_1_amperage = Robot.drive.pdp.getCurrent(kPDP.CLIMBER_1);
		Values.climber_2_amperage = Robot.drive.pdp.getCurrent(kPDP.CLIMBER_2);
	}

	@Override
	protected void out() {
		climber_1.set(Math.abs(Values.climber_output));
	}

	public enum ClimberStates {
		NEUTRAL, MANUAL
	}

	public static class Values {
		// in
		static double climber_1_amperage;
		static double climber_2_amperage;

		// out
		static double climber_output;
		static double climber_desired_output;
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putNumber(Climber2.class.getName() + " - Climber 1 Amperage (" + kPDP.CLIMBER_1 + ")",
				Values.climber_1_amperage);
		SmartDashboard.putNumber(Climber2.class.getName() + " - Climber 2 Amperage (" + kPDP.CLIMBER_2 + ")",
				Values.climber_2_amperage);
	}

}
