package frc.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.util.GZJoystick;

public class DriveTele extends Command {
    

    private GZJoystick joy;
    public DriveTele(GZJoystick aJoy)
    {
        requires(Robot.drive);
        joy = aJoy;
    }

    protected void initialize(){
    }

    protected void execute(){
        Robot.drive.arcade(joy);
    }

    protected boolean isFinished()
    {
        return false;
    }

    protected void end()
    {
    }

    protected void interrupted()
    {
        end();
    }

}