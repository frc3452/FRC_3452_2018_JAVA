package frc.robot.subsystems.superstructure;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Constants.kSuperstructure;
import frc.robot.Constants.kSuperstructure.Poofs;

public class SuperstructureStateMachine {

    private SystemState mState = SystemState.MANUAL;
    private SuperstructureCommand mTargetCommand = new SuperstructureCommand();

    public enum WantedAction {
        NEUTRAL, WANT_MANUAL, MOVE_TO_POSITION,
    }

    public enum SystemState {
        MANUAL, MOVING_TO_POSITION, HOLDING_POSITION
    }

    /**
     * Used by superstructure to set desired height
     */
    public void setHeight(double height) {
        mTargetCommand.height = height;
    }

    /**
     * Used by superstructure to set desired angle
     */
    public void setAngle(double angle) {
        mTargetCommand.angle = angle;
    }

    /**
     * Used by superstructure to set desired elevator power
     * 
     * @param power
     */
    public void setElevatorPower(double power) {
        mTargetCommand.openLoopElevatorPercent = power;
    }

    /**
     * Used by superstructure to set desired wrist power
     */
    public void setWristPower(double power) {
        mTargetCommand.openLoopWristPercent = power;
    }

    /**
     * Generates command for superstructure to send to subsystems
     */
    public SuperstructureCommand update(WantedAction wanted, SuperstructureState currentStructureState) {

        synchronized (SuperstructureStateMachine.this) {
            //Transition state
            SystemState newState;
            switch (mState) {
            case MANUAL:
                newState = handleManualTransition(wanted);
                break;
            case HOLDING_POSITION:
                newState = handleHoldingTransitions(wanted);
                break;
            case MOVING_TO_POSITION:
                newState = handleMovingToPositionTransition(wanted);
                break;
            default:
                System.out.println("ERROR: Unhandleable Superstructure state reached: " + mState);
                newState = mState;
            }

            if (newState != mState) {
                System.out.println("Superstructure state changed from " + mState + " --> " + newState);
                mState = newState;
            }

            //Generate output command
            SuperstructureCommand mOutputCommand = new SuperstructureCommand();

            //Match command to state
            modifyCommandWithState(mOutputCommand, wanted, mState);

            //Apply limiting saftey to command
            mOutputCommand = applySafteyToCommand(mTargetCommand, currentStructureState, LimitingOptions.POOFS);

            //Give back to superstructure
            return mOutputCommand;
        }
    }

    //Match command to state and wanted action
    private void modifyCommandWithState(SuperstructureCommand command, WantedAction wanted, SystemState state) {
        if (state == SystemState.MANUAL) {
            command.openLoopWrist = true;
            command.openLoopElevator = true;

            if (wanted == WantedAction.NEUTRAL) {
                command.openLoopElevatorPercent = 0;
                command.openLoopWristPercent = 0;
            }
        } else {
            command.openLoopElevator = false;
            command.openLoopWrist = false;
        }
    }

    //For testing, limit either a 2018 cheesy poofs style robot or a standard wrist
    private enum LimitingOptions {
        POOFS, STANDARD_WRIST
    }

    /**
     * Generate possible output from target combined with saftey
     */
    private SuperstructureCommand applySafteyToCommand(SuperstructureCommand target, SuperstructureState state,
            LimitingOptions option) {
        SuperstructureCommand command = new SuperstructureCommand();

        // if we're actually using closed loop,
        if (!target.openLoopElevator || !target.openLoopWrist) {

            switch (option) {
            case POOFS:
                // Not possible, but prioritize height
                if (target.angle < Poofs.CLEAR_FIRST_STAGE_MIN_ANGLE
                        && target.height > Poofs.CLEAR_FIRST_STAGE_MIN_HEIGHT)
                    command.angle = Math.max(target.angle, Poofs.CLEAR_FIRST_STAGE_MIN_ANGLE);

                // Folded, want to go high
                if (target.height > kSuperstructure.Poofs.CLEAR_FIRST_STAGE_MIN_HEIGHT
                        && state.angle < Poofs.CLEAR_FIRST_STAGE_MIN_ANGLE)
                    command.height = Math.min(target.height, Poofs.CLEAR_FIRST_STAGE_MIN_HEIGHT);

                // Want to fold, wait for wrist
                else if (target.angle < Poofs.CLEAR_FIRST_STAGE_MIN_ANGLE
                        && state.height > Poofs.CLEAR_FIRST_STAGE_MIN_HEIGHT)
                    command.angle = Math.max(target.angle, Poofs.CLEAR_FIRST_STAGE_MIN_ANGLE);

                command.height = Math.min(target.height, Poofs.ELEV_MAX_HEIGHT);
                command.height = Math.max(target.height, Poofs.ELEV_MIN_HEIGHT);
                command.angle = Math.min(target.angle, Poofs.WRIST_MAX_RANGE);
                command.angle = Math.max(target.angle, Poofs.WRIST_MIN_RANGE);

                break;
            case STANDARD_WRIST:
                command.height = Math.min(target.height, Constants.kSuperstructure.Wrist.ELEV_MIN_HEIGHT);
                command.height = Math.max(target.height, Constants.kSuperstructure.Wrist.ELEV_MAX_HEIGHT);
                command.angle = Math.min(target.angle, Constants.kSuperstructure.Wrist.WRIST_MAX_RANGE);
                command.angle = Math.max(target.angle, Constants.kSuperstructure.Wrist.WRIST_MIN_RANGE);
                break;
            }

        } else {
            // Let other systems handle open loop limiting
            command = target;
        }
        return command;
    }

    /**
     * Handle transition
     */
    private SystemState handleMovingToPositionTransition(WantedAction wantedAction) {
        return handleTransitions(wantedAction);
    }

    /**
     * Handle transition
     */
    private SystemState handleHoldingTransitions(WantedAction wantedAction) {
        return handleTransitions(wantedAction);
    }


    /**
     * Handle transition
     */
    private SystemState handleManualTransition(WantedAction wantedAction) {
        return handleTransitions(wantedAction);
    }

    /**
     * TODO need to compare if mTarget is close to mState
     */
    private SystemState handleTransitions(WantedAction wantedAction) {
        if (wantedAction == WantedAction.WANT_MANUAL) {
            return SystemState.MANUAL;
        } else if (wantedAction == WantedAction.NEUTRAL) {
            return SystemState.MANUAL;
        } else {
            if (wantedAction == WantedAction.MOVE_TO_POSITION && Robot.elevator.isMovementComplete()
                    && Robot.wrist.isMovementComplete()) {
                return SystemState.HOLDING_POSITION;
            }
            return SystemState.MOVING_TO_POSITION;
        }
    }
}