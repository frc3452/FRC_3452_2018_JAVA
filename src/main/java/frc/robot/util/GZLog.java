package frc.robot.util;

import java.util.ArrayList;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.RobotController;

public class GZLog {

	ArrayList<LogItem> values = new ArrayList<>();

	@SuppressWarnings("unused")	
	public GZLog() {

		LogItem l1_amp = new LogItem("L1-AMP")
		{
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L1_amp.toString();
			}
		};

		LogItem l2_amp = new LogItem("L2-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L2_amp.toString();
			}
		};

		LogItem l3_amp = new LogItem("L3-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L3_amp.toString();
			}
		};

		LogItem l4_amp = new LogItem("L4-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L4_amp.toString();
			}
		};

		LogItem r1_amp = new LogItem("R1-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R1_amp.toString();
			}
		};

		LogItem r2_amp = new LogItem("R2-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R2_amp.toString();
			}
		};

		LogItem r3_amp = new LogItem("R3-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R3_amp.toString();
			}
		};

		LogItem r4_amp = new LogItem("R4-AMP"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R4_amp.toString();
			}
		};

		LogItem l1_volt = new LogItem("L1-VOLT")
		{
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L1_volt.toString();
			}
		};

		LogItem l2_volt = new LogItem("L2-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L2_volt.toString();
			}
		};

		LogItem l3_volt = new LogItem("L3-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L3_volt.toString();
			}
		};

		LogItem l4_volt = new LogItem("L4-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.L4_volt.toString();
			}
		};

		LogItem r1_volt = new LogItem("R1-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R1_volt.toString();
			}
		};

		LogItem r2_volt = new LogItem("R2-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R2_volt.toString();
			}
		};

		LogItem r3_volt = new LogItem("R3-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R3_volt.toString();
			}
		};

		LogItem r4_volt = new LogItem("R4-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.mIO.R4_volt.toString();
			}
		};
		LogItem battery_voltage = new LogItem("BATTERY-VOLTAGE"){
		
			@Override
			public void update() {
				this.mValue = String.valueOf(RobotController.getBatteryVoltage());
			}
		};

		LogItem pdp_temp = new LogItem("PDP-TEMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.drive.getPDPTemperature().toString();
			}
		};

		LogItem pdp_current = new LogItem("PDP-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.drive.getPDPTotalCurrent().toString();
			}
		};

		LogItem pdp_volt = new LogItem("PDP-VOLT"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.getPDPVoltage().toString();
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

	public String getLog() {
		String retval = "";
		update();

		retval += Util.dateTime(true);

		for (LogItem item : values)
			retval += "," + item.getValue();

		return retval;
	}
}
