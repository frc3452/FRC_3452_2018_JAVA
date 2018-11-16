package frc.robot;

/**
 * Robot subsystem constants
 *
 * @author Max
 * @since 5/4/18
 */

public class Constants {

	public class kDrivetrain {
		public final static int FIRMWARE_VERSION = 0;

		public final static int L1 = 1, L2 = 2, L3 = 3, L4 = 4;
		public final static int R1 = 5, R2 = 6, R3 = 7, R4 = 8;

		public final static boolean L_INVERT = false;
		public final static boolean R_INVERT = true;
		public final static int AMP_40_TRIGGER = 60, AMP_40_LIMIT = 30, AMP_40_TIME = 4000;

		public final static int AMP_30_TRIGGER = 45, AMP_30_LIMIT = 25, AMP_30_TIME = 3000;

		public final static double OPEN_LOOP_RAMP_TIME = 0.125;
		
		public class PID {
			public class LEFT {
				public final static double P = .425;
				public final static double I = 0;
				public final static double D = 4.25;
				public static final double F = 0;
			}

			public class RIGHT {
				public final static double P = .425;
				public final static double I = 0;
				public final static double D = 4.25;
				public static final double F = 0;
			}
		}
	}

	public class kPDP {
		public final static int DRIVE_L_1 = 0, DRIVE_L_2 = 1;
		public final static int DRIVE_R_1 = 15, DRIVE_R_2 = 14;
	}

	public class kFileManagement {
		public final static int RECORDING_MOTION_PROFILE_MS = 100; // 20
		public final static double LOGGING_SPEED = .125;
		public final static String DEFAULT_LOG_VALUE = "N/A";
	}

	public class kOI {
		public class Rumble {
			public final static double ENDGAME = .6;
		}
	}

}
