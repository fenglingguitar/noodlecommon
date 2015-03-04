package org.fl.noodle.common.connect.exception;

public class ConnectNoAliveException extends RuntimeException {
	
	private static final long serialVersionUID = 7109238735967871952L;

	public ConnectNoAliveException() {
		super();
	}
	
	public ConnectNoAliveException(String message) {
		super(message);
	}
}
