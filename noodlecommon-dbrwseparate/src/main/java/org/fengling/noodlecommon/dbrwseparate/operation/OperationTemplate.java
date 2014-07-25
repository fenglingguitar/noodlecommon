package org.fengling.noodlecommon.dbrwseparate.operation;

public interface OperationTemplate {

	<T> T execute(OperationCallback<T> action) throws OperationException, Exception;
	<T> T executeWithoutTransaction(OperationCallback<T> action) throws OperationException, Exception;
}
