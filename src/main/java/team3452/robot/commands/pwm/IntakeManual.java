package team3452.robot.commands.pwm;

import edu.wpi.first.wpilibj.command.Command;
import team3452.robot.OI;
import team3452.robot.OI.CONTROLLER;
import team3452.robot.Robot;
import team3452.robot.subsystems.Intake;

public class IntakeManual extends Command {
    private double m_speed;

    /**
     * @param speed
     * @author macco
     * @see Intake
     */
    public IntakeManual(double speed) {
        requires(Robot.intake);

        m_speed = speed;
    }

    @Override
    protected void initialize() {
    }

    @Override
    protected void execute() {
        Robot.intake.manual(m_speed);
        OI.rumble(CONTROLLER.BOTH, .4);
    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void end() {
        Robot.intake.manual(0);
        OI.rumble(CONTROLLER.BOTH, 0);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
