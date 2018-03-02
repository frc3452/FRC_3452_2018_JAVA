package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.subsystems.Climber;

import edu.wpi.first.wpilibj.command.Command;

public class Climb extends Command {
	private double m_speed;

    public Climb(double speed) {
    	requires(Climber.getInstance());
    	
    	m_speed = speed;
    }
    protected void initialize() {
    }
    protected void execute() {
    	Climber.getInstance().Control(m_speed);
    }
    protected boolean isFinished() {
        return false;
    }
    protected void end() {
    	Climber.getInstance().Control(0);
    }
    protected void interrupted() {
    	end();
    }
}
