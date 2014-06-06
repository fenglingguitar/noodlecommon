package org.fengling.noodlecommon.mvc.exception;

public class JsonException extends Exception {
	
	private static final long serialVersionUID = -2911566902700125265L;
	
	private String promptMessage;

	public JsonException(String promptMessage, String message) {
		super(message);
		this.promptMessage = promptMessage;
	}

	public String getPromptMessage() {
		return promptMessage;
	}
}
