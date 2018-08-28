package org.usfirst.frc.team3452.robot.util;

import org.usfirst.frc.team3452.robot.Constants.kFileManagement;

public class  GZLogItem {

	private String mName = "";
	private String mValue = kFileManagement.DEFAULT_LOG_VALUE;

	public GZLogItem(String header) {
		this.mName = header;
	}

	public String getHeader() {
		return mName;
	}

	public void setValue(String value) {
		this.mValue = value;
	}

	public String getValue() {
		return mValue;
	}

}
