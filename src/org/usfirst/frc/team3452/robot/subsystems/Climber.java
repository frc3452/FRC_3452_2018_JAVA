package org.usfirst.frc.team3452.robot.subsystems;

import java.util.ArrayList;

import org.usfirst.frc.team3452.robot.Constants;

import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <h1>Climber subsystem</h1>
 * Handles climber.
 * 
 * @author max
 *
 */
public class Climber extends Subsystem implements GZSubsystem {
	public Spark Climber;
	public int climbCounter = 0;
	
	private ArrayList<SpeedController> controllers = new ArrayList<SpeedController>();

	/**
	 * one direction control of climber
	 * 
	 * @author max
     * @param speed double
	 */
	public void control(double speed) {
		Climber.set(Math.abs(speed));
	}

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Climber() {
		Climber = new Spark(Constants.kClimber.CLIMBER_1);
		Climber.setInverted(Constants.kClimber.CLIMBER_1_INVERT);

		Climber.setSubsystem("Climber");
		Climber.setName("Climber Motor");
		
		controllers.add(Climber);
	}

	@Override
	public void initDefaultCommand() {
	}

	@Override
	public void setDisable(boolean toSetDisable) {
		for (SpeedController controller : controllers)
			if (toSetDisable)
				controller.disable();
			else
				controller.set(0);
		
	}

}
