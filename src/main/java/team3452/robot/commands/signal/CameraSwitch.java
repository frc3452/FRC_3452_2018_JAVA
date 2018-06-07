package team3452.robot.commands.signal;

import edu.wpi.first.wpilibj.command.InstantCommand;
import team3452.robot.Robot;
import team3452.robot.subsystems.Camera;

public class CameraSwitch extends InstantCommand {

    private int m_cam;

    /**
     * @param cam
     * @author macco
     * @see Camera
     */
    public CameraSwitch(int cam) {
        super();
        requires(Robot.camera);
    }

    protected void initialize() {
        Robot.camera.camSwitch(m_cam);
    }

}
