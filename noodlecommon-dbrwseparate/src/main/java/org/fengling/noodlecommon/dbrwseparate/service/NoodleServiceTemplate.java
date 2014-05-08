package org.fengling.noodlecommon.dbrwseparate.service;

public interface NoodleServiceTemplate {

	<T> T execute(NoodleServiceCallback<T> action) throws NoodleServiceException, Exception;
	<T> T executeWithoutTransaction(NoodleServiceCallback<T> action) throws NoodleServiceException, Exception;
}
