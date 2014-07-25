package org.fengling.noodlecommon.dbrwseparate.operation;

public interface OperationCallback<T> {
    public T executeAction() throws OperationException, Exception;
}
