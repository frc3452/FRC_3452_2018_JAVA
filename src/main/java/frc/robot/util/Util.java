package frc.robot.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import frc.robot.motionprofiles.Path;

public class Util {

	public static double limit(double value) {
		if (value > 1.0) {
			return 1.0;
		}
		if (value < -1.0) {
			return -1.0;
		}
		return value;
	}

	public static boolean between(double value, double low, double high) {
		if (value >= low && value <= high)
			return true;

		return false;
	}

	public static double applyDeadband(double value, double deadband) {
		if (Math.abs(value) > deadband) {
			if (value > 0.0) {
				return (value - deadband) / (1.0 - deadband);
			} else {
				return (value + deadband) / (1.0 - deadband);
			}
		} else {
			return 0.0;
		}
	}

	/**
	 * toRound = 2.342, wanting to round to nearest .05 1/<b>20</b> is .05
	 * roundToFraction(2.342,20)
	 *
	 * @author max
	 * @param value
	 * @param denominator double
	 * @return double
	 */
	public static double roundToFraction(double value, double denominator) {
		return Math.round(value * denominator) / denominator;
	}

	/**
	 * <p>
	 * Returns current date in format
	 * </p>
	 * <p>
	 * <b>MM.dd.HH.mm.ss.SSS</b>
	 * </p>
	 * <p>
	 * or
	 * </p>
	 * <p>
	 * <b>yyyy.MM.dd.HH.mm</b>
	 * </p>
	 * 
	 * @author max
	 * @since
	 */
	public static String dateTime(boolean precision) {
		String temp;
		if (precision)
			temp = new SimpleDateFormat("MM.dd.HH.mm.ss.SSS").format(new Date());
		else
			temp = new SimpleDateFormat("yyyy.MM.dd.HH.mm").format(new Date());
		return temp;
	}

	public static class Parse implements Path {

		@Override
		public double[][] mpL() {
			double[][] mpL = { { 3452, 3452 }, { 3452, 3452 }, };
			return mpL;
		}

		@Override
		public double[][] mpR() {
			double[][] mpR = { { 3452, 3452 }, { 3452, 3452 }, };
			return mpR;
		}

		@Override
		public Integer mpDur() {
			return 3452;
		}
	}

}
