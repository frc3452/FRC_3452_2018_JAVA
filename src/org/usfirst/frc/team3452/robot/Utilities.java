package org.usfirst.frc.team3452.robot;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author max
 *
 */
public class Utilities {

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
	 * enum for motion profiling to select which path will be run
	 *
	 * @author max
	 */
	public enum FILES {
		Parse, MotionProfileTest
	}

	/**
	 * <p>
	 * Returns current date in format
	 * </p>
	 * <p>
	 * <b>yyyy.MM.dd.HH.mm.ss</b>
	 * </p>
	 * <p>
	 * or
	 * </p>
	 * <p>
	 * <b>MM.dd.HH.mm</b>
	 * </p>
	 * 
	 * @author max
	 * @param full
	 * @return string
	 * @since
	 */
	public static String dateTime(boolean full) {
		String temp;
		if (full)
			temp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
		else
			temp = new SimpleDateFormat("MM.dd.HH.mm").format(new Date());
		return temp;
	}

}
