package org.fl.noodle.common.dbseparate.operation;

public interface OperationCallback<T> {
    public T executeAction() throws OperationException, Exception;
}
