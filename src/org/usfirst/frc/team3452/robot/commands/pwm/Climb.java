package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double m_speed;

    public Climb(double speed) {
    	requires(Robot.climber);
    	
    	m_speed = speed;
    }
    protected void initialize() {
    }
    protected void execute() {
    	Robot.climber.Control(m_speed);
    }
    protected boolean isFinished() {
        return false;
    }
    protected void end() {
    	Robot.climber.Control(0);
    }
    protected void interrupted() {
    	end();
    }
}
