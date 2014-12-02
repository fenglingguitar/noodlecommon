package org.fl.noodle.common.distributedlock.api;

public interface DistributedLock {
	public boolean getStatus();
	public boolean waitLocker();
	public void releaseLocker();
}
