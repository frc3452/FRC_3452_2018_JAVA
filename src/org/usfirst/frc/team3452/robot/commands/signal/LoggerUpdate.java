package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.subsystems.AutonSelector;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

import edu.wpi.first.wpilibj.command.Command;

public class LoggerUpdate extends Command {

    public LoggerUpdate() {
    	requires(AutonSelector.getInstance());
    }
    
    
    protected void initialize() {
    	setTimeout(0.15);
    }
    
    protected void execute() {
    	Drivetrain.getInstance().LoggerUpdate();
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
