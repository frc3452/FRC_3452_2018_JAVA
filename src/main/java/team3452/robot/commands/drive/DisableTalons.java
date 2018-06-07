package team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team3452.robot.Robot;

/**
 *
 */
public class DisableTalons extends InstantCommand {

    public DisableTalons() {
        super();
    }

    @Override
    protected void initialize() {
        Robot.drive.setDisable(!Robot.drive.tempPrevDisable);
    }

}
