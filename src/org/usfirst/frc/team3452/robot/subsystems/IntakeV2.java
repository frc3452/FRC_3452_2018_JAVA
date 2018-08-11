package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kIntake;
import org.usfirst.frc.team3452.robot.Constants.kPDP;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class IntakeV2 extends GZSubsystem {

	private static Spark left_intake, right_intake;

	public IntakeStates mState = IntakeStates.NEUTRAL;

	// Construction
	public IntakeV2() {
		left_intake = new Spark(kIntake.INTAKE_L);
		right_intake = new Spark(kIntake.INTAKE_R);

		left_intake.setInverted(kIntake.INTAKE_L_INVERT);
		right_intake.setInverted(kIntake.INTAKE_R_INVERT);

		left_intake.setSubsystem(IntakeV2.class.getName());
		right_intake.setSubsystem(IntakeV2.class.getName());

		left_intake.setName("left_intake");
		right_intake.setName("right_intake");
	}

	public synchronized void loop() {
		switch (mState) {
		case MANUAL:

			Values.left_output = Values.left_desired_output;
			Values.right_output = Values.right_desired_output;

			break;
		case NEUTRAL:

			Values.left_output = 0;
			Values.right_output = 0;

			break;
		default:
			System.out.println("Incorrect intake state " + mState + " reached.");
			break;
		}

		super.inputOutput();
	}

	// READ INPUT VALUES
	public void in() {
		Values.left_amperage = Robot.drive.pdp.getCurrent(kPDP.INTAKE_L);
		Values.right_amperage = Robot.drive.pdp.getCurrent(kPDP.INTAKE_R);
	}

	// WRITE OUTPUT VALUES
	public void out() {
		left_intake.set(Values.left_output);
		right_intake.set(Values.right_output);
	}

	@Override
	public void stop() {
		mState = IntakeStates.NEUTRAL;
	}

	public static class Values {
		// in
		static double left_amperage;
		static double right_amperage;

		// out
		static double left_output;
		static double left_desired_output;

		static double right_output;
		static double right_desired_output;
	}

	public enum IntakeStates {
		NEUTRAL, MANUAL
	}

	@Override
	protected void initDefaultCommand() {
	}

	@Override
	public void outputSmartDashboard() {
		SmartDashboard.putNumber(IntakeV2.class.getName() + " - Left Amperage", Values.left_amperage);
		SmartDashboard.putNumber(IntakeV2.class.getName() + " - Right Amperage", Values.right_amperage);
	}

}
