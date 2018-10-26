package frc.robot.subsystems.superstructure;

import frc.robot.Constants;
import frc.robot.Robot;
import frc.robot.Constants.kSuperstructure;
import frc.robot.Constants.kSuperstructure.Poofs;

public class SuperstructureStateMachine {

    private SystemState mState = SystemState.MANUAL;
    private SuperstructureCommand mTargetCommand = new SuperstructureCommand();

    public void setHeight(double height) {
        mTargetCommand.height = height;
    }

    public void setAngle(double angle) {
        mTargetCommand.angle = angle;
    }

    public void setElevatorPower(double power) {
        mTargetCommand.openLoopElevatorPercent = power;
    }

    public void setWristPower(double power) {
        mTargetCommand.openLoopWristPercent = power;
    }

    public void setWristForwardLimit(double angle) {
    }

    public enum WantedState {
        NEUTRAL, WANT_MANUAL, MOVE_TO_POSITION,
    }

    public enum SystemState {
        MANUAL, MOVING_TO_POSITION, HOLDING_POSITION
    }

    public SuperstructureCommand update(WantedState wanted, SuperstructureState currentStructureState) {

        synchronized (SuperstructureStateMachine.this) {
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

            SuperstructureCommand mOutputCommand = new SuperstructureCommand();
            modifyCommandWithState(mOutputCommand, mState);
            mOutputCommand = applySafteyToCommand(mTargetCommand, currentStructureState, LimitingOptions.POOFS);

            return mOutputCommand;
        }
    }

    private void modifyCommandWithState(SuperstructureCommand command, SystemState state) {
        if (state == SystemState.MANUAL) {
            command.openLoopWrist = true;
            command.openLoopElevator = true;
        } else {
            command.openLoopElevator = false;
            command.openLoopWrist = false;
        }
    }

    private enum LimitingOptions {
        POOFS, STANDARD_WRIST
    }

    /**
     * Lets put in a few different posibilities for mechanisms
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

    private SystemState handleMovingToPositionTransition(WantedState wantedAction) {
        return handleTransitions(wantedAction);
    }

    private SystemState handleHoldingTransitions(WantedState wantedAction) {
        return handleTransitions(wantedAction);
    }

    private SystemState handleManualTransition(WantedState wantedAction) {
        return handleTransitions(wantedAction);
    }

    private SystemState handleTransitions(WantedState wantedAction) {
        if (wantedAction == WantedState.WANT_MANUAL) {
            return SystemState.MANUAL;
        } else if (wantedAction == WantedState.NEUTRAL) {
            return SystemState.MANUAL;
        } else {
            if (wantedAction == WantedState.MOVE_TO_POSITION && Robot.elevator.isMovementComplete()
                    && Robot.wrist.isMovementComplete()) {
                return SystemState.HOLDING_POSITION;
            }
            return SystemState.MOVING_TO_POSITION;
        }
    }
}