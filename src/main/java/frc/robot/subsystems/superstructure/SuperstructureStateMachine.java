package frc.robot.subsystems.superstructure;

import frc.robot.Robot;
import frc.robot.Constants.kElevator;
import frc.robot.Constants.kWrist;

public class SuperstructureStateMachine {

    private SystemState mState = SystemState.MANUAL;
    private SystemState mWantedState = SystemState.MANUAL;
    private SuperstructureCommand mCommand = new SuperstructureCommand();

    public void setHeight(double height)
    {
        mCommand.height = height;
    }

    public void setAngle(double angle)
    {
        mCommand.wristAngle = angle;
    }

    public void setElevatorPower(double power)
    {
        mCommand.openLoopElevatorPercent = power;
    }

    public void setWristPower(double power)
    {
        mCommand.openLoopWristPercent = power;
    }


    public enum WantedState {
        NEUTRAL, WANT_MANUAL, MOVE_TO_POSITION,
    }

    public enum SystemState {
        MANUAL, MOVING_TO_POSITION, HOLDING_POSITION
    }


    public SuperstructureCommand update(WantedState wanted, SuperstructureState sysState) {
        synchronized (SuperstructureStateMachine.this) {
            SystemState newState;

            switch (mState) {
            case MANUAL:
                newState = handleTransitions(wanted);
                break;
            case HOLDING_POSITION:
                newState = handleTransitions(wanted);
                break;
            case MOVING_TO_POSITION:
                newState = handleTransitions(wanted);
                break;
            default:
                System.out.println("ERROR: Unhandleable Superstructure state reached: " + mState);
                newState = mState;
            }

            if (newState != mState)
            {
                System.out.println("Superstructure state changed from " + mState + " --> " + newState);
                mState = newState;
            }

            fillCommand(mState);
            
            return mCommand;
        }
    }

    private void fillCommand(SystemState state)
    {
        if (state == SystemState.MANUAL)
        {
            mCommand.openLoopWrist = true;
            mCommand.openLoopElevator = true;
        } else {
            mCommand.openLoopElevator = false;
            mCommand.openLoopWrist = false;
        }
    }

    private SystemState handleTransitions(WantedState wantedAction) {
        if (wantedAction == WantedState.WANT_MANUAL) {
            return SystemState.MANUAL;
        } else if (wantedAction == WantedState.NEUTRAL) {
            return SystemState.MANUAL;
        } else {
            if (wantedAction == WantedState.MOVE_TO_POSITION && Robot.elevator.isEncoderMovementDone()) {
                return SystemState.HOLDING_POSITION;
            }
            return SystemState.MOVING_TO_POSITION;
        }
    }
}