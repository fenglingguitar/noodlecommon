package org.fengling.noodlecommon.dbrwseparate.datasource;

public class DataSourceCheckResult {
	
	private boolean isAlive = true;

	private String errorCode;

	public DataSourceCheckResult(boolean isAlive, String errorCode) {
		this.isAlive = isAlive;
		this.errorCode = errorCode;
	}

	public boolean isAlive() {
		return isAlive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}
