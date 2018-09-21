package frc.robot.util;

import java.util.ArrayList;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.RobotController;

public class GZLog {

	ArrayList<LogItem> values = new ArrayList<>();

	@SuppressWarnings("unused")	
	public GZLog() {
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


		LogItem elev_1_amp = new LogItem("ELEV-1-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.mIO.elevator_1_amp.toString();
			}
		};

		LogItem elev_2_amp = new LogItem("ELEV-2-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.mIO.elevator_2_amp.toString();	
			}
		};

		LogItem elev_1_volt = new LogItem("ELEV-1-VOLT")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.mIO.elevator_1_volt.toString();
			}
		};

		LogItem elev_2_volt = new LogItem("ELEV-2-VOLT")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.mIO.elevator_2_volt.toString();
			}
		};

		LogItem elev_up_limit = new LogItem("ELEV-UP-LMT")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.getTopLimit().toString();
			}
		};

		LogItem elev_down_limit = new LogItem("ELEV-DOWN-LMT")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.getBottomLimit().toString();
			}
		};

		LogItem elev_rotations = new LogItem("ELEV-ROT"){
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.getRotations().toString();
			}
		};
		LogItem elev_speed = new LogItem("ELEV-SPEED")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.getSpeed().toString();
			}
		};

		LogItem climber_1_amp = new LogItem("CLIMBER-1-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.climber.mIO.climber_1_amperage.toString();
			}
		};

		LogItem climber_2_amp = new LogItem("CLIMBER-2-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.climber.mIO.climber_2_amperage.toString();
			}
		};

		LogItem climber_output = new LogItem("CLIMBER-SPD"){
		
			@Override
			public void update() {
				this.mValue = Robot.climber.mIO.climber_desired_output.toString();
			}
		};

		LogItem intake_l_amp = new LogItem("INTAKE-L-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.intake.mIO.left_amperage.toString();
			}
		};

		LogItem intake_r_amp = new LogItem("INTAKE-R-AMP")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.intake.mIO.right_amperage.toString();
			}
		};

		LogItem intake_l_speed = new LogItem("INTAKE-L-SPD")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.intake.mIO.left_desired_output.toString();
			}
		};

		LogItem intake_r_speed = new LogItem("INTAKE-R-SPD")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.intake.mIO.right_desired_output.toString();
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

		LogItem state_drive = new LogItem("DRIVE-STATE"){
		
			@Override
			public void update() {
				this.mValue = Robot.drive.getStateString() + "-" + Robot.drive.isDisabed();
			}
		};

		LogItem state_elevator = new LogItem("ELEV-STATE")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.elevator.getStateString() + "-" + Robot.elevator.isDisabed();
			}
		};

		LogItem state_intake = new LogItem("INTAKE-STATE")
		{
			@Override
			public void update()
			{
				this.mValue = Robot.intake.getStateString() + "-" + Robot.intake.isDisabed();
			}
		};

		LogItem state_climber = new LogItem("CLIMB-STATE")
		{
			@Override
			public void update()
			{
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

	public String getLog() {
		String retval = "";
		update();

		retval += Util.dateTime(true);

		for (LogItem item : values)
			retval += "," + item.getValue();

		return retval;
	}
}
