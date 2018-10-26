package frc.robot.subsystems.superstructure;

import frc.robot.util.GZSubsystem;
import frc.robot.Robot;
import frc.robot.subsystems.superstructure.SuperstructureStateMachine.WantedAction;

public class Superstructure extends GZSubsystem {

    private SuperstructureState mState = new SuperstructureState();
    private SuperstructureStateMachine.WantedAction mWantedAction = SuperstructureStateMachine.WantedAction.NEUTRAL;

    private SuperstructureStateMachine mStateMachine = new SuperstructureStateMachine();

    public Superstructure() {
    }

    /**
     * Fill information about superstructure into variable
     * 
     * @param state
     */
    private synchronized void updateFromObservedState(SuperstructureState state) {
        state.angle = Robot.wrist.getAngle();
        state.height = Robot.elevator.getHeight();
    }

    /**
     * Set wanted state to neutral
     */
    public void setNeutral() {
        mWantedAction = WantedAction.NEUTRAL;
    }

    /**
     * Update state machine desired height, angle, and wanted action
     */
    public void setPosition(double height, double angle) {
        mWantedAction = WantedAction.MOVE_TO_POSITION;
        mStateMachine.setHeight(height);
        mStateMachine.setAngle(angle);
    }

    /**
     * Set manual speed of elevator and wrist
     */
    public void setSpeeed(double elev, double wrist) {
        mWantedAction = WantedAction.WANT_MANUAL;
        mStateMachine.setElevatorPower(elev);
        mStateMachine.setWristPower(wrist);
    }

    /**
     * Get command from state machine based of current state and desired action
     * Send to subsystem
     */
    public void loop() {
        updateFromObservedState(mState);
        SuperstructureCommand mCommand = mStateMachine.update(mWantedAction, mState);

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