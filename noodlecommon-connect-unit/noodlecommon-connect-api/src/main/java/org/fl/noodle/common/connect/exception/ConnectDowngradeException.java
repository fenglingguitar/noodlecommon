package org.fl.noodle.common.connect.exception;

public class ConnectDowngradeException extends RuntimeException {
	
	private static final long serialVersionUID = -2706777719333436881L;

	public ConnectDowngradeException() {
		super();
	}
	
	public ConnectDowngradeException(String message) {
		super(message);
	}
}
