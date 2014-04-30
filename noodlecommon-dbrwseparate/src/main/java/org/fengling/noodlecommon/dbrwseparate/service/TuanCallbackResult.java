
package org.fengling.noodlecommon.dbrwseparate.service;


public class TuanCallbackResult {
	
	public final static int SUCCESS = 1;
	public final static int FAILURE = -1;

	private int statusCode = SUCCESS;
	private int resultCode = 0;

	private Throwable throwable;
	private Object businessObject;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public Object getBusinessObject() {
        return businessObject;
    }

    public void setBusinessObject(Object businessObject) {
        this.businessObject = businessObject;
    }

    /**
     * 禁止用户直接构造该对象，必须通过业务方法构造
     */
    private TuanCallbackResult(int statusCode, int resultCode, Throwable throwable, Object businessObject) {
        this.statusCode = statusCode;
        this.resultCode = resultCode;
        this.throwable = throwable;
        this.businessObject = businessObject;
    }

    //------------------ 辅助方法 --------------------------------------

    /**
     * 直接构造成功状况下的回调结果对象
     */
    public static TuanCallbackResult success() {
        return success(0, null);
    }

    /**
     * 直接构造成功状况下的回调结果对象，同时设置业务代码
     */
    public static TuanCallbackResult success(int resultCode) {
        return success(resultCode, null);
    }

    /**
     * 直接构造成功状况下的回调结果对象，同时设置业务代码、业务对象
     */
    public static TuanCallbackResult success(int resultCode, Object businessObject) {
        return new TuanCallbackResult(SUCCESS, resultCode, null, businessObject);
    }

    /**
     * 直接构造失败状况下的回调结果对象，同时设置业务代码
     */
    public static TuanCallbackResult failure(int resultCode) {
        return failure(resultCode, null, null);
    }

    /**
     * 直接构造失败状况下的回调结果对象，同时设置业务代码、异常
     */
    public static TuanCallbackResult failure(int resultCode, Throwable throwable) {
        return failure(resultCode, throwable, null);
    }

    /**
     * 直接构造失败状况下的回调结果对象，同时设置业务代码、异常、业务对象
     */
    public static TuanCallbackResult failure(int resultCode, Throwable throwable,Object businessObject) {
        return new TuanCallbackResult(FAILURE, resultCode, throwable, businessObject);
    }

    /**
     * 检查服务处理是否成功
     */
    public boolean isSuccess() {
        return statusCode == SUCCESS;
    }

    /**
     * 检查服务处理是否失败
     */
    public boolean isFailure() {
        return statusCode == FAILURE;
    }

    /**
     * 检查业务对象是否为NULL，以便于外部方法判断是否进行其它处理
     */
    public boolean isNullBusinessObject() {
        return null == this.businessObject;
    }
}
