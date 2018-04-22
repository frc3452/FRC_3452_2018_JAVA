package org.usfirst.frc.team3452.robot.subsystems;

import edu.wpi.cscore.*;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * <h1>Camera subsystem</h1>
 * Handles camera exposure change and server switching
 * @author max
 *
 */
public class Camera extends Subsystem {
	private UsbCamera mCamera0, mCamera1;
	private VideoSink server;

	/**
	 * hardware initialization
	 * 
	 * @author max
	 * @since
	 */
	public void initHardware() {
		mCamera0 = CameraServer.getInstance().startAutomaticCapture(0);
		mCamera0.setResolution(640, 480);
		mCamera0.setFPS(15);
		mCamera0.setExposureManual(40);

//		mCamera1 = CameraServer.getInstance().startAutomaticCapture(1);
//		mCamera1.setResolution(180, 120);
//		mCamera1.setFPS(30);
//		mCamera1.setExposureAuto();

		server = CameraServer.getInstance().getServer();
		server.setSource(mCamera0);
	}

	/**
	 * @author max
	 * @param cameraswitch
	 * @since
	 */
	public void camSwitch(int cameraswitch) {
		switch (cameraswitch) {
		case 0:
			server.setSource(mCamera0);
			break;
		case 1:
			server.setSource(mCamera1);
			break;
		default:
			System.out.println("Invalid camera selection");
			break;
		}
	}

	/**
	 * @author max
	 * @param camera
	 * @param exposure
	 * @since
	 */
	public void camExposure(int camera, int exposure) {
		switch (camera) {
		case 0:
			mCamera0.setExposureManual(exposure);
			break;
		case 1:
			mCamera1.setExposureManual(exposure);
			break;
		default:
			System.out.println("Invalid camera selection");
			break;
		}
	}

	public void initDefaultCommand() {
	}
}
