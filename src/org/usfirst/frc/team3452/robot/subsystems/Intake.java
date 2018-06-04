package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.command.Subsystem;
import org.usfirst.frc.team3452.robot.Constants;

public class Intake extends Subsystem {

	public Spark Intake_L, Intake_R;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
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

	/**
	 * @author max
	 * @param speed
	 * @since
	 */
	public void manual(double speed) {
		Intake_L.set(speed);
		Intake_R.set(speed);
	}

	@Override
	public void initDefaultCommand() {
	}

	
}
