package org.usfirst.frc.team3452.robot.commands.pwm;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class ClimbReleaseControl extends Command {

	private double m_speed, m_timeout;
	
    public ClimbReleaseControl(double speed, double timeout) {
    	requires(Robot.climber);
    	
    	m_speed = speed;
    	m_timeout = timeout;
    }
    protected void initialize() {
    	setTimeout(m_timeout);
    }

    protected void execute() {
    	Robot.climber.release(m_speed);
    }
    protected boolean isFinished() {
        return isTimedOut();
    }
    protected void end() {
    	Robot.climber.release(0);
    }
    protected void interrupted() {
    	end();
    }
}
