package frc.robot.subsystems.superstructure;

import frc.robot.Constants.kElevator;
import frc.robot.Constants.kWrist;

public class SuperstructureCommand {
    public double height = kElevator.BOTTOM_ROTATION;
    public double angle = kWrist.MINIMUM_ANGLE;

    public boolean openLoopElevator = false;
    public double openLoopElevatorPercent = 0.0;

    public boolean openLoopWrist = false;
    public double openLoopWristPercent = 0.0;
}