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

	public static class kAuton {
		public static final double GYRO_TURN_SPEED = .25;

		public static final int COMMAND_ARRAY_SIZE = 41;

		public static final double CORRECTION = 0.025;

		public static final int SAFTEY_SWITCH = 96;

		public static final int AUTO_SELECTOR_1 = 2;
		public static final int AUTO_SELECTOR_2 = 3;

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

	public static class kCamera {
	}

	public static class kClimber {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;
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
		public final static boolean R_INVERT = false;

		public final static double DIFFERENTIAL_DRIVE_DEADBAND = 0.045;

		public final static int AMP_40_TRIGGER = 60, AMP_40_LIMIT = 30, AMP_40_TIME = 4000;

		public final static int AMP_30_TRIGGER = 45, AMP_30_LIMIT = 25, AMP_30_TIME = 3000;

		public final static double OPEN_LOOP_RAMP_TIME = 0.125;

		public static final double DEMO_DRIVE_MODIFIER = .4;

		public final static double ELEV_TURN_SCALAR = 1.67; //1.67
	}

	public static class kPDP {
		public final static int DRIVE_L_1 = 0, DRIVE_L_2 = 1, DRIVE_L_3 = 5, DRIVE_L_4 = 4;
		public final static int DRIVE_R_1 = 15, DRIVE_R_2 = 14, DRIVE_R_3 = 11, DRIVE_R_4 = 10;

		public final static int ELEVATOR_1 = 12;
		public final static int ELEVATOR_2 = 13;

		public final static int INTAKE_L = 9;
		public final static int INTAKE_R = 8;

		public final static int CLIMBER_1 = 3;
		public final static int CLIMBER_2 = 2;
	}

	public static class kElevator {

		public class PID {
			public static final double F = 0;
			public static final double P = .185;
			public static final double I = 0.000028; //0.000028
			public static final double D = 6;
		}
		
		public class OLDPID {
			public static final double F = 0;
			public static final double P = .2;
			public static final double I = 0.000028; //0.000028
			public static final double D = 2.5;
		}
		
		//CLOSED LOOP
		public static final double CLOSED_DOWN_SPEED_LIMIT = .5;
		public static final double CLOSED_UP_SPEED_LIMIT = 1;
		public static final double CLOSED_COMPLETION = .06;
		
		public static final double OPEN_RAMP_TIME = .5;
		public static final double CLOSED_RAMP_TIME = .2;
		
		public static final int E_1 = 9;
		public static final int E_2 = 10;

		public static final boolean E_1_INVERT = false;
		public static final boolean E_2_INVERT = false;

		public static final boolean ENC_INVERT = true;
		public static final double ENC_TICKS_PER_INCH = -507.0;
		public static final double ENC_HOME_INCHES = 10.4375;
		
		//LIMITING
		public static final double BOTTOM_ROTATION = HeightsInches.Floor;
		public static final double TOP_ROTATION = 9.414;

		public static final double SPEED_LIMIT_SLOWEST_SPEED = .17;
		public static final double SPEED_LIMIT_STARTING_ROTATION = 2.08;
		
		public static final double TOP_HEIGHT_INCHES = (TOP_ROTATION * 4096) / -ENC_TICKS_PER_INCH;

		public static final double LOWER_SOFT_LIMIT_INCHES = 0;
		public static final double UPPER_SOFT_LIMIT_INCHES = TOP_HEIGHT_INCHES / 2;

		public static final boolean USE_DEMO_SOFT_LIMITS = false;


		//TELEOP MODIFIERS
		public static final double JOYSTICK_MODIFIER_UP = 1;
		public static final double JOYSTICK_MODIFIER_DOWN = .6;

		public static final double DEMO_JOYSTICK_MODIFIER_DOWN = .3;
		public static final double DEMO_JOYSTICK_MODIFIER_UP = .5;


		public static final int AMP_TRIGGER = 50;
		public static final int AMP_LIMIT = 40;
		public static final int AMP_TIME = 1000;

		public class HeightsInches {
			public static final double Floor = 0;
			public static final double Switch = 25;
			public static final double Scale_Mid = 52; //53
			public static final double Scale_High = 68.75; //68.75
		}

		public class HeightsRotations {
			public static final double Floor = 0;
			public static final double Switch = 3.3; //3.3 rotations //23.6 inches
			public static final double Scale = 8; //8 rotations //TODO TUNE
		}

	}

	public static class kIntake {
		public static final int INTAKE_L = 0;
		public static final int INTAKE_R = 1;

		public static final boolean INTAKE_L_INVERT = false;
		public static final boolean INTAKE_R_INVERT = true;

		public class Speeds {
			public static final double INTAKE = -.8;
			public static final double SHOOT = .75;
			public static final double SLOW = .3;
			public static final double PLACE = .325; //.225
			public static final double SPIN = .425; //.35
		}

	}

	public class kTempSensor {
		public final static double LOW_TEMP_C = -50;
		public final static double HIGH_TEMP_C = 100;
		public final static double LOW_VOLT = 0;
		public final static double HIGH_VOLT = 1.75;
	}


	public static class kLights {
		public static final int CANIFIER_ID = 0;

		public static final int RED = 0;
		public static final int BLUE = 120;
		public static final int PURPLE = 55;
		public static final int GREEN = 254; // ;)
		public static final int YELLOW = 330;
	}

	public static class kFiles {
		public final static String MP_NAME = "MP1";
		public final static String MP_FOLDER = "MotionProfiles";
		public final static boolean MP_USB = true;

		public final static String STATS_FILE_FOLDER = "GZStats";
		public final static String STATS_FILE_NAME = "Stats";
		public final static boolean STATS_FILE_ON_USB = false;
		public static final double DEFAULT_STATS_RECORD_TIME = .5;

		public final static int RECORDING_MOTION_PROFILE_MS = 30; // 20
		public final static double LOGGING_SPEED = .125;
		public final static String DEFAULT_LOG_VALUE = "N/A";
	}

	public static class kOI {
		public class Rumble {

			public final static double INTAKE = .3;
			public final static double ELEVATOR_SPEED_OVERRIDE_DRIVE = .45;
			public final static double ELEVATOR_SPEED_OVERRIDE_OP = ELEVATOR_SPEED_OVERRIDE_DRIVE - .2;
			public final static double ELEVATOR_LIMIT_OVERRIDE = .45;
			public final static double ENDGAME = .6;

		}
	}
}
