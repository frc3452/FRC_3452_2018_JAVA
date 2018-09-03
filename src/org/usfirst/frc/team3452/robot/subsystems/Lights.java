package org.usfirst.frc.team3452.robot.subsystems;

import java.util.Arrays;

import org.usfirst.frc.team3452.robot.Constants;
import org.usfirst.frc.team3452.robot.OI;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.Constants.kAuton;
import org.usfirst.frc.team3452.robot.Constants.kLights;
import org.usfirst.frc.team3452.robot.util.GZJoystick.Buttons;
import org.usfirst.frc.team3452.robot.util.GZSubsystem;

import com.ctre.phoenix.CANifier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;

public class Lights extends GZSubsystem {
	private static CANifier canifier;

	private boolean readyForMatch = false;

	public int m_hue = 225;
	private double pulseBrightness = 0;
	private boolean pulseDirection = true;

	private Timer lightTimer = new Timer();

	private NetworkTableEntry centerX;
	private NetworkTableEntry centerY;

	private double tempArray[] = new double[10];

	/**
	 * hardware initialization
	 * 
	 * @author max
	 */
	public Lights() {
		for (int i = 0; i < 10; i++)
			tempArray[i] = 3452;

		canifier = new CANifier(Constants.kLights.CANIFIER_ID);

		lightTimer.stop();
		lightTimer.reset();
		lightTimer.start();

		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("/GRIP/vision");

		centerX = table.getEntry("centerX");
		centerY = table.getEntry("centerY");
	}

	public void loop() {
		if (OI.driverJoy.areButtonsPressed(Arrays.asList(Buttons.A, Buttons.B, Buttons.BACK)))
			readyForMatch = true;

		if (Robot.gzOI.isTele()) {
			
			hsv(kLights.GREEN, 1, .5);
			
		} else if (Robot.gzOI.isAuto()) {
			
			hsv(Robot.gzOI.isRed() ? kLights.RED : kLights.BLUE, 1, .5);
			
		} else if (Robot.gzOI.isDisabled()) {
			
			switch (Robot.auton.uglyAnalog()) {
			case 100:

				// OFF
				Robot.lights.off();

				break;

			case kAuton.SAFTEY_SWITCH:

				// FADE
				Robot.lights.hsv(Robot.lights.m_hue, 1, .25);
				Robot.lights.m_hue++;

				break;
			case 97:

				// POLICE
				if (Robot.lights.m_hue > 180)
					Robot.lights.hsv(kLights.RED, 1, 1);
				else
					Robot.lights.hsv(kLights.BLUE, 1, 1);
				Robot.lights.m_hue += 30;

				break;
			default:

				// IF CONNECTED LOW GREEN
				if (DriverStation.getInstance().isDSAttached()) {

					if (readyForMatch)
						Robot.lights.pulse(kLights.GREEN, 1, 0.1, .4, 0.025 / 3.5);
					else
						Robot.lights.pulse(kLights.YELLOW, 1, 0.1, .4, 0.025 / 3.5);

				} else {
					// IF NOT CONNECTED DO AGGRESSIVE RED PULSE
					Robot.lights.pulse(0, 1, 0.2, .8, 0);
				}
				break;
			}
		}
	}

	public double centerX(int whichCube) {
		if (visionLength() > 0)
			return centerX.getDoubleArray(tempArray)[whichCube];
		else
			return 3452;
	}

	public double centerY(int whichCube) {
		if (visionLength() > 0)
			return centerY.getDoubleArray(tempArray)[whichCube];
		else
			return 3452;
	}

	private int visionLength() {
		return centerX.getDoubleArray(tempArray).length;
	}

	public void off() {
		hsv(0, 0, 0);
	}

	public void hsv(double hDegrees, double saturation, double value) {
		double R, G, B;
		double H = hDegrees;

		if (H < 0) {
			H += 360;
		}
		if (H >= 360) {
			H -= 360;
		}

		if (value <= 0) {
			R = G = B = 0;
		} else if (saturation <= 0) {
			R = G = B = value;
		} else {
			double hf = H / 60.0;
			int i = (int) Math.floor(hf);
			double f = hf - i;
			double pv = value * (1 - saturation);
			double qv = value * (1 - saturation * f);
			double tv = value * (1 - saturation * (1 - f));
			switch (i) {
			/* Red is dominant color */
			case 0:
				R = value;
				G = tv;
				B = pv;
				break;
			/* Green is dominant color */
			case 1:
				R = qv;
				G = value;
				B = pv;
				break;
			case 2:
				R = pv;
				G = value;
				B = tv;
				break;
			/* Blue is the dominant color */
			case 3:
				R = pv;
				G = qv;
				B = value;
				break;
			case 4:
				R = tv;
				G = pv;
				B = value;
				break;
			/* Red is the dominant color */
			case 5:
				R = value;
				G = pv;
				B = qv;
				break;
			/*
			 * Just in case we overshoot on our math by a little, we put these here. Since
			 * its a switch it won't slow us down at all to put these here
			 */
			case 6:
				R = value;
				G = tv;
				B = pv;
				break;
			case -1:
				R = value;
				G = pv;
				B = qv;
				break;
			/* The color is not defined, we should throw an error */
			default:
				/* Just pretend its black/white */
				R = G = B = value;
				break;
			}
		}
		rgb((float) R, (float) G, (float) B);
	}

	private void rgb(float red, float green, float blue) {
		if (m_hue > 360)
			m_hue = 0;

		try {
			canifier.setLEDOutput(red, CANifier.LEDChannel.LEDChannelA);
			canifier.setLEDOutput(green, CANifier.LEDChannel.LEDChannelB);
			canifier.setLEDOutput(blue, CANifier.LEDChannel.LEDChannelC);
		} catch (Exception e) {
		}
	}

	public void pulse(int hue, double saturation, double lowBounePoint, double highBouncePoint, double pulseIntensity) {
		if (pulseIntensity > highBouncePoint / 15)
			pulseIntensity = highBouncePoint / 15;

		if (pulseDirection)
			pulseBrightness += pulseIntensity;
		else
			pulseBrightness -= pulseIntensity;

		if (pulseBrightness >= highBouncePoint)
			pulseDirection = false;

		if (pulseBrightness <= lowBounePoint)
			pulseDirection = true;

		hsv(hue, saturation, pulseBrightness);
		Robot.lights.m_hue = hue;
	}

	public void stop() {
	}

	public String getStateString() {
		return super.getName() + " no state.";
	}

	protected void in() {
	}

	protected void out() {
	}

	protected void initDefaultCommand() {
	}

}
