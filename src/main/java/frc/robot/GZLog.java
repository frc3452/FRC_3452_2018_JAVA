package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.util.Util;

public class GZLog {

	ArrayList<LogItem> values = new ArrayList<>();

	public GZLog() {
	}

	@SuppressWarnings("unused")
	public void fillLogger() {

		LogItem left_speed = new LogItem("L-RPM") {
			@Override
			public void update() {
				this.mValue = Robot.drive.getLeftVel().toString();
			}
		};

		LogItem left_encoder_valid = new LogItem("L-ENC-PRSNT") {
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.leftEncoderValid.toString();
			}
		};

		LogItem right_speed = new LogItem("R-RPM") {
			@Override
			public void update() {
				this.mValue = Robot.drive.getRightVel().toString();
			}
		};

		LogItem right_encoder_valid = new LogItem("R-ENC-PRSNT") {
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.rightEncoderValid.toString();
			}
		};

		LogItem l1_amp = new LogItem("L1-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L1_amp.toString();
			}
		};

		LogItem l1_amp_avg = new LogItem("L1-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem l2_amp = new LogItem("L2-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L2_amp.toString();
			}
		};

		LogItem l2_amp_avg = new LogItem("L2-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem l3_amp = new LogItem("L3-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L3_amp.toString();
			}
		};

		LogItem l3_amp_avg = new LogItem("L3-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem l4_amp = new LogItem("L4-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L4_amp.toString();
			}
		};

		LogItem l4_amp_avg = new LogItem("L4-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem r1_amp = new LogItem("R1-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R1_amp.toString();
			}
		};

		LogItem r1_amp_avg = new LogItem("R1-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem r2_amp = new LogItem("R2-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R2_amp.toString();
			}
		};

		LogItem r2_amp_avg = new LogItem("R2-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem r3_amp = new LogItem("R3-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R3_amp.toString();
			}
		};

		LogItem r3_amp_avg = new LogItem("R3-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem r4_amp = new LogItem("R4-AMP") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R4_amp.toString();
			}
		};

		LogItem r4_amp_avg = new LogItem("R4-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem l1_volt = new LogItem("L1-VOLT") {
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L1_volt.toString();
			}
		};

		LogItem l2_volt = new LogItem("L2-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L2_volt.toString();
			}
		};

		LogItem l3_volt = new LogItem("L3-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L3_volt.toString();
			}
		};

		LogItem l4_volt = new LogItem("L4-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L4_volt.toString();
			}
		};

		LogItem r1_volt = new LogItem("R1-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R1_volt.toString();
			}
		};

		LogItem r2_volt = new LogItem("R2-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R2_volt.toString();
			}
		};

		LogItem r3_volt = new LogItem("R3-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R3_volt.toString();
			}
		};

		LogItem r4_volt = new LogItem("R4-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R4_volt.toString();
			}
		};

		LogItem elev_1_amp = new LogItem("ELEV-1-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.mIO.elevator_1_amp.toString();
			}
		};

		LogItem elev_1_amp_avg = new LogItem("ELEV-1-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem elev_2_amp = new LogItem("ELEV-2-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.mIO.elevator_2_amp.toString();
			}
		};

		LogItem elev_2_amp_avg = new LogItem("ELEV-2-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem elev_1_volt = new LogItem("ELEV-1-VOLT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.mIO.elevator_1_volt.toString();
			}
		};

		LogItem elev_2_volt = new LogItem("ELEV-2-VOLT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.mIO.elevator_2_volt.toString();
			}
		};

		LogItem elev_up_limit = new LogItem("ELEV-UP-LMT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getTopLimit().toString();
			}
		};

		LogItem elev_down_limit = new LogItem("ELEV-DOWN-LMT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getBottomLimit().toString();
			}
		};

		LogItem elev_encoder_present = new LogItem("ELEV-ENC-PRSNT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.mIO.encoderValid.toString();
			}
		};

		LogItem elev_rotations = new LogItem("ELEV-ROT") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getRotations().toString();
			}
		};

		LogItem elev_inches = new LogItem("ELEV-INCHES") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getHeight().toString();
			}

		};
		LogItem elev_speed = new LogItem("ELEV-SPEED") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getSpeed().toString();
			}
		};

		LogItem climber_1_amp = new LogItem("CLIMBER-1-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.climber.mIO.climber_1_amperage.toString();
			}
		};

		LogItem climber_1_amp_avg = new LogItem("CLIMBER-1-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem climber_2_amp = new LogItem("CLIMBER-2-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.climber.mIO.climber_2_amperage.toString();
			}
		};

		LogItem climber_2_amp_avg = new LogItem("CLIMBER-2-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem climber_output = new LogItem("CLIMBER-PRCNT") {

			@Override
			public void update() {
				this.mValue = Robot.climber.mIO.climber_desired_output.toString();
			}
		};

		LogItem intake_l_amp = new LogItem("INTAKE-L-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.intake.mIO.left_amperage.toString();
			}
		};

		LogItem intake_l_amp_avg = new LogItem("INTAKE-L-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem intake_r_amp = new LogItem("INTAKE-R-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.intake.mIO.right_amperage.toString();
			}
		};

		LogItem intake_r_amp_avg = new LogItem("INTAKE-R-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem intake_l_speed = new LogItem("INTAKE-L-PRCNT") {
			@Override
			public void update() {
				this.mValue = Robot.intake.mIO.left_desired_output.toString();
			}
		};

		LogItem intake_r_speed = new LogItem("INTAKE-R-PRCNT") {
			@Override
			public void update() {
				this.mValue = Robot.intake.mIO.right_desired_output.toString();
			}
		};

		LogItem battery_voltage = new LogItem("BATTERY-VOLTAGE") {

			@Override
			public void update() {
				this.mValue = String.valueOf(RobotController.getBatteryVoltage());
			}
		};

		LogItem brownout = new LogItem("BROWNED-OUT") {
			@Override
			public void update() {
				this.mValue = String.valueOf(RobotController.isBrownedOut());
			}
		};

		LogItem pdp_temp = new LogItem("PDP-TEMP") {
			@Override
			public void update() {
				this.mValue = Robot.drive.getPDPTemperature().toString();
			}
		};

		LogItem pdp_temp_avg = new LogItem("PDP-TEMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem pdp_current = new LogItem("PDP-AMP") {
			@Override
			public void update() {
				this.mValue = Robot.drive.getPDPTotalCurrent().toString();
			}
		};

		LogItem pdp_current_avg = new LogItem("PDP-AMP-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem pdp_volt = new LogItem("PDP-VOLT") {

			@Override
			public void update() {
				this.mValue = Robot.drive.getPDPVoltage().toString();
			}
		};

		LogItem pdp_volt_avg = new LogItem("PDP-VOLT-AVG", true) {
			@Override
			public void update() {
				this.mValue = "=AVERAGE($L:$L)";
			}
		};

		LogItem state_drive = new LogItem("DRIVE-STATE") {

			@Override
			public void update() {
				this.mValue = Robot.drive.getStateString() + "-" + Robot.drive.isDisabed();
			}
		};

		LogItem state_elevator = new LogItem("ELEV-STATE") {
			@Override
			public void update() {
				this.mValue = Robot.elevator.getStateString() + "-" + Robot.elevator.isDisabed();
			}
		};

		LogItem state_intake = new LogItem("INTAKE-STATE") {
			@Override
			public void update() {
				this.mValue = Robot.intake.getStateString() + "-" + Robot.intake.isDisabed();
			}
		};

		LogItem state_climber = new LogItem("CLIMB-STATE") {
			@Override
			public void update() {
				this.mValue = Robot.climber.getStateString() + "-" + Robot.climber.isDisabed();
			}
		};

	}

	public void add(LogItem item) {
		values.add(item);
	}

	public void update() {
		values.forEach((s) -> s.update());
	}

	public String getHeader() {
		String retval = "";
		retval += Util.dateTime(false);

		for (LogItem item : values) {
			retval += ("," + item.getHeader());
		}
		return retval;
	}

	public String getFunctions() {
		this.update();

		String retval = "";
		retval += "Functions";

		int counter = 1;
		// Loop through all values
		for (LogItem item : values) {

			// If standard value, just ignore this column
			if (!item.mIsFormula) {
				retval += ",";
			} else {
				// If is formula
				String temp;

				try {
					// Replace "$L"s and "$R"s with appropriate letter
					temp = item.getValue();
					temp = temp.replace("$L", Util.letters[counter]);
					temp = temp.replace("$R", Util.letters[counter + 2]);

				} catch (Exception e) {

					// In case of out of bounds area
					temp = "Error with function " + item.getHeader();
					System.out.println(temp);

				}

				// Add to value
				retval += "," + temp;
			}
			// Add to counter
			counter++;
		}

		return retval;
	}

	public String getLog() {
		String retval = "";
		update();

		retval += Util.dateTime(true);

		for (LogItem item : values) {
			if (!item.mIsFormula)
				retval += "," + item.getValue();
			else
				retval += ",";
		}

		return retval;
	}

	public abstract class LogItem {
		private String mName = "";
		public String mValue = Constants.kFileManagement.DEFAULT_LOG_VALUE;

		private Boolean mIsFormula = false;

		public LogItem(String header) {
			this.mName = header;
			Robot.files.mLog.add(this);
		}

		/**
		 * Use $L to signify column to the left of value Use $R to signify column to the
		 * right of value
		 */
		public LogItem(String header, boolean isFormula) {
			this.mName = header;
			mIsFormula = isFormula;
			Robot.files.mLog.add(this);
		}

		public String getHeader() {
			return this.mName;
		}

		public String getValue() {
			return this.mValue;
		}

		public abstract void update();
	}
}
