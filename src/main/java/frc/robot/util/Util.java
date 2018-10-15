package frc.robot.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import frc.robot.motionprofiles.Path;

public class Util {

	private Util() {
	}

	int scaleBetween(int unscaledNum, int minAllowed, int maxAllowed, int min, int max) {
		return (maxAllowed - minAllowed) * (unscaledNum - min) / (max - min) + minAllowed;
	}

	double scaleBetween(double unscaledNum, double minAllowed, double maxAllowed, double min, double max) {
		return (maxAllowed - minAllowed) * (unscaledNum - min) / (max - min) + minAllowed;
	}

	/**
	 * Thread.currentThread().getStackTrace();
	 */
	public static String trace(StackTraceElement e[]) {

		String retval = "";
		try {
			for (int i = e.length - 5; i > 0; i--) {
				retval += e[i].getMethodName();

				if (i != 1)
					retval += ".";
			}
		} catch (Exception ex) {
			System.out.println(
					"Max was a dummy that tried to write something to make his life easier but he made it much much harder");
			// ex.printStackTrace();
		}

		return retval;
	}

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

	public static boolean epsilonEquals(double value, double epislonPoint, double epsilon) {
		return (value - epsilon <= epislonPoint) && (value + epsilon >= epislonPoint);
	}

	public static boolean allCloseTo(final ArrayList<Double> list, double value, double epsilon) {
		boolean result = true;
		for (Double value_in : list) {
			result &= epsilonEquals(value_in, value, epsilon);
		}
		return result;
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
