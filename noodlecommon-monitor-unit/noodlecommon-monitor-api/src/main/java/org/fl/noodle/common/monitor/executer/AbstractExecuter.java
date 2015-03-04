package org.fl.noodle.common.monitor.executer;

public abstract class AbstractExecuter implements Executer {

	protected long initialDelay = 0;
	protected long delay = 0;
	
	@Override
	public long getInitialDelay() {
		return initialDelay;
	}

	public void setInitialDelay(long initialDelay) {
		this.initialDelay = initialDelay;
	}

	@Override
	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

}
