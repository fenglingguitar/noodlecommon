package org.fl.noodle.common.connect.exception;

public class ConnectNoExistException extends RuntimeException {
	
	private static final long serialVersionUID = 7437477803669361359L;

	public ConnectNoExistException() {
		super();
	}
	
	public ConnectNoExistException(String message) {
		super(message);
	}
}
