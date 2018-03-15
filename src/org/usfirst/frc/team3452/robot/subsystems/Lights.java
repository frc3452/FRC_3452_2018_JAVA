package org.usfirst.frc.team3452.robot.subsystems;

import org.usfirst.frc.team3452.robot.Robot;

import com.ctre.phoenix.CANifier;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Lights extends Subsystem {
	private static CANifier canifier;

	public int m_hue = 225;
	public double p_h = 0, p_s = 0, p_v = 0;
	private double pulseBrightness = 0;
	private boolean pulseDirection = true;

	public Timer lightTimer = new Timer();

	public NetworkTableEntry centerX;
	public NetworkTableEntry centerY;

	private double tempArray[] = new double[10];

	public void initHardware() {
		for (int i = 0; i < 10; i++)
			tempArray[i] = 3452;

		canifier = new CANifier(Constants.CANIFIER_ID);

		lightTimer.stop();
		lightTimer.reset();
		lightTimer.start();

		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTable table = inst.getTable("/GRIP/vision");

		centerX = table.getEntry("centerX");
		centerY = table.getEntry("centerY");

	}

	public double centerX(int cube) {
		return centerX.getDoubleArray(tempArray)[cube];
	}

	public int visionLength() {
		return centerX.getDoubleArray(tempArray).length;
	}

	public void hsv(double hDegrees, double S, double V) {
		double R, G, B;
		double H = hDegrees;

		if (H < 0) {
			H += 360;
		}
		if (H >= 360) {
			H -= 360;
		}

		if (V <= 0) {
			R = G = B = 0;
		} else if (S <= 0) {
			R = G = B = V;
		} else {
			double hf = H / 60.0;
			int i = (int) Math.floor(hf);
			double f = hf - i;
			double pv = V * (1 - S);
			double qv = V * (1 - S * f);
			double tv = V * (1 - S * (1 - f));
			switch (i) {
			/* Red is dominant color */
			case 0:
				R = V;
				G = tv;
				B = pv;
				break;
			/* Green is dominant color */
			case 1:
				R = qv;
				G = V;
				B = pv;
				break;
			case 2:
				R = pv;
				G = V;
				B = tv;
				break;
			/* Blue is the dominant color */
			case 3:
				R = pv;
				G = qv;
				B = V;
				break;
			case 4:
				R = tv;
				G = pv;
				B = V;
				break;
			/* Red is the dominant color */
			case 5:
				R = V;
				G = pv;
				B = qv;
				break;
			/*
			 * Just in case we overshoot on our math by a little, we put these
			 * here. Since its a switch it won't slow us down at all to put
			 * these here
			 */
			case 6:
				R = V;
				G = tv;
				B = pv;
				break;
			case -1:
				R = V;
				G = pv;
				B = qv;
				break;
			/* The color is not defined, we should throw an error */
			default:
				/* Just pretend its black/white */
				R = G = B = V;
				break;
			}
		}
		p_h = H;
		p_s = S;
		p_v = V;

		rgb((float) R, (float) G, (float) B);
	}

	public void rgb(float r, float g, float b) {
		canifier.setLEDOutput(r, CANifier.LEDChannel.LEDChannelA);
		canifier.setLEDOutput(g, CANifier.LEDChannel.LEDChannelB);
		canifier.setLEDOutput(b, CANifier.LEDChannel.LEDChannelC);
	}

	public void pulse(int h, double s, double low, double high, double pulseIntensity) {
		if (pulseIntensity > high / 15)
			pulseIntensity = high / 15;

		if (pulseDirection) {
			pulseBrightness += pulseIntensity;
		} else {
			pulseBrightness -= pulseIntensity;
		}

		if (pulseBrightness >= high) {
			pulseDirection = false;
		}
		if (pulseBrightness <= low) {
			pulseDirection = true;
		}

		hsv(h, s, pulseBrightness);

		Robot.lights.m_hue = h;
	}

	public String gsm() {
		String f;
		f = edu.wpi.first.wpilibj.DriverStation.getInstance().getGameSpecificMessage();

		if (f.length() > 0)
			return f;
		else
			return "NOT";
	}

	public void initDefaultCommand() {
		//		setDefaultCommand(new LightsCycle());
	}

	public static class Constants {
		public static final int CANIFIER_ID = 0;
	}
}
