package org.usfirst.frc.team3452.robot.commands.signal;

import edu.wpi.first.wpilibj.command.InstantCommand;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Camera;

public class CameraSwitch extends InstantCommand {

    private int m_cam;

    /**
     * @author macco
     * @param cam
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
