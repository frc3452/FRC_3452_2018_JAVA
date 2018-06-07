package team3452.robot.commands.playback;

import edu.wpi.first.wpilibj.command.Command;
import team3452.robot.Robot;
import team3452.robot.subsystems.Playback.STATE;
import team3452.robot.subsystems.Playback.TASK;

public class PlaybackControl extends Command {

    private TASK m_task;
    private String m_name, m_folder;
    private boolean m_usb;

    /**
     * Command version of playback control
     *
     * @param name
     * @param usb
     * @param task
     * @author macco
     * @see PlaybackControl
     * @see TASK
     */
    public PlaybackControl(String name, String folder, boolean usb, TASK task) {
        m_task = task;
        m_name = name;
        m_folder = folder;
        m_usb = usb;
    }

    @Override
    protected void initialize() {
        Robot.playback.control(m_name, m_folder, m_usb, m_task, STATE.STARTUP);
    }

    @Override
    protected void execute() {
        Robot.playback.control(m_name, m_folder, m_usb, m_task, STATE.RUNTIME);
    }

    @Override
    protected boolean isFinished() {
        switch (m_task) {
            case Log:
                return false;
            case Parse:
                return true;
            case Record:
                return false;
            default:
                return false;
        }
    }

    @Override
    protected void end() {
        Robot.playback.control(m_name, m_folder, m_usb, m_task, STATE.FINISH);
    }

    @Override
    protected void interrupted() {
        end();
    }
}
