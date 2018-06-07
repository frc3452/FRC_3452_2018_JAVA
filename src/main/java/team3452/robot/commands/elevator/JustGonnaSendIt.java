package team3452.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;
import team3452.robot.commands.pwm.IntakeTime;

public class JustGonnaSendIt extends CommandGroup {

    /**
     * <b>Just Gonna Send it</b>
     */
    public JustGonnaSendIt() {
        addSequential(new ElevatorTime(1, 10));
        addSequential(new IntakeTime(1, .5));
    }
}
