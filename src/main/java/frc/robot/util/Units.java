package frc.robot.util;


//thx 254
public class Units {
	
	public static double ticks_to_rotations(double ticks)
	{
		return ticks / 4096;
	}
	
	public static int rotations_to_ticks(double rotations)
	{
		return (int) (rotations * 4096);
	}
	
    public static double inches_to_meters(double inches) {
        return inches * 0.0254;
    }

    public static double meters_to_inches(double meters) {
        return meters / 0.0254;
    }

    public static double feet_to_meters(double feet) {
        return inches_to_meters(feet * 12.0);
    }

    public static double meters_to_feet(double meters) {
        return meters_to_inches(meters) / 12.0;
    }

    public static double degrees_to_radians(double degrees) {
        return Math.toRadians(degrees);
    }

    public static double radians_to_degrees(double radians) {
        return Math.toDegrees(radians);
    }
}
