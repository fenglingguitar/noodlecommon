package org.fengling.noodlecommon.distributedlock.api;

public interface DistributedLock {
	public boolean getStatus();
	public boolean waitLocker();
	public void releaseLocker();
}
