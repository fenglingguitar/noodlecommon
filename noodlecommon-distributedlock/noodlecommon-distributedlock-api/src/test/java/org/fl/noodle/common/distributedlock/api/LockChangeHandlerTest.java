package org.fl.noodle.common.distributedlock.api;

import org.fl.noodle.common.distributedlock.api.LockChangeHandler;

public class LockChangeHandlerTest implements LockChangeHandler {

	private String lockName;
	
	@Override
	public void onMessageGetLock() {
		System.out.println("Lock: " + lockName + " -> LockChangeHandler -> onMessageGetLock");
	}

	@Override
	public void onMessageLossLock() {
		System.out.println("Lock: " + lockName + " -> LockChangeHandler -> onMessageLossLock");
	}

	@Override
	public void onMessageReleaseLock() {
		System.out.println("Lock: " + lockName + " -> LockChangeHandler -> onMessageReleaseLock");
	}

	@Override
	public void onMessageStart() {
		System.out.println("Lock: " + lockName + " -> LockChangeHandler -> onMessageStart");
	}

	@Override
	public void onMessageStop() {
		System.out.println("Lock: " + lockName + " -> LockChangeHandler -> onMessageStop");
	}

	public void setLockName(String lockName) {
		this.lockName = lockName;
	}
}
