package team3452.robot.subsystems;

import com.ctre.phoenix.ParamEnum;
import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.command.Subsystem;
import team3452.robot.Robot;
import team3452.robot.util.Constants;

import java.util.ArrayList;

/**
 * <b>Elevator subsystem</b> Handles elevator open + closed loop and speed
 * control for drivetrain
 *
 * @author max
 */
public class Elevator extends Subsystem implements GZSubsystem {
    public double m_pos = 0;
    public boolean m_overriden = false;

    public WPI_TalonSRX Elev_1;
    public WPI_TalonSRX Elev_2;

    public boolean isRemoteSensor = true;

    private ArrayList<WPI_TalonSRX> controllers = new ArrayList<WPI_TalonSRX>();

    /**
     * hardware initialization
     *
     * @author max
     * @since
     */
    public Elevator() {
        Elev_1 = new WPI_TalonSRX(Constants.kElevator.E_1);
        Elev_2 = new WPI_TalonSRX(Constants.kElevator.E_2);

        controllers.add(Elev_1);
        controllers.add(Elev_2);

        //Init talons
        for (WPI_TalonSRX talons : controllers)
            generalTalonInit(talons);

        // FOLLOWER
        Elev_2.follow(Elev_1);

        // INVERT
        Elev_1.setInverted(Constants.kElevator.E_1_INVERT);
        Elev_2.setInverted(Constants.kElevator.E_2_INVERT);

        // PIDs
        Elev_1.config_kF(0, 0, 10);
        Elev_1.config_kP(0, 0.08, 10);
        Elev_1.config_kI(0, 0.000028, 10);
        Elev_1.config_kD(0, 2.5, 10);
        Elev_1.configOpenloopRamp(Constants.kElevator.E_OPEN_RAMP_TIME, 10);
        Elev_1.configClosedloopRamp(Constants.kElevator.E_CLOSED_RAMP_TIME, 10);

        // ENCODER
        Elev_1.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        Elev_1.setSelectedSensorPosition(0, 0, 10);
        Elev_1.setSensorPhase(Constants.kElevator.E_ENC_INVERT);

        //		 RESET ENCODER ON LIMIT SWITCH DOWN
        Elev_1.configSetParameter(ParamEnum.eClearPosOnLimitF, 1, 0, 0, 10);

        if (isRemoteSensor) {
            //		 REMOTE LIMIT SWITCHES
            Elev_1.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                    LimitSwitchNormal.NormallyOpen, Elev_2.getDeviceID(), 10);
            Elev_1.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                    LimitSwitchNormal.NormallyOpen, Elev_2.getDeviceID(), 10);
        } else {
            // PRACTICE LIMIT SWITCHES
            Elev_1.configForwardLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
                    10);
            Elev_1.configReverseLimitSwitchSource(LimitSwitchSource.FeedbackConnector, LimitSwitchNormal.NormallyOpen,
                    10);
            Elev_2.configForwardLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                    LimitSwitchNormal.NormallyOpen, Elev_1.getDeviceID(), 10);
            Elev_2.configReverseLimitSwitchSource(RemoteLimitSwitchSource.RemoteTalonSRX,
                    LimitSwitchNormal.NormallyOpen, Elev_1.getDeviceID(), 10);

        }

        Elev_1.setName("Elev 1");
        Elev_2.setName("Elev 2");
    }

    public void generalTalonInit(WPI_TalonSRX talon) {
        // BRAKE
        Elev_1.setNeutralMode(NeutralMode.Brake);
        Elev_2.setNeutralMode(NeutralMode.Brake);

        //CURRENT LIMITING
        talon.configContinuousCurrentLimit(Constants.kElevator.AMP_LIMIT, 10);
        talon.configPeakCurrentLimit(Constants.kElevator.AMP_TRIGGER, 10);
        talon.configPeakCurrentDuration(Constants.kElevator.AMP_TIME, 10);

        talon.enableCurrentLimit(true);

        talon.setSubsystem("Elevator");
    }

    /**
     * update elevator modifier
     *
     * @author max
     * @since
     */
    public void setDriveLimit() {
        double pos = -Elev_1.getSelectedSensorPosition(0);

        if (m_overriden == false) {

            if (pos < 8500)
                Robot.drive.m_elev_modify = Constants.kElevator.SPEED_1;
            else if (pos < 12000 && pos > 8500)
                Robot.drive.m_elev_modify = Constants.kElevator.SPEED_2;
            else if (pos < 15000 && pos > 12000)
                Robot.drive.m_elev_modify = Constants.kElevator.SPEED_3;
            else if (pos < 25000 && pos > 15000)
                Robot.drive.m_elev_modify = Constants.kElevator.SPEED_4;
            else if (pos > 25000)
                Robot.drive.m_elev_modify = Constants.kElevator.SPEED_5;

        } else {
            Robot.drive.m_elev_modify = 1;
        }

    }

    /**
     * run to position
     *
     * @param position
     * @author max
     * @since
     */
    public void encoder(double position) {
        Elev_1.configPeakOutputForward(.45, 10);
        Elev_1.configPeakOutputReverse(-.8, 10);
        m_pos = -position * 4096;
        Elev_1.set(ControlMode.Position, m_pos);
    }

    /**
     * @param multiplier
     * @return boolean
     * @author max
     * @since
     */
    public boolean isDone(double multiplier) {
        return (Elev_1.getSelectedSensorPosition(0) < (m_pos + (102 * multiplier))
                && Elev_1.getSelectedSensorPosition(0) > (m_pos - (102 * multiplier)));
    }

    /**
     * set output to default set position target default
     *
     * @author max
     * @since
     */
    public void encoderDone() {
        Elev_1.set(ControlMode.PercentOutput, 0);
        Elev_1.configPeakOutputForward(1, 10);
        Elev_1.configPeakOutputReverse(-1, 10);
        m_pos = 0;
    }

    @Override
    public void initDefaultCommand() {
    }

    @Override
    public void setDisable(boolean toSetDisable) {
        for (WPI_TalonSRX controller : controllers)
            if (toSetDisable)
                controller.disable();
            else
                controller.set(0);
    }

    /**
     * Elevator speed limiting override
     *
     * @author max
     */
    public enum ESO {
        TOGGLE, ON, OFF
    }
}