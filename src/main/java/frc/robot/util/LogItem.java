package frc.robot.util;

import frc.robot.Constants;
import frc.robot.Robot;

public abstract class LogItem {
    private String mName = "";
    public String mValue = Constants.kFileManagement.DEFAULT_LOG_VALUE;

    public LogItem(String header) {
        this.mName = header;
        Robot.files.mLog.add(this);
    }

    public String getHeader() {
        return this.mName;
    }

    public String getValue() {
        return this.mValue;
    }

    public abstract void update();
}