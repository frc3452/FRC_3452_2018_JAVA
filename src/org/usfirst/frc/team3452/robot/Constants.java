package org.usfirst.frc.team3452.robot;

/**
 * Robot subsystem constants
 * 
 * @since 5/4/18
 * @author Max
 */

public class Constants {
	public class AutonSelector {
		public static final int AUTO_SELECTOR_1 = 2; //1
		public static final int AUTO_SELECTOR_2 = 3; //0

		public static final int AUTO_V = 15;

		//COMP
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

		public static final int AUTO_1_L = AUTO_1 - AUTO_V;
		public static final int AUTO_1_H = AUTO_1 + AUTO_V;

		public static final int AUTO_2_L = AUTO_2 - AUTO_V;
		public static final int AUTO_2_H = AUTO_2 + AUTO_V;

		public static final int AUTO_3_L = AUTO_3 - AUTO_V;
		public static final int AUTO_3_H = AUTO_3 + AUTO_V;

		public static final int AUTO_4_L = AUTO_4 - AUTO_V;
		public static final int AUTO_4_H = AUTO_4 + AUTO_V;

		public static final int AUTO_5_L = AUTO_5 - AUTO_V;
		public static final int AUTO_5_H = AUTO_5 + AUTO_V;

		public static final int AUTO_6_L = AUTO_6 - AUTO_V;
		public static final int AUTO_6_H = AUTO_6 + AUTO_V;

		public static final int AUTO_7_L = AUTO_7 - AUTO_V;
		public static final int AUTO_7_H = AUTO_7 + AUTO_V;

		public static final int AUTO_8_L = AUTO_8 - AUTO_V;
		public static final int AUTO_8_H = AUTO_8 + AUTO_V;

		public static final int AUTO_9_L = AUTO_9 - AUTO_V;
		public static final int AUTO_9_H = AUTO_9 + AUTO_V;

		public static final int AUTO_10_L = AUTO_10 - AUTO_V;
		public static final int AUTO_10_H = AUTO_10 + AUTO_V;
	}

	public class Camera {
	}

	public class Climber {
		public static final int CLIMBER_1 = 2;
		public static final boolean CLIMBER_1_INVERT = false;
	}

	public class Drivetrain {
		public final static int L_1 = 1, L_2 = 2, L_3 = 3, L_4 = 4;
		public final static int R_1 = 5, R_2 = 6, R_3 = 7, R_4 = 8;

		public final static boolean L_INVERT = false;
		public final static boolean R_INVERT = false;

		public final static int AMP_40_TRIGGER = 60, AMP_40_LIMIT = 30, AMP_40_TIME = 4000;

		public final static int AMP_30_TRIGGER = 45, AMP_30_LIMIT = 25, AMP_30_TIME = 3000;

		public final static double RAMP_TIME = 0.125;
	}
	
	public class PDP {
		public final static int DRIVE_L_1 = 0, DRIVE_L_2 = 1, DRIVE_L_3 = 5, DRIVE_L_4 = 4;
		public final static int DRIVE_R_1 = 15, DRIVE_R_2 = 14, DRIVE_R_3 = 11, DRIVE_R_4 = 10;

		public final static int ELEVATOR_1 = 12;
		public final static int ELEVATOR_2 = 13;

		public final static int INTAKE_L = 9;
		public final static int INTAKE_R = 8;

		public final static int CLIMBER_1 = 3;
		public final static int CLIMBER_2 = 2;
	}

	public class Elevator {
		public static final int E_1 = 9;
		public static final int E_2 = 10;

		public static final boolean E_1_INVERT = false;
		public static final boolean E_2_INVERT = false;

		public static final boolean E_ENC_INVERT = true;

		public static final double E_OPEN_RAMP_TIME = .5;
		public static final double E_CLOSED_RAMP_TIME = .25;

		public static final double SPEED_1 = 1;
		public static final double SPEED_2 = .9;
		public static final double SPEED_3 = .65;
		public static final double SPEED_4 = .55;
		public static final double SPEED_5 = .48; //.45

		public static final int AMP_TRIGGER = 50;
		public static final int AMP_LIMIT = 40;
		public static final int AMP_TIME = 1000;
	}

	public class Intake {
		public static final int INTAKE_L = 0;
		public static final int INTAKE_R = 1;

		public static final boolean INTAKE_L_INVERT = false;
		public static final boolean INTAKE_R_INVERT = true;
	}

	public class Lights {
		public static final int CANIFIER_ID = 0;
	}

	public class Playback {
	}

}
