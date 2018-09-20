package frc.robot.util;

import frc.robot.Robot;

import edu.wpi.first.wpilibj.Spark;

public class ExampleGZSubsystem extends GZSubsystem {
	/**
	 * The synchronized keyword means that if two different threads try to execute a
	 * method at the same time, it makes them wait to do it one at a time. Not doing
	 * this can make for some really fun troubleshooting :)
	 */

	/** Example motor */
	private Spark example_motor;

	/** Current state of subsystem and wanted state of subsystem*/
	private ExampleState mState = ExampleState.NEUTRAL;
	private ExampleState mWantedState = mState;

	//Input & output object.
	public IO mIO = new IO();

	
	public ExampleGZSubsystem() {
		
	}
	
	/**
	 *  
	 */
	public synchronized void construct()
	{
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
	 * force it to stop moving. read into handleStates(), in(), and out() to learn
	 * about those functions.
	 */
	@Override
	public void loop() {
		handleStates();
		in();
		out();

		switch (mState) {
		case MANUAL:

			mIO.output = mIO.desired_output;

			break;
		case NEUTRAL:

			mIO.output = 0;
			break;

		case DEMO:
			break;

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
	static class IO {
		// In
		public double speed = -1;
		public double position = -1;

		// out
		private double output = 0;
		public double desired_output = 0;
	}

	/**
	 * here, we update the Values class information with input from our sensors.
	 * read into the Values class as to why we do this.
	 */
	@Override
	protected void in() {
		mIO.speed = example_motor.getSpeed();
		mIO.position = example_motor.getPosition();
	}

	/**
	 * For example, in this method we will set the state to manual and the desired
	 * output to whatever the joystick left analog up and down axis that is given to
	 * this method is.
	 */
	public void exampleMethodRunMotorWithJoystick(GZJoystick joy) {
		setWantedState(ExampleState.MANUAL);
		mIO.desired_output = joy.getLeftAnalogY();
	}

	/** Set our motor values to what they should be */
	@Override
	protected void out() {
		example_motor.set(mIO.output);
	}

	/**
	 * States for the subsystem. All will have a NEUTRAL, but other states will
	 * vary.
	 */
	public enum ExampleState {
		NEUTRAL, MANUAL, DEMO
	}

	/** Set the desired state of the subsystem to NEUTRAL */
	@Override
	public void stop() {
		setWantedState(ExampleState.NEUTRAL);
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
	 * This is the central control for each subsystem. This sets the wanted state to
	 * whatever we please. mWantedState will be used in the next method.
	 */
	public void setWantedState(ExampleState wantedState) {
		this.mWantedState = wantedState;
	}

	/**
	 * This is the method we use to control the state of the subsystem. We do this
	 * instead of just changing the mState variable because this is what keeps the
	 * robot locked in a disabled state if we call .disable(true) on the subsystem.
	 * This also calls onStateExit and onStateStart for the current and new state.
	 * 
	 * First, we check if it's disabled and not connected to the field, or wanting to be stopped,
	 *  thats our first priority. If it is, we put the system in neutral. If it isn't, we check if
	 * its in demo. If so, set the current state to demo. Then, if we aren't in demo
	 * or disabled, which means were safe to switch to any state, we do that.
	 * 
	 * The 'if stateNot()' block is used to call the exit and start
	 * functions of each state, but only the first time we go into that state. To
	 * come from the previous example, maybe we zero a sensor when we first go into
	 * a certain state. We don't want to continually zero that sensor, we only want
	 * to do it once.
	 * 
	 * <b> Keep in mind, among the if statements (on lines with 'AAAA'), only one of
	 * these can be true at any given time. By writing it this way, it lets us have
	 * a hierarchy of what we need the subsystem to be controlled by. (Saftey first)
	 */
	private synchronized void handleStates() {

		if ((this.isDisabed() && !Robot.gzOI.isFMS()) || mWantedState == ExampleState.NEUTRAL) { /* AAAA **/

			if (stateNot(ExampleState.NEUTRAL)) {
				onStateExit(mState);
				mState = ExampleState.NEUTRAL;
				onStateStart(mState);
			}

		} else if (Robot.auton.isDemo()) { /* AAAA **/

			if (stateNot(ExampleState.DEMO)) {
				onStateExit(mState);
				mState = ExampleState.DEMO;
				onStateStart(mState);
			}

		} else if (mWantedState != mState) { /* AAAA **/
			onStateExit(mState);
			mState = mWantedState;
			onStateStart(mState);
		}

	}

	/**
	 * This method returns if a given state is NOT what the current state is. we use
	 * this in handleStates().
	 */
	private synchronized boolean stateNot(ExampleState state) {
		return mState != state;
	}

	/**
	 * This is used with subsystems that use CAN bus and following. When entering
	 * Test mode, the robot will take every motor out of follower mode, unless this
	 * is called.
	 */
	public synchronized void enableFollower() {
		// controller_2.follow(controller_1);
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
