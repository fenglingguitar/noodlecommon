package org.fl.noodle.common.connect.exception;

public class ConnectStopException extends RuntimeException {
	
	private static final long serialVersionUID = 4812488884550714187L;

	public ConnectStopException() {
		super();
	}
	
	public ConnectStopException(String message) {
		super(message);
	}
}
