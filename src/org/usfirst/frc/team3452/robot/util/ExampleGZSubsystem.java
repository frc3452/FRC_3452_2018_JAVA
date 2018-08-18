package org.usfirst.frc.team3452.robot.util;

import org.usfirst.frc.team3452.robot.Robot;

import edu.wpi.first.wpilibj.Spark;

public class ExampleGZSubsystem extends GZSubsystem {
	/**
	 * The synchronized keyword means that if two different threads try to execute a
	 * method at the same time, it makes them wait to do it one at a time. Not doing
	 * this can make for some really fun troubleshooting :)
	 */

	/** Example motor */
	private Spark example_motor;

	/** Current state of subsystem */
	private ExampleState mState = ExampleState.NEUTRAL;
	private ExampleState prevState = mState;

	/**
	 * This is a constructor, this runs when you create the object in Robot.java.
	 * Use this for initialization
	 */
	public ExampleGZSubsystem() {
		example_motor = new Spark(1);
	}

	/**
	 * onStateStart is what we call when we are first entering a state. If we
	 * entering a state where we use an encoder, we may want to enable certain
	 * functions when we first enter that state.
	 */
	private void onStateStart(ExampleState s) {
		switch (s) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	/**
	 * onStateExit is what we call when we are exiting a state. To follow from the
	 * previous example, if we are leaving a state where we are using an encoder, we
	 * may want to disable certain functions after we are done using that state
	 */
	private void onStateExit(ExampleState s) {
		switch (s) {
		case MANUAL:
			break;
		case NEUTRAL:
			break;
		default:
			break;
		}
	}

	/**
	 * this is the central loop of the subsystem. in this loop, we interface our
	 * desired_output (what we want the motor to do), with our output (what the
	 * motor is allowed to do). In manual, we allow the motor to run at what we
	 * want. But, in neutral we dont allow the motor to run at what we want, we
	 * force it to stop moving. this.inputOutput() call is running the two functions
	 * below, in() and out(). Read into GZSubsystem and in() and out() to learn
	 * more.
	 */
	@Override
	public void loop() {
		switch (mState) {
		case MANUAL:

			Values.output = Values.desired_output;

			break;
		case NEUTRAL:

			Values.output = 0;
			break;

		case DEMO:

		default:
			System.out.println("WARNING: Incorrect ExampleSubsystem state " + mState + " reached.");
			break;
		}
	}

	/**
	 * The purpose of having this class is because over CAN bus, (a type of
	 * connection), every time we want information, it has to travel through every
	 * single motor controller connected to get us that information. If we need to
	 * read a sensor in three different places, we will call that 'getSensor()'
	 * three times, and require it to send us the same number multiple times, which
	 * slows down the system. Using in(), we fill the values we need every loop (but
	 * only once) so that we can access the stored values without abusing the CAN
	 * bus.
	 * 
	 * as you can see, 'output' is private while 'desired_output' is not (which
	 * means it is public). We make 'output' private because we want that to only be
	 * changed by loop() so that you cannot bypass the disable() call that each
	 * subsystem has (see GZSubsystem) that is implemented for safety. We want
	 * anything to be able to change what the subsystem is trying to do, but nothing
	 * but the subsystem itself to change what it is actually doing.
	 */
	public static class Values {
		// In
		static double speed = -1;
		static double position = -1;

		// out
		static private double output = 0;
		static double desired_output = 0;
	}

	/**
	 * here, we update the Values class information with input from our sensors.
	 * read into the Values class as to why we do this.
	 */
	@Override
	protected void in() {
		Values.speed = example_motor.getSpeed();
		Values.position = example_motor.getPosition();
	}

	// Put methods you need to create here!

	/**
	 * For example, in this method we will set the state to manual and the desired
	 * output to whatever the joystick left analog up and down axis that is given to
	 * this method is.
	 */
	public void exampleMethodRunMotorWithJoystick(GZJoystick joy) {
		setState(ExampleState.MANUAL);
		Values.desired_output = joy.getLeftAnalogY();
	}

	/** Set our motor values to what they should be */
	@Override
	protected void out() {
		example_motor.set(Values.output);
	}

	/**
	 * States for the subsystem. All will have a NEUTRAL, but other states will
	 * vary.
	 */
	public enum ExampleState {
		NEUTRAL, MANUAL, DEMO
	}

	/** Set the state of the subsystem to NEUTRAL */
	@Override
	public void stop() {
		setState(ExampleState.NEUTRAL);
	}

	/**
	 * This call returns the state that the subsystem is in as a String, which means
	 * we can use this to monitor the subsystem in the console, smartdashboard, a
	 * log, etc.
	 */
	@Override
	public String getStateString() {
		return mState.toString();
	}

	/**
	 * This method returns the current state of the subsystem. If we want to check
	 * if 'ExampleSubsytem' is in neutral or manual, we can use this call and then
	 * use an if statement to determine what the subsystem is currently doing.
	 */
	public ExampleState getState() {
		return mState;
	}

	/**
	 * This is the method we use to control the state of the subsystem. We do this
	 * instead of just changing the mState variable because this is what keeps the
	 * robot locked in a disabled state if we call .disable(true) on the subsystem.
	 * This also calls onStateExit and onStateStart for the current and new state.
	 * 
	 * First, we check if it's disabled, thats our first priority. If it is, we put
	 * the system in neutral. If it isn't, we check if its in demo. Either we set it
	 * to demo. Then, if we aren't in demo or disabled, which means were safe to switch 
	 * to any state, we do that.
	 */
	public synchronized void setState(ExampleState wantedState) {
		if (this.isDisabed()) {

			mState = ExampleState.NEUTRAL;
			
		} else if (Robot.autonSelector.isDemo()) {
			
			mState = ExampleState.DEMO;
			
		} else {
			mState = wantedState;
		}
	}
	
	/** This is the runner for onStateExit and onStateStart. */
	public synchronized void checkPrevState()
	{
		if (mState != prevState)
		{
			onStateExit(prevState);
			onStateStart(mState);
		}
		prevState = mState;
	}

	/**
	 * This class is type 'GZSubsystem', which extends the WPI 'Subsystem', meaning
	 * it is a Subsystem with extra info with it. initDefaultCommand() is a call for
	 * 'Subsystem' which lets you set a command to run when no other command is
	 * running.
	 */
	@Override
	protected void initDefaultCommand() {
	}
}
