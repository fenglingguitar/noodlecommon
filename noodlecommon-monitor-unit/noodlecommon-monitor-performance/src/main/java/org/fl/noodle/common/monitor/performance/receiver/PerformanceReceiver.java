package org.fl.noodle.common.monitor.performance.receiver;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.fl.noodle.common.monitor.performance.net.UdpServer;
import org.fl.noodle.common.monitor.performance.persistence.PerformancePersistence;
import org.fl.noodle.common.monitor.performance.vo.InfoVo;
import org.fl.noodle.common.monitor.performance.vo.KeyVo;
import org.fl.noodle.common.monitor.performance.vo.NetVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.fl.noodle.common.util.json.JsonTranslator;
import org.fl.noodle.common.util.thread.Stopping;

public class PerformanceReceiver {
	
	private final static Logger logger = LoggerFactory.getLogger(PerformanceReceiver.class);
	
	private PerformancePersistence performancePersistence;
	
	private UdpServer udpServer;
	
	private ExecutorService executorServiceDeal;
	private volatile boolean stopSign = false;
	private Stopping stopping = new Stopping();
	
	private int cacheSize = 100;
	private int threadCountDeal = 1;
	
	public void start() {
		
		stopping.stopInit(threadCountDeal);
		
		executorServiceDeal = Executors.newFixedThreadPool(threadCountDeal);
		
		for (int i=0; i<threadCountDeal; i++) {
			executorServiceDeal.execute(new Runnable() {
				@Override
				public void run() {
					List<String> recvStrList = new ArrayList<String>(cacheSize);
					while (true) {
						String recvStr = udpServer.get();
						if (recvStr != null) {
							recvStrList.add(recvStr);
							if (recvStrList.size() == cacheSize) {
								save(recvStrList);
							}
						} else {
							if (recvStrList.size() > 0) {
								save(recvStrList);
							}
						}
						if (stopSign) {
							if (recvStrList.size() > 0) {
								save(recvStrList);
							}
							stopping.stopDo();
							break;
						}
					}
				}
			});
		}
	}
	
	public void destroy() throws Exception {
		stopSign = true;
		stopping.stopWait(10000);
		executorServiceDeal.shutdown();
	}
	
	private void save(final List<String> recvStrList) {
		
		for (String recvStr : recvStrList) {
			
			if (logger.isDebugEnabled()) {
				logger.debug("save -> save a recv -> {}", recvStr);
			}
			
			NetVo netVo = null;
			
			try {
				netVo = JsonTranslator.fromString(recvStr, NetVo.class);
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("save -> JsonTranslator.fromString -> {} -> Exception:{}", recvStr, e.getMessage());
				}
			}
			
			if (netVo == null) {
				continue;
			}
			
			KeyVo keyVo = netVo.getKeyVo();
			Object infoVo = netVo.getInfoVo();

			try {
				performancePersistence.insert(keyVo.toKeyString(), ((InfoVo)infoVo).getTimestamp(), infoVo);
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("save -> performancePersistence.insert -> {} -> Exception:{}", keyVo, e.getMessage());
				}
			}
		}
		recvStrList.clear();
	}

	public void setPerformancePersistence(PerformancePersistence performancePersistence) {
		this.performancePersistence = performancePersistence;
	}

	public void setUdpServer(UdpServer udpServer) {
		this.udpServer = udpServer;
	}

	public void setCacheSize(int cacheSize) {
		this.cacheSize = cacheSize;
	}
	
	public void setThreadCountDeal(int threadCountDeal) {
		this.threadCountDeal = threadCountDeal;
	}
}
