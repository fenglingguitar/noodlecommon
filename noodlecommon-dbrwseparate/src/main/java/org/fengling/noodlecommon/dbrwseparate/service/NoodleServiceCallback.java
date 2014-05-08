package org.fengling.noodlecommon.dbrwseparate.service;

public interface NoodleServiceCallback<T> {
    public T executeAction() throws NoodleServiceException, Exception;
}
