package org.fengling.noodlecommon.mvc.exception;

public class ViewException extends Exception {
	
	private static final long serialVersionUID = -2911566902700125265L;
	
	private String promptMessage;

	public ViewException(String promptMessage) {
		this.promptMessage = promptMessage;
	}
	
	public ViewException(String promptMessage, String message) {
		super(message);
		this.promptMessage = promptMessage;
	}

	public String getPromptMessage() {
		return promptMessage;
	}
}
