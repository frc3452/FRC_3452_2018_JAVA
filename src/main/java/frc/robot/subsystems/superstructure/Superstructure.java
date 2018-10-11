package frc.robot.subsystems.superstructure;

import frc.robot.util.GZSubsystem;
import frc.robot.Robot;

public class Superstructure extends GZSubsystem {
    
    private SuperstructureState mState = new SuperstructureState();
    private SuperstructureStateMachine.WantedState mWantedState = SuperstructureStateMachine.WantedState.NEUTRAL;
    
    public SuperstructureStateMachine mStateMachine = new SuperstructureStateMachine();
    private SuperstructureCommand mCommand = new SuperstructureCommand();

    public Superstructure()
    {
    }

    private synchronized void updateFromObservedState(SuperstructureState state)
    {
        state.angle = Robot.wrist.getAngle();
        state.height = Robot.elevator.getHeight();
    }

    public void loop(){
        updateFromObservedState(mState);   
        
        mCommand = mStateMachine.update(mWantedState, mState);
        
        sendToSubsystems(mCommand);
    }
    
    public void sendToSubsystems(SuperstructureCommand command)
    {
        if (command.openLoopElevator)
        {
            Robot.elevator.manual(command.openLoopElevatorPercent);
        } else {
            Robot.elevator.setHeight(command.height);
        }

        if (command.openLoopWrist)
        {
            Robot.wrist.setPercentage(command.openLoopWristPercent);
        } else {
            Robot.wrist.setAngle(command.wristAngle);
        }
    }

    public void in(){}

    public void out(){}

    public void stop(){}

    public void construct(){}

    public void initDefaultCommand(){}

    public String getStateString()
    {
        return "";
    }

}