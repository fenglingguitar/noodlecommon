package org.fl.noodle.common.distributedlock.api;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class AbstractDistributedLock implements DistributedLock {

	private final static Log logger = LogFactory.getLog(AbstractDistributedLock.class);
	
	private ExecutorService executorService = Executors.newSingleThreadExecutor();
	
	private volatile boolean stopSign = false;
	
	private AtomicBoolean status = new AtomicBoolean(false);
	
	private CountDownLatch stopCountDownLatch;
	
	protected long diffTime = 0;
	
	protected long sleepTimeGetAlive = 10000;
	protected long sleepTimeKeepAlive = 10000;
	protected long delayTime = 3000;
	protected long intervalTime = sleepTimeKeepAlive + delayTime;
	protected long timeSyncRate = 100;
	
	private LockChangeHandler lockChangeHandler;

	private Object sleepObject = new Object();
	
	public void start() throws Exception {
		
		init();
		
		stopInit();
		
		timeSync(false);
				
		executorService.execute(new Runnable() {
			@Override
			public void run() {
				doStart();
				while (true) {
					if (!doKeepAlive()) {
						status.set(false);
						while(!doGetAlive()) {
							startSleep(sleepTimeGetAlive);
							if (stopSign) {
								doStop();
								return;
							}
						}
						if (stopSign) {
							doStop();
							return;
						}
					} 
					status.set(true);
					notifyAllForLocker();
					startSleep(sleepTimeKeepAlive);
					if (stopSign) {
						doStop();
						return;
					}
				}
			}
		});
	}
	
	public void destroy() {	
		stopSign = true;		
		notifySleep();
		stopWait();
		executorService.shutdown();
		releaseLocker();
		notifyAllForLocker();
	}
	
	@Override
	public boolean getStatus() {
		return status.get();
	}

	@Override
	public boolean waitLocker() {
		if (status.get() == false && !stopSign) {
			waitForLocker();
			return status.get();
		}
		return false;
	}
	
	private synchronized void waitForLocker() {
		if (!stopSign) {
			try {
				wait();
			} catch (InterruptedException e) {
				if (logger.isErrorEnabled()) {
					logger.error("WaitForLocker -> Wait -> " + e);
				}
			}
		}
	}
	
	private synchronized void notifyAllForLocker() {
		notifyAll();
	}
	
	@Override
	public void releaseLocker() {
		doReleaseAlive();
	}
	
	private void startSleep(long suspendTime){
		if (!stopSign && suspendTime > 0) {			
			synchronized(sleepObject) {
				try {
					sleepObject.wait(suspendTime);
				} catch (InterruptedException e) {
					if (logger.isErrorEnabled()) {
						logger.error("startSleep -> sleepObject wait -> InterruptedException: " + e);
					}
				}
			}
		}
	}
	
	private synchronized void notifySleep() {
		synchronized(sleepObject) {
			sleepObject.notifyAll();
		}
	}
	
	private void stopInit() {
		stopCountDownLatch = new CountDownLatch(1);
	}
	
	private void stopDo() {
		stopCountDownLatch.countDown();
	}
	
	private void stopWait() {
		try {
			stopCountDownLatch.await();
		} catch (InterruptedException e) {
			if (logger.isErrorEnabled()) {
				logger.error("stopWait -> stopCountDownLatch await -> InterruptedException: " + e);
			}
		}
	}
	
	private void timeSync(boolean isRandom) {
		if (isRandom) {
			if ((int) Math.round(Math.random() * timeSyncRate) == timeSyncRate) {
				diffTime = getDiffTime();			
			}			
		} else {	
			diffTime = getDiffTime();		
		}
	}
	
	private boolean doGetAlive() {
		timeSync(true);
		if (getAlive()) {
			if (lockChangeHandler != null) {
				lockChangeHandler.onMessageGetLock();				
			}
			return true;
		}
		return false;
	}
	
	private boolean doKeepAlive() {
		timeSync(true);
		if (keepAlive()) {
			return true;
		}
		if (lockChangeHandler != null) {
			lockChangeHandler.onMessageLossLock();			
		}
		return false;
	}
	
	private boolean doReleaseAlive() {
		status.set(false);
		boolean result = releaseAlive();
		if (lockChangeHandler != null) {
			lockChangeHandler.onMessageReleaseLock();	
		}
		return result;
	}
	
	private void doStart() {
		lockChangeHandler.onMessageStart();
	}

	private void doStop() {
		lockChangeHandler.onMessageStop();
		stopDo();
	}
	
	protected abstract void init() throws Exception;
	protected abstract long getDiffTime();
	protected abstract boolean getAlive();
	protected abstract boolean keepAlive();
	protected abstract boolean releaseAlive();

	public void setSleepTimeGetAlive(long sleepTimeGetAlive) {
		this.sleepTimeGetAlive = sleepTimeGetAlive;
	}

	public void setSleepTimeKeepAlive(long sleepTimeKeepAlive) {
		this.sleepTimeKeepAlive = sleepTimeKeepAlive;
	}

	public void setDelayTime(long delayTime) {
		this.delayTime = delayTime;
		intervalTime = sleepTimeKeepAlive + delayTime;
	}
	
	public void setTimeSyncRate(long timeSyncRate) {
		this.timeSyncRate = timeSyncRate;
	}

	public void setLockChangeHandler(LockChangeHandler lockChangeHandler) {
		this.lockChangeHandler = lockChangeHandler;
	}
}
