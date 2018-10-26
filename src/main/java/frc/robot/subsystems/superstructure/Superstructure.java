package frc.robot.subsystems.superstructure;

import frc.robot.util.GZSubsystem;
import frc.robot.Robot;
import frc.robot.subsystems.superstructure.SuperstructureStateMachine.WantedState;

public class Superstructure extends GZSubsystem {

    private SuperstructureState mState = new SuperstructureState();
    private SuperstructureStateMachine.WantedState mWantedState = SuperstructureStateMachine.WantedState.NEUTRAL;

    private SuperstructureStateMachine mStateMachine = new SuperstructureStateMachine();

    public Superstructure() {
    }

    private synchronized void updateFromObservedState(SuperstructureState state) {
        state.angle = Robot.wrist.getAngle();
        state.height = Robot.elevator.getHeight();
    }

    public void setNeutral() {
        mWantedState = WantedState.NEUTRAL;
    }

    public void setHeight(double height) {
        mWantedState = WantedState.MOVE_TO_POSITION;
        mStateMachine.setHeight(height);
    }

    public void setAngle(double angle) {
        mWantedState = WantedState.NEUTRAL;
    }

    public void loop() {
        updateFromObservedState(mState);

        SuperstructureCommand mCommand = mStateMachine.update(mWantedState, mState);

        sendToSubsystems(mCommand);
    }

    /**
     * Interface command into subsystems appropriately
     */
    private void sendToSubsystems(SuperstructureCommand command) {
        if (command.openLoopElevator) {
            if (command.openLoopElevatorPercent != 0)
                Robot.elevator.manual(command.openLoopElevatorPercent);
            else
                Robot.elevator.stop();
        } else {
            Robot.elevator.setHeight(command.height);
        }

        if (command.openLoopWrist) {
            if (command.openLoopWristPercent != 0)
                Robot.wrist.setPercentage(command.openLoopWristPercent);
            else
                Robot.wrist.stop();
        } else {
            Robot.wrist.setAngle(command.angle);
        }
    }

    public void in() {
    }

    public void out() {
    }

    public void stop() {
    }

    public void construct() {
    }

    public void initDefaultCommand() {
    }

    public String getStateString() {
        return "";
    }

}