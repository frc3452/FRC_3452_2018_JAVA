package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain2.Values;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Intake extends Subsystem {

	public Spark Intake_L, Intake_R;
	
	public Intake() {
		Intake_L = new Spark(Constants.kIntake.INTAKE_L);
		Intake_R = new Spark(Constants.kIntake.INTAKE_R);

		Intake_L.setInverted(Constants.kIntake.INTAKE_L_INVERT);
		Intake_R.setInverted(Constants.kIntake.INTAKE_R_INVERT);

		Intake_L.setSubsystem("Intake");
		Intake_R.setSubsystem("Intake");

		Intake_L.setName("Intake L");
		Intake_R.setName("Intake R");
	}

	public void manual(double percentage) {
		Intake_L.set(percentage);
		Intake_R.set(percentage);
	}
	

	@Override
	public void initDefaultCommand() {
	}

}
