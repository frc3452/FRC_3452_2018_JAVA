package frc.robot.util;

import java.util.Arrays;
import java.util.List;

import frc.robot.Constants.kFileManagement;
import frc.robot.Constants.kPDP;
import frc.robot.Robot;

import edu.wpi.first.wpilibj.RobotController;

public class GZLog {

	List<LogItem> values = null;

	LogItem left_speed, right_speed, l1_amp, l2_amp, l3_amp, l4_amp, r1_amp, r2_amp, r3_amp, r4_amp, l1_volt, l2_volt,
			l3_volt, l4_volt, r1_volt, r2_volt, r3_volt, r4_volt,battery_voltage, pdp_temp, pdp_current, pdp_volt;

	public GZLog() {
		left_speed = new LogItem("L-RPM");
		right_speed = new LogItem("R-RPM");
		l1_amp = new LogItem("L1-AMP");
		l2_amp = new LogItem("L2-AMP");
		l3_amp = new LogItem("L3-AMP");
		l4_amp = new LogItem("L4-AMP");
		r1_amp = new LogItem("R1-AMP");
		r2_amp = new LogItem("R2-AMP");
		r3_amp = new LogItem("R3-AMP");
		r4_amp = new LogItem("R4-AMP");
		l1_volt = new LogItem("L1-VOLT");
		l2_volt = new LogItem("L2-VOLT");
		l3_volt = new LogItem("L3-VOLT");
		l4_volt = new LogItem("L4-VOLT");
		r1_volt = new LogItem("R1-VOLT");
		r2_volt = new LogItem("R2-VOLT");
		r3_volt = new LogItem("R3-VOLT");
		r4_volt = new LogItem("R4-VOLT");
		battery_voltage = new LogItem("BATTERY-VOLTAGE");
		pdp_temp = new LogItem("PDP-TEMP");
		pdp_current = new LogItem("PDP-CURRENT");
		pdp_volt = new LogItem("PDP-VOLT");

		values = Arrays.asList(l1_amp, l2_amp, l3_amp, l4_amp, r1_amp, r2_amp, r3_amp, r4_amp,
				l1_volt, l2_volt, l3_volt, l4_volt, r1_volt, r2_volt, r3_volt, r4_volt, battery_voltage, pdp_temp, pdp_current, pdp_volt);
	}

	public void update() {
		l1_amp.setValue(Robot.drive.mIO.L1_amp.toString());
		l2_amp.setValue(Robot.drive.mIO.L2_amp.toString());
		l3_amp.setValue(Robot.drive.mIO.L3_amp.toString());
		l4_amp.setValue(Robot.drive.mIO.L4_amp.toString());
		r1_amp.setValue(Robot.drive.mIO.R1_amp.toString());
		r2_amp.setValue(Robot.drive.mIO.R2_amp.toString());
		r3_amp.setValue(Robot.drive.mIO.R3_amp.toString());
		r4_amp.setValue(Robot.drive.mIO.R4_amp.toString());

		l1_volt.setValue(Robot.drive.mIO.L1_volt.toString());
		l2_volt.setValue(Robot.drive.mIO.L2_volt.toString());
		l3_volt.setValue(Robot.drive.mIO.L3_volt.toString());
		l4_volt.setValue(Robot.drive.mIO.L4_volt.toString());
		r1_volt.setValue(Robot.drive.mIO.R1_volt.toString());
		r2_volt.setValue(Robot.drive.mIO.R2_volt.toString());
		r3_volt.setValue(Robot.drive.mIO.R3_volt.toString());
		r4_volt.setValue(Robot.drive.mIO.R4_volt.toString());

		battery_voltage.setValue(String.valueOf(RobotController.getBatteryVoltage()));
		pdp_temp.setValue(Robot.drive.getPDPTemperature().toString());
		pdp_current.setValue(Robot.drive.getPDPTotalCurrent().toString());
		pdp_volt.setValue(Robot.drive.getPDPVoltage().toString());
	}

	public String getHeader() {
		String retval = "";
		retval += Util.dateTime(false);

		for (LogItem item : values) {
			retval += ("," + item.getHeader());
		}
		return retval;
	}
	
	public String getLog()
	{
		String retval = "";
		update();
		
		retval += Util.dateTime(true);
		
		for (LogItem item : values)
			retval += "," + item.getValue();
		
		return retval;
	}

	private class LogItem {

		private String mName = "";
		private String mValue = kFileManagement.DEFAULT_LOG_VALUE;

		public LogItem(String header) {
			this.mName = header;
		}

		public String getHeader() {
			return mName;
		}

		public void setValue(String value) {
			this.mValue = value;
		}

		public String getValue() {
			return mValue;
		}

	}
}
