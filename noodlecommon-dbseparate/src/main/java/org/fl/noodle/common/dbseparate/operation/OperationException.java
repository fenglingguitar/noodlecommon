package org.fl.noodle.common.dbseparate.operation;

public class OperationException extends RuntimeException {

	private static final long serialVersionUID = 6635209562022209560L;

	public OperationException(Throwable root) {
		super(root);
	}

	public OperationException(String string, Throwable root) {
		super(string, root);
	}

	public OperationException(String s) {
		super(s);
	}
}
