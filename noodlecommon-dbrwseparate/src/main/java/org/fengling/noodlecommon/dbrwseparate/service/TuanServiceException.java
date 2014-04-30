
package org.fengling.noodlecommon.dbrwseparate.service;

public class TuanServiceException extends RuntimeException {

	private static final long serialVersionUID = 6635209562022209560L;

	private int errorCode;

    public TuanServiceException(int errorCode) {
        this.errorCode = errorCode;
    }

    public TuanServiceException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public TuanServiceException(int errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    public TuanServiceException(int errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
