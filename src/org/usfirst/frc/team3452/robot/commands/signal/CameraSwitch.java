package org.usfirst.frc.team3452.robot.commands.signal;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.command.InstantCommand;

public class CameraSwitch extends InstantCommand {

	private int m_cam;
	
    public CameraSwitch(int cam) {
        super();
        requires(Robot.camera);
    }
    protected void initialize() {
    	Robot.camera.camSwitch(m_cam);
    }

}
