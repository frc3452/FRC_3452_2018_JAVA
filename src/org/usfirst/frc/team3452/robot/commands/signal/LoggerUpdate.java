package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class LoggerUpdate extends Command {

    public LoggerUpdate() {
    	requires(Robot.autonSelector);
    }
    
    
    protected void initialize() {
    	setTimeout(0.15);
    }
    
    protected void execute() {
    	Robot.drive.LoggerUpdate();
    }

    protected boolean isFinished() {
        return isTimedOut();
    }

    protected void end() {
    }
    protected void interrupted() {
    	end();
    }
}
