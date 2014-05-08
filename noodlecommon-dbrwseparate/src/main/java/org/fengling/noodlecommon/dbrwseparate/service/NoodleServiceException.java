package org.fengling.noodlecommon.dbrwseparate.service;

public class NoodleServiceException extends RuntimeException {

	private static final long serialVersionUID = 6635209562022209560L;

	public NoodleServiceException(Throwable root) {
		super(root);
	}

	public NoodleServiceException(String string, Throwable root) {
		super(string, root);
	}

	public NoodleServiceException(String s) {
		super(s);
	}
}
