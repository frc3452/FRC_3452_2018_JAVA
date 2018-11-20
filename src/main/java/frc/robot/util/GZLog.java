package frc.robot.util;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.RobotController;
import frc.robot.Constants;
import frc.robot.subsystems.Drive;

public class GZLog {

	private ArrayList<LogItem> values = new ArrayList<>();

	private static GZLog mInstance = null;

	public synchronized static GZLog getInstance()
	{
		if (mInstance == null)
			mInstance = new GZLog();
		return mInstance;
	}

	private GZLog() {
	}

	@SuppressWarnings("unused")
	public void fillLogger() {

		LogItem left_speed = new LogItem("L-RPM") {
			@Override
			public void update() {
				this.mValue = Drive.getInstance().getLeftVel().toString();
			}
		};

		LogItem left_encoder_valid = new LogItem("L-ENC-PRSNT") {
			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.leftEncoderValid.toString();
			}
		};

		LogItem right_speed = new LogItem("R-RPM") {
			@Override
			public void update() {
				this.mValue = Drive.getInstance().getRightVel().toString();
			}
		};

		LogItem right_encoder_valid = new LogItem("R-ENC-PRSNT") {
			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.rightEncoderValid.toString();
			}
		};

		LogItem l1_amp = new LogItem("L1-AMP") {
			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.L1_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.L2_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.L3_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.L4_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.R1_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.R2_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.R3_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.R4_amp.toString();
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
				this.mValue = Drive.getInstance().mIO.L1_volt.toString();
			}
		};

		LogItem l2_volt = new LogItem("L2-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.L2_volt.toString();
			}
		};

		LogItem l3_volt = new LogItem("L3-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.L3_volt.toString();
			}
		};

		LogItem l4_volt = new LogItem("L4-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.L4_volt.toString();
			}
		};

		LogItem r1_volt = new LogItem("R1-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.R1_volt.toString();
			}
		};

		LogItem r2_volt = new LogItem("R2-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.R2_volt.toString();
			}
		};

		LogItem r3_volt = new LogItem("R3-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.R3_volt.toString();
			}
		};

		LogItem r4_volt = new LogItem("R4-VOLT") {

			@Override
			public void update() {
				this.mValue = Drive.getInstance().mIO.R4_volt.toString();
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
				this.mValue = Drive.getInstance().getPDPTemperature().toString();
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
				this.mValue = Drive.getInstance().getPDPTotalCurrent().toString();
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
				this.mValue = Drive.getInstance().getPDPVoltage().toString();
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
				this.mValue = Drive.getInstance().getStateString() + "-" + Drive.getInstance().isDisabed();
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
		retval += GZUtil.dateTime(false);

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
					temp = temp.replace("$L", GZUtil.letters[counter]);
					temp = temp.replace("$R", GZUtil.letters[counter + 2]);

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

		retval += GZUtil.dateTime(true);

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
			GZLog.getInstance().add(this);
		}

		/**
		 * Use $L to signify column to the left of value Use $R to signify column to the
		 * right of value
		 */
		public LogItem(String header, boolean isFormula) {
			this.mName = header;
			mIsFormula = isFormula;
			GZLog.getInstance().add(this);
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
