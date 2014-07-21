package org.fengling.noodlecommon.distributedlock.api;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class LockChangeHandlerTest implements LockChangeHandler {

	private final static Log logger = LogFactory.getLog(LockChangeHandlerTest.class);
	
	private String lockName;
	
	@Override
	public void onMessageGetLock() {
		logger.info("Lock: " + lockName + " -> LockChangeHandler -> onMessageGetLock");
	}

	@Override
	public void onMessageLossLock() {
		logger.info("Lock: " + lockName + " -> LockChangeHandler -> onMessageLossLock");
	}

	@Override
	public void onMessageReleaseLock() {
		logger.info("Lock: " + lockName + " -> LockChangeHandler -> onMessageReleaseLock");
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
}
