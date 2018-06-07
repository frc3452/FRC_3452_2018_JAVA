package team3452.robot.commands.elevator;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team3452.robot.Robot;
import team3452.robot.subsystems.Drivetrain;
import team3452.robot.subsystems.Elevator;
import team3452.robot.subsystems.Elevator.ESO;

public class OverrideSet extends InstantCommand {

    private ESO m_override;

    /**
     * @param override
     * @author macco
     * @see Drivetrain
     * @see Elevator
     * @see ESO
     */
    public OverrideSet(ESO override) {
        super();
        m_override = override;
    }

    protected void initialize() {
        switch (m_override) {
            case ON:
                Robot.elevator.m_overriden = true;
                break;
            case OFF:
                Robot.elevator.m_overriden = false;
                break;
            case TOGGLE:
                Robot.elevator.m_overriden = !Robot.elevator.m_overriden;
                break;
        }
    }
}
