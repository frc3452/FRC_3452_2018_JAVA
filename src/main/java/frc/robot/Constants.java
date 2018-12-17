package frc.robot;

import frc.robot.util.GZPID;

/**
 * Robot subsystem constants
 *
 * @author Max
 * @since 5/4/18
 */

public class Constants {
	public static class kLoop {
		public static final double LOOP_SPEED = .02;
		public static final double ENCODER_CHECKER_SPEED = .1;
	}

	public static class kDrivetrain {

		public static class PID {

			static double p = .3; //.1 
			static double d = p * 0; //40
			public static final GZPID Left = new GZPID(0, p, 0, d, .235, 0);
			public static final GZPID Right = new GZPID(0, p, 0, d, .239, 0);

			// public static final GZPID Left = new GZPID(0, 2.7, 0, 2.7 * 25, .235, 0);
			// public static final GZPID Right = new GZPID(0, 2.7, 0, 2.7 * 25, .239, 0);
			
			public static final GZPID OldLeft = new GZPID(0, .425, 0, 4.25, 0, 0);
			public static final GZPID OldRight = new GZPID(0, .8, 0, 4.25, 0, 0);
		}

		public final static double WHEEL_DIAMATER_IN = 6;

		public final static int L1 = 1, L2 = 2, L3 = 3, L4 = 4;
		public final static int R1 = 5, R2 = 6, R3 = 7, R4 = 8;

		public final static boolean L_INVERT = false;
		public final static boolean R_INVERT = true;

		public final static double DIFFERENTIAL_DRIVE_DEADBAND = 0.045;

		public final static int AMP_40_TRIGGER = 60, AMP_40_LIMIT = 30, AMP_40_TIME = 4000;

		public final static int AMP_30_TRIGGER = 30, AMP_30_LIMIT = 30, AMP_30_TIME = 100; //hard limit to 30 amps

		public final static double OPEN_LOOP_RAMP_TIME = .3; //.5

		public static final double DEMO_DRIVE_MODIFIER = .4;

	}

	public static class kPDP {
		public final static int DRIVE_L_1 = 0, DRIVE_L_2 = 1, DRIVE_L_3 = 5, DRIVE_L_4 = 4;
		public final static int DRIVE_R_1 = 15, DRIVE_R_2 = 14, DRIVE_R_3 = 11, DRIVE_R_4 = 10;
	}



	public class kTempSensor {
		public final static double LOW_TEMP_C = -50;
		public final static double HIGH_TEMP_C = 100;
		public final static double LOW_VOLT = 0;
		public final static double HIGH_VOLT = 1.75;
	}


	public static class kFiles {
		public final static String MP_NAME = "MP1";
		public final static String MP_FOLDER = "MotionProfiles";
		public final static boolean MP_USB = true;

		public final static String STATS_FILE_FOLDER = "3452/GZStats";
		public final static String STATS_FILE_NAME = "Stats";
		public final static boolean STATS_FILE_ON_USB = false;
		public static final double DEFAULT_STATS_RECORD_TIME = .5;

		public final static int RECORDING_MOTION_PROFILE_MS = 30; // 20
		public final static double LOGGING_SPEED = .125;
		public final static String DEFAULT_LOG_VALUE = "N/A";
	}

	public static class kOI {
		public class Rumble {

			public final static double ENDGAME = .6;

		}
	}
}
