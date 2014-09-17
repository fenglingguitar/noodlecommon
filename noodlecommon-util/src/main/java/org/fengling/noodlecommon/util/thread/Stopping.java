package org.fengling.noodlecommon.util.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Stopping {
	
	private CountDownLatch stopCountDownLatch;
	
	public void stopInit(int num) {
		stopCountDownLatch = new CountDownLatch(num);
	}
	
	public void stopDo() {
		stopCountDownLatch.countDown();
	}
	
	public boolean stopWait(long timeout) {
		try {
			return stopCountDownLatch.await(timeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			return false;
		}
	}
}
