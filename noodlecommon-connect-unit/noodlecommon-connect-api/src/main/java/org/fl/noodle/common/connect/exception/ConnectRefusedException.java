package org.fl.noodle.common.connect.exception;

public class ConnectRefusedException extends RuntimeException {
	
	private static final long serialVersionUID = -5824236696037864527L;

	public ConnectRefusedException() {
		super();
	}
	
	public ConnectRefusedException(String message) {
		super(message);
	}
}
