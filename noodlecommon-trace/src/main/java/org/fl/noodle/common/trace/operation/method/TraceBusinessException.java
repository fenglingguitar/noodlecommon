package org.fl.noodle.common.trace.operation.method;

public class TraceBusinessException extends RuntimeException {

	private static final long serialVersionUID = 7585653059401640505L;
	
	public TraceBusinessException(Throwable cause) {
		super(cause);
	}

	public TraceBusinessException(String pmessage) {
		super(pmessage);
	}
}
