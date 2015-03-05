package org.fl.noodle.common.test.press;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPressTool {
	
	private final static Logger logger = LoggerFactory.getLogger(AbstractPressTool.class);
	
	protected long planExecuteNum = 0;
	protected int threadNum = 0;
	protected long executeInterval = 1;
	protected long monitorInterval = 1000;
	protected boolean isPrintException = true;
	
	protected AtomicLong threadNumNow = new AtomicLong(0);
	
	private AtomicLong executeNumCount = new AtomicLong(0);
	private AtomicLong succeedNumCount = new AtomicLong(0);
	private AtomicLong failNumCount = new AtomicLong(0);
	private AtomicLong timeConsumeSumCount = new AtomicLong(0);
	
	private AtomicLong totalExecuteNumCount = new AtomicLong(0);
	private AtomicLong totalSucceedNumCount = new AtomicLong(0);
	private AtomicLong totalFailNumCount = new AtomicLong(0);
	private AtomicLong totalTimeConsumeCount = new AtomicLong(0);
	private AtomicLong totalTimeConsumeSumCount = new AtomicLong(0);
	
	protected AtomicLong planExecuteNumCount = new AtomicLong(0);
		
	public AbstractPressTool() {
		init();
	}
	
	public AbstractPressTool(long planExecuteNum) {
		this.planExecuteNum = planExecuteNum;
		init();
	}
	
	protected AbstractPressTool(long planExecuteNum, int threadNum) {
		this.planExecuteNum = planExecuteNum;
		this.threadNum = threadNum;
		init();
	}
	
	protected AbstractPressTool(long planExecuteNum, int threadNum, long executeInterval, long monitorInterval) {
		this.planExecuteNum = planExecuteNum;
		this.threadNum = threadNum;
		this.executeInterval = executeInterval;
		this.monitorInterval = monitorInterval;
		init();
	}
	
	private void init() {
		Thread monitorThread = new Thread(new MonitorRunnable());
		monitorThread.setDaemon(true);
		monitorThread.setName("MonitorRunnable");
		monitorThread.start();
	}
	
	protected Object doInvoke(Object proxy, final Method method, final Object[] args) {
		
		Object object = null;
		
		planExecuteNumCount.getAndIncrement();
		
		try {
			
			executeNumCount.incrementAndGet();
			long start = System.currentTimeMillis();
			
			object = method.invoke(proxy, args);
			
			timeConsumeSumCount.addAndGet(System.currentTimeMillis() - start);
			succeedNumCount.incrementAndGet();
			
		} catch (Exception e) {    		
			if (isPrintException && e instanceof InvocationTargetException) {
				logger.error("doInvoke -> method.invoke -> Exception:{}", ((InvocationTargetException)e).getTargetException().getMessage());
			}
			failNumCount.incrementAndGet();
		}
		
		return object;
	}
	
	protected class MonitorRunnable implements Runnable {

		@Override
		public void run() {
			
			long start = System.currentTimeMillis();
			
			while (planExecuteNum == 0 || planExecuteNumCount.get() < planExecuteNum) {
				
				try {
    				Thread.sleep(monitorInterval);
    			} catch (InterruptedException e) {
    				logger.error("execServiceMonitor -> Runnable -> Thread.sleep -> Exception:{}", e.getMessage());
    			}
				
				long executeNum = executeNumCount.getAndSet(0);
				long succeedNum = succeedNumCount.getAndSet(0);
				long failNum = failNumCount.getAndSet(0);
				long timeConsumeSum = timeConsumeSumCount.getAndSet(0);
				long end = System.currentTimeMillis();
				long timeConsume = end - start;
				start = end;
				
				long totalExecuteNum = totalExecuteNumCount.addAndGet(executeNum);
				long totalSucceedNum = totalSucceedNumCount.addAndGet(succeedNum);
				long totalFailNum = totalFailNumCount.addAndGet(failNum);
				long totalTimeConsume = totalTimeConsumeCount.addAndGet(timeConsume);
				long totalTimeConsumeSum = totalTimeConsumeSumCount.addAndGet(timeConsumeSum);
				
				StringBuilder stringBuilder = new StringBuilder();
				
				stringBuilder.append("Exec(Total):");
				stringBuilder.append(totalExecuteNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("Succ(Total):");
				stringBuilder.append(totalSucceedNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("Fail(Total):");
				stringBuilder.append(totalFailNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("ErrRate(Total):");
				stringBuilder.append(String.format("%.2f", totalFailNum * 1.0 / (totalExecuteNum > 0 ? totalExecuteNum : 1) * 100));
				stringBuilder.append("%");
				
				stringBuilder.append(",");
				stringBuilder.append("TPS:");
				stringBuilder.append((long)Math.floor(succeedNum * 1.0 / timeConsume * 1000));
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("AvgTime:");
				stringBuilder.append(String.format("%.2f", timeConsumeSum * 1.0 / (succeedNum > 0 ? succeedNum : 1)));
				stringBuilder.append("ms");
				
				stringBuilder.append(",");
				stringBuilder.append("Exec:");
				stringBuilder.append(executeNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("Succ:");
				stringBuilder.append(succeedNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("Fail:");
				stringBuilder.append(failNum);
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("ErrRate:");
				stringBuilder.append(String.format("%.2f", failNum * 1.0 / (executeNum > 0 ? executeNum : 1) * 100));
				stringBuilder.append("%");
				
				stringBuilder.append(",");
				stringBuilder.append("TimeConsume:");
				stringBuilder.append(timeConsume);
				stringBuilder.append("ms");
				
				stringBuilder.append(",");
				stringBuilder.append("TPM(Total):");
				stringBuilder.append((long)((Math.floor(totalSucceedNum / ((totalTimeConsume > 0 ? totalTimeConsume : 1) * 1.0 / 60000)))));
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("TPS(Total):");
				stringBuilder.append((long)((Math.floor(totalSucceedNum * 1.0 / (totalTimeConsume > 0 ? totalTimeConsume : 1) * 1000))));
				stringBuilder.append("n");
				
				stringBuilder.append(",");
				stringBuilder.append("AvgTime(Total):");
				stringBuilder.append(String.format("%.2f", totalTimeConsumeSum * 1.0 / (totalSucceedNum > 0 ? totalSucceedNum : 1)));
				stringBuilder.append("ms");
				
				if (planExecuteNum > 0) {
					stringBuilder.append(",");
					stringBuilder.append("PlanExec:");
					stringBuilder.append(planExecuteNum < Long.MAX_VALUE ? planExecuteNum : "limitless");
					stringBuilder.append("n");
				}
				
				if (threadNum > 0) {
					stringBuilder.append(",");
					stringBuilder.append("Thread:");
					stringBuilder.append(threadNumNow);
					stringBuilder.append("n");
				}
				
				stringBuilder.append(",");
				stringBuilder.append("TimeConsume(Total)(S):");
				stringBuilder.append(String.format("%.2f", totalTimeConsume * 1.0 / 1000));
				stringBuilder.append("s");
				
				stringBuilder.append(",");
				stringBuilder.append("TimeConsume(Total)(M):");
				stringBuilder.append(String.format("%.2f", totalTimeConsume * 1.0 / 60000));
				stringBuilder.append("m");
				
				logger.info(stringBuilder.toString());
			}
		}		
	}

	public void setPlanExecuteNum(long planExecuteNum) {
		this.planExecuteNum = planExecuteNum;
	}

	public void setThreadNum(int threadNum) {
		this.threadNum = threadNum;
	}

	public void setExecuteInterval(long executeInterval) {
		this.executeInterval = executeInterval;
	}

	public void setMonitorInterval(long monitorInterval) {
		this.monitorInterval = monitorInterval;
	}

	public void setIsPrintException(boolean isPrintException) {
		this.isPrintException = isPrintException;
	}
}
