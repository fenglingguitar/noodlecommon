package org.fl.noodle.common.test.press;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.util.thread.Stopping;

public abstract class AbstractPressToolClient extends AbstractPressTool {

	private final static Logger logger = LoggerFactory.getLogger(AbstractPressToolClient.class);
	
	protected Stopping stopping = new Stopping();
	
	protected AbstractPressToolClient() {
	}
	
	protected AbstractPressToolClient(long planExecuteNum) {
		super(planExecuteNum);
	}
	
	protected AbstractPressToolClient(long planExecuteNum, int threadNum) {
		super(planExecuteNum, threadNum);
	}
	
	protected AbstractPressToolClient(long planExecuteNum, int threadNum, long executeInterval, long monitorInterval) {
		super(planExecuteNum, threadNum, executeInterval, monitorInterval);
	}
	
	protected Object requestInvoke(final Object proxy, final Method method, final Object[] args) {
		
		stopping.stopInit(threadNum);
		
        ExecutorService execService = Executors.newCachedThreadPool();
        
    	for (int i=0; i<threadNum; i++) {
        	
        	execService.execute(new Runnable() {

				@Override
				public void run() {
					
					while (planExecuteNumCount.get() < planExecuteNum) {
						
						doInvoke(proxy, method, args);
						
						if (executeInterval > 0) {
							try {
								Thread.sleep(executeInterval);
							} catch (InterruptedException e) {
								logger.error("requestInvoke -> doInvoke -> Thread.sleep -> Exception:{}", e.getMessage());
							}
						}
					}
					
					stopping.stopDo();
				}
        	});
        	
        	threadNumNow.incrementAndGet();
        	
        	try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				logger.error("requestInvoke -> execService.execute -> Thread.sleep -> Exception:{}", e.getMessage());
			}
        }
    	
    	stopping.stopWait(Long.MAX_VALUE);
    	
    	execService.shutdown();
    	
    	try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			logger.error("requestInvoke -> Thread.sleep -> Exception:{}", e.getMessage());
		}
    	
		return null;
	}
}
