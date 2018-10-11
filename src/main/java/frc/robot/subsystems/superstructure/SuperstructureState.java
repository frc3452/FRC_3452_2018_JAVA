package frc.robot.subsystems.superstructure;

import frc.robot.Constants.kElevator;
import frc.robot.Constants.kWrist;
    
public class SuperstructureState {

    public double height = kElevator.BOTTOM_ROTATION;
    public double angle = kWrist.MINIMUM_ANGLE;

    public SuperstructureState(double height, double angle)
    {
        this.height = height;
        this.angle = angle;
    }

    public SuperstructureState(SuperstructureState other)
    {
        this.height = other.height;
        this.angle = other.angle;
    }

    public SuperstructureState()
    {
        this(kElevator.BOTTOM_ROTATION, kWrist.MINIMUM_ANGLE);
    }

    /**
    public boolean inIllegalZone(boolean allowSmallErrors) {
        double kAllowableWristAngleError = allowSmallErrors ? 5.5 : 0;
        double kAllowableElevatorHeightError = allowSmallErrors ? 1 : 0;

        if (height >= SuperstructureConstants.kClearFirstStageMaxHeight + kAllowableElevatorHeightError &&
                angle < SuperstructureConstants.kClearFirstStageMinWristAngle - kAllowableWristAngleError) {
            return true;
        }

        return false;
    }
     */

    /** 
    public boolean inIllegalJawZone() {
        return angle < SuperstructureConstants.kAlwaysNeedsJawClampMinAngle && !jawClamped;
    }
    */
    public boolean inIllegalZone(boolean allowSmallErrors)
    {
        double wristAngleError = allowSmallErrors ? 4 : 0;

        if (wristAngleError > kWrist.MAXIMUM_ANGLE)        
            return true;

        return false;
    }

    public boolean inIllegalZone()
    {
        return inIllegalZone(false);
    }

}