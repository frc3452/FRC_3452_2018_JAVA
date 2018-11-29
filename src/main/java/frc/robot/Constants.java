package frc.robot;

/**
 * Robot subsystem constants
 *
 * @author Max
 * @since 5/4/18
 */

public class Constants {
	public class kLoop {
		public static final double LOOP_SPEED = .02;
		public static final double ENCODER_CHECKER_SPEED = .1;
	}

	public class kAuton {
		public static final double GYRO_TURN_SPEED = .25;

		public static final int COMMAND_ARRAY_SIZE = 41;

		public static final double CORRECTION = 0.025;

		public static final int SAFTEY_SWITCH = 96;

		public static final int AUTO_SELECTOR_1 = 1;
		public static final int AUTO_SELECTOR_2 = 2;

		public static final int AUTO_VARIANCE = 15;

		// COMP
		public static final int AUTO_1 = 2683;
		public static final int AUTO_2 = 2992;
		public static final int AUTO_3 = 3185;
		public static final int AUTO_4 = 3321;
		public static final int AUTO_5 = 3427;
		public static final int AUTO_6 = 3507;
		public static final int AUTO_7 = 3565;
		public static final int AUTO_8 = 3658;
		public static final int AUTO_9 = 3721;
		public static final int AUTO_10 = 3781;

		public static final int AUTO_1_L = AUTO_1 - AUTO_VARIANCE;
		public static final int AUTO_1_H = AUTO_1 + AUTO_VARIANCE;

		public static final int AUTO_2_L = AUTO_2 - AUTO_VARIANCE;
		public static final int AUTO_2_H = AUTO_2 + AUTO_VARIANCE;

		public static final int AUTO_3_L = AUTO_3 - AUTO_VARIANCE;
		public static final int AUTO_3_H = AUTO_3 + AUTO_VARIANCE;

		public static final int AUTO_4_L = AUTO_4 - AUTO_VARIANCE;
		public static final int AUTO_4_H = AUTO_4 + AUTO_VARIANCE;

		public static final int AUTO_5_L = AUTO_5 - AUTO_VARIANCE;
		public static final int AUTO_5_H = AUTO_5 + AUTO_VARIANCE;

		public static final int AUTO_6_L = AUTO_6 - AUTO_VARIANCE;
		public static final int AUTO_6_H = AUTO_6 + AUTO_VARIANCE;

		public static final int AUTO_7_L = AUTO_7 - AUTO_VARIANCE;
		public static final int AUTO_7_H = AUTO_7 + AUTO_VARIANCE;

		public static final int AUTO_8_L = AUTO_8 - AUTO_VARIANCE;
		public static final int AUTO_8_H = AUTO_8 + AUTO_VARIANCE;

		public static final int AUTO_9_L = AUTO_9 - AUTO_VARIANCE;
		public static final int AUTO_9_H = AUTO_9 + AUTO_VARIANCE;

		public static final int AUTO_10_L = AUTO_10 - AUTO_VARIANCE;
		public static final int AUTO_10_H = AUTO_10 + AUTO_VARIANCE;

		public static final String DEFAULT_NAME = "NO COMMAND";
	}

	public class kDrivetrain {

		public class PID {
			public class LEFT {
				public final static double P = .425;
				public final static double I = 0;
				public final static double D = 4.25;
				public static final double F = 0;
			}

			public class RIGHT {
				public final static double P = .8;
				public final static double I = 0;
				public final static double D = 4.25;
				public static final double F = 0;
			}
		}

		public final static int L1 = 1, L2 = 2, L3 = 3, L4 = 4;
		public final static int R1 = 5, R2 = 6, R3 = 7, R4 = 8;

		public final static boolean L_INVERT = false;
		public final static boolean R_INVERT = true;
		
		public final static double DIFFERENTIAL_DRIVE_DEADBAND = 0.045;

		public final static int AMP_40_TRIGGER = 60, AMP_40_LIMIT = 30, AMP_40_TIME = 4000;

		public final static int AMP_30_TRIGGER = 45, AMP_30_LIMIT = 25, AMP_30_TIME = 3000;

		public final static double OPEN_LOOP_RAMP_TIME = 0.125;

		public static final double DEMO_DRIVE_MODIFIER = .4;

		public final static double ELEV_TURN_SCALAR = 1.65;
	}

	public class kPDP {
		public final static int DRIVE_L_1 = 0, DRIVE_L_2 = 1, DRIVE_L_3 = 5, DRIVE_L_4 = 4;
		public final static int DRIVE_R_1 = 15, DRIVE_R_2 = 14, DRIVE_R_3 = 11, DRIVE_R_4 = 10;
	}

	public class kFileManagement {
		public final static int RECORDING_MOTION_PROFILE_MS = 100; // 20
		public final static double LOGGING_SPEED = .125;
		public final static String DEFAULT_LOG_VALUE = "N/A";
	}

	public class kTempSensor {
		public final static double LOW_TEMP_C = -50;
		public final static double HIGH_TEMP_C = 100;
		public final static double LOW_VOLT = 0;
		public final static double HIGH_VOLT = 1.75;

		public final static int TEMP_SENSOR_1 = 3;
		public final static int TEMP_SENSOR_2 = 5;
		public final static int TEMP_SENSOR_3 = 6;
		public final static int TEMP_SENSOR_4 = 7;
	}

	public class kOI {
		public class Rumble {
			public final static double ENDGAME = .6;
		}
	}
}
