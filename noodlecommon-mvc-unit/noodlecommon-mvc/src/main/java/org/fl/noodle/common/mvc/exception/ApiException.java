package org.fl.noodle.common.mvc.exception;

public class ApiException extends Exception {
	
	private static final long serialVersionUID = -2911566902700125265L;
	
	private String promptMessage;

	public ApiException(String promptMessage, String message) {
		super(message);
		this.promptMessage = promptMessage;
	}

	public String getPromptMessage() {
		return promptMessage;
	}
}
