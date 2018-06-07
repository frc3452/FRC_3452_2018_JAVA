package org.usfirst.frc.team3452.robot.commands.drive;

import edu.wpi.first.wpilibj.command.Command;
import org.usfirst.frc.team3452.robot.Robot;
import org.usfirst.frc.team3452.robot.subsystems.Drivetrain;

public class SmoothMooth extends Command {

    private double m_left, m_right, m_accel, g_accel, m_speed, straight_percent, m_heading, k;

    private double c_p, l_adjust, r_adjust, c_gyro;

    /**
     * Command for integrating encoderDrive with smoother arcs
     *
     * @param left
     * @param right
     * @param accel
     * @param lessaccel
     * @param speed
     * @param percentForStraight
     * @param heading
     * @param constant
     * @author macco
     * @see Drivetrain
     */
    public SmoothMooth(double left, double right, double accel, double lessaccel, double speed,
                       double percentForStraight, double heading, double constant) {
        requires(Robot.drive);

        m_left = left;
        m_right = right;
        m_accel = accel;
        g_accel = lessaccel;
        m_speed = speed;
        straight_percent = percentForStraight;
        m_heading = heading;
        l_adjust = 0;
        r_adjust = 0;
        k = constant;
    }

    protected void initialize() {
        setTimeout(8);

        c_gyro = Robot.drive.Gyro.getAngle();
    }

    protected void execute() {
        c_p = Robot.drive.p_pos;

        //after half way through, adjust acceleration for side traveling farther to slow down quicker
        if (c_p > .5) {
            if (m_left > m_right) {
                Robot.drive.motionMagic(m_left, m_right, g_accel, m_accel, m_speed * l_adjust, m_speed * r_adjust);
            } else if (m_right > m_left) {
                Robot.drive.motionMagic(m_left, m_right, m_accel, g_accel, m_speed * l_adjust, m_speed * r_adjust);
            } else if (m_right == m_left) {
                Robot.drive.motionMagic(m_left, m_right, g_accel, g_accel, m_speed * l_adjust, m_speed * r_adjust);
            }
        } else {
            Robot.drive.motionMagic(m_left, m_right, m_accel, m_accel, m_speed * l_adjust, m_speed * r_adjust);
        }

        //keep straight until turn at _ percent
        if (c_p < straight_percent) {

            if (c_gyro < m_heading + .4 && c_gyro > m_heading - .4) {
                l_adjust = 1;
                r_adjust = 1;

            } else if (c_gyro > m_heading) {
                l_adjust = (1 - (k * Math.abs(c_gyro - m_heading)));
                r_adjust = 1;
            } else if (c_gyro < m_heading) {
                r_adjust = (1 - (k * Math.abs(c_gyro - m_heading)));
                l_adjust = 1;
            }
        } else {
            r_adjust = 1;
            l_adjust = 1;
        }
    }

    protected boolean isFinished() {
        return Robot.drive.encoderIsDone(1.5) || isTimedOut();
    }

    protected void end() {
        Robot.drive.encoderDone();
    }

    protected void interrupted() {
        end();
    }
}
