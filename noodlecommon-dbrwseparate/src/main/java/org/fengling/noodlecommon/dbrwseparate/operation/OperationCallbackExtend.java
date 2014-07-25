package org.fengling.noodlecommon.dbrwseparate.operation;

public interface OperationCallbackExtend<T> extends OperationCallback<T> {

	public boolean beforeExecuteActionCheck();
	public void beforeExecuteAction();
	
	public boolean beforeExecuteActionCheckInTransaction();
	public void beforeExecuteActionInTransaction();
	
	public void afterExecuteActionInTransaction(boolean isSuccess, T result, Exception e);
	public void afterExecuteAction(boolean isSuccess, T result, Exception e);
}
