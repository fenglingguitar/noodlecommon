package org.fl.noodle.common.monitor.executer;

public interface Executer {
	public void execute() throws Exception;
	public long getInitialDelay();
	public long getDelay();
}
