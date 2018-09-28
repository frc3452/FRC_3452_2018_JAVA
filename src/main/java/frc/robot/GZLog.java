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
	
		LogItem left_speed = new LogItem("L-RPM"){
			@Override
			public void update() {
				this.mValue = Robot.drive.getLeftVel().toString();
			}
		};

		LogItem right_speed = new LogItem("R-RPM")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.drive.getRightVel().toString();
			}
		};

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

		LogItem battery_voltage = new LogItem("BATTERY-VOLTAGE"){
		
			@Override
			public void update() {
				this.mValue = String.valueOf(RobotController.getBatteryVoltage());
			}
		};

		LogItem brownout = new LogItem("BROWNED-OUT")
		{
			@Override
			public void update()
			{
				this.mValue = String.valueOf(RobotController.isBrownedOut());
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

	public void addLogItemsToLog()
	{

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

	public abstract class LogItem {
		private String mName = "";
		public String mValue = Constants.kFileManagement.DEFAULT_LOG_VALUE;
	
		public LogItem(String header) {
			this.mName = header;
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
