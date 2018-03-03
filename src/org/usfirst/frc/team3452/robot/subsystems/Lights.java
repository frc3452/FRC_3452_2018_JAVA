package org.usfirst.frc.team3452.robot.subsystems;

import com.ctre.phoenix.CANifier;
import com.ctre.phoenix.CANifier.LEDChannel;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Lights extends Subsystem {

	private static CANifier canifier;

	public int m_hue;

	private static Lights instance = new Lights();

	public void initHardware() {
		// canifier = new CANifier(Constants.CANIFIER_ID);
		m_hue = 0;
	}

	public void hsv(double hDegrees, double S, double V) {
		float RGB[] = null;

		double R, G, B;
		double H = hDegrees;

		if (H < 0) {
			H += 360;
		}
		;
		if (H >= 360) {
			H -= 360;
		}
		;

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
		/* Since we can't pass by reference, return an array */
		/*
		 * * RGB[0] = (float) R; RGB[1] = (float) G; RGB[2] = (float) B; return
		 * RGB;
		 */
		rgb((float) RGB[0], (float) RGB[1], (float) RGB[2]);

	}

	public void rgb(float r, float g, float b) {
		canifier.setLEDOutput(r, CANifier.LEDChannel.LEDChannelA);
		canifier.setLEDOutput(g, CANifier.LEDChannel.LEDChannelB);
		canifier.setLEDOutput(b, CANifier.LEDChannel.LEDChannelC);
	}

	public void brightness(double brightness) {
		canifier.setLEDOutput(brightness, LEDChannel.LEDChannelA);
		canifier.setLEDOutput(brightness, LEDChannel.LEDChannelB);
		canifier.setLEDOutput(brightness, LEDChannel.LEDChannelC);
	}

	public String gsm() {
		if (edu.wpi.first.wpilibj.DriverStation.getInstance().getGameSpecificMessage().length() > 0) {
			return edu.wpi.first.wpilibj.DriverStation.getInstance().getGameSpecificMessage();
		} else {
			return "NO";
		}
	}

	public void initDefaultCommand() {
		// setDefaultCommand(new LightsCycle());
	}

	public static Lights getInstance() {
		return instance;
	}

	public static class Constants {
		public static final int CANIFIER_ID = 20;
	}
}
