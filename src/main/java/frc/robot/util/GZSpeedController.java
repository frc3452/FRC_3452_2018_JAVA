package frc.robot.util;

public interface GZSpeedController {
    public Double getAmperage();

    public String getGZName();

    public Double getTemperatureSensor();
    public boolean hasTemperatureSensor();
}