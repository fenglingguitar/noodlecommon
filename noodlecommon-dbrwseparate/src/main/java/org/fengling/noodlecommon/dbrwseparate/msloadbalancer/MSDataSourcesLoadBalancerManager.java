package org.fengling.noodlecommon.dbrwseparate.msloadbalancer;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceCheckAlive;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceCheckResult;

/**
 * 此类负责对后端的数据库做心跳检测，返回可用数据库列表，适用读写分离策略，检查规则具体查看HAproxy
 * weight  -- 调节服务器的负重
 * inter  -- 设置连续的两次健康检查之间的时间，单位为毫秒(ms)，默认值 2000(ms)
 * rise  -- 指定多少次连续成功的健康检查后，即可认定该服务器处于可操作状态，默认值 2
 * fall  -- 指定多少次不成功的健康检查后，认为服务器为当掉状态，默认值 3
 */
public class MSDataSourcesLoadBalancerManager {
	
	private final static Log logger = LogFactory.getLog(MSDataSourcesLoadBalancerManager.class);
		
	//取得所有注入的数据源.默认全部可用
	private static  Map<String,MSDataSourceModel> dataSourcesMap = new ConcurrentHashMap<String,MSDataSourceModel>();
	private static  Map<String,MSDataSourceModel> deadSourcesMap = new ConcurrentHashMap<String,MSDataSourceModel>();

	private static MSConfig config;

	private long interTime = 2000;
	private String detectingSql = null;
	private int totalFailureCount = 3;
	private int totalRiseCount = 2;
	
	private Map<String, DataSource> initDataSourceMap;

	public void init(){
		
		config = new MSConfig();
		config.setDetectingSql(detectingSql);
		config.setInterTime(interTime);
		config.setTotalFailureCount(totalFailureCount);
		config.setTotalRiseCount(totalRiseCount);
		
		// 添加初始化的数据源
		for (Map.Entry<String, DataSource> parm : initDataSourceMap.entrySet()) {
			MSDataSourceModel dataSourceModel = new MSDataSourceModel();
			dataSourceModel.setDataSource(parm.getValue());
			dataSourceModel.setDataSourceType(parm.getKey());
			dataSourcesMap.put(parm.getKey(), dataSourceModel);
		}
		 
	    //启动检测线程
	    Thread dataSourceBeatAlive= new Thread(new HeartBeatAliveTasksScan());
	    dataSourceBeatAlive.setDaemon(true);
	    dataSourceBeatAlive.setName("dataSourceHeartBeatAlive");
	    dataSourceBeatAlive.start();
	    
	    Thread dataSourceDeadHeartBeat= new Thread(new HeartBeatDeadTasksScan());
	    dataSourceDeadHeartBeat.setDaemon(true);
	    dataSourceDeadHeartBeat.setName("dataSourceDeadHeartBeat");
	    dataSourceDeadHeartBeat.start();
	    logger.info("Master Slave Data Sources Load Balancer Manager init ok");
	}
	
	
	public static List<MSDataSourceModel> getAliveDataSourceModeList(){
		
		List<MSDataSourceModel> list=null;
		
		if(dataSourcesMap!=null&&dataSourcesMap.size()>0){
			list=new ArrayList<MSDataSourceModel>(dataSourcesMap.size());
			for (Map.Entry<String,MSDataSourceModel> parm:dataSourcesMap.entrySet()){
				MSDataSourceModel node=parm.getValue();
				if(node.getFailureCount().get()==0){
				   list.add(node);
				}
			}	
		}
			
		return list;
	}
	
	public static MSDataSourceModel getAliveMSDataSource(){
		
		List<MSDataSourceModel> aliveList=getAliveDataSourceModeList();
		
		if(aliveList==null||aliveList.size()==0){
			return null;
		}

		Random rand =new Random(); 
		int randNum=rand.nextInt(aliveList.size());
		return aliveList.get(randNum);
	}
	
	class HeartBeatAliveTasksScan implements Runnable {

		public void run() {
			while (true) {
				try {
					for (Map.Entry<String, MSDataSourceModel> parm : dataSourcesMap.entrySet()) {
						MSDataSourceModel dataSourceModel = parm.getValue();
						String key = parm.getKey();
						DataSourceCheckResult status = DataSourceCheckAlive.CheckAliveDataSource(dataSourceModel.getDataSource(), config.getDetectingSql());

						if (!status.isAlive()) {// 检查失败
							AtomicInteger failureCount = dataSourceModel.getFailureCount();
							int count = failureCount.incrementAndGet();
							if (count > config.getTotalFailureCount()) {
								dataSourcesMap.remove(key);
								deadSourcesMap.put(key, dataSourceModel);
								logger.error(key + " dead and remove dataSourcesMap");
							} else {
								dataSourceModel.setFailureCount(new AtomicInteger(count));
								dataSourcesMap.put(key, dataSourceModel);
							}
						} else {// 检查成功
							AtomicInteger failureCount = dataSourceModel.getFailureCount();
							if (failureCount.get() > 0) {
								dataSourceModel.setFailureCount(new AtomicInteger(0));
								dataSourcesMap.put(key, dataSourceModel);
							}
						}
					}

					Thread.sleep(config.getInterTime());
					
				} catch (InterruptedException e) {
					logger.error("HeartBeatAliveTasksScan InterruptedException", e);
				}
			}
		}
	}

	class HeartBeatDeadTasksScan implements Runnable {

		public void run() {
			while (true) {
				try {
					for (Map.Entry<String, MSDataSourceModel> parm : deadSourcesMap.entrySet()) {
						MSDataSourceModel dataSourceModel = parm.getValue();
						String key = parm.getKey();
						DataSourceCheckResult status = DataSourceCheckAlive.CheckAliveDataSource(dataSourceModel.getDataSource(), config.getDetectingSql());
						if (status.isAlive()) {// 检查成功
							AtomicInteger riseCount = dataSourceModel.getRiseCount();
							int count = riseCount.incrementAndGet();
							if (count >= config.getTotalRiseCount()) {
								dataSourceModel.setRiseCount(new AtomicInteger(0));
								dataSourceModel.setFailureCount(new AtomicInteger(0));
								dataSourcesMap.put(key, dataSourceModel);// 添加活的池子
								deadSourcesMap.remove(key);// 删除死的池子
								logger.error(key + " alive and remove deadSourcesMap");
							} else {
								dataSourceModel.setRiseCount(new AtomicInteger(count));
								deadSourcesMap.put(key, dataSourceModel);
							}
						} else {// 检查失败
							dataSourceModel.setRiseCount(new AtomicInteger(0));
							deadSourcesMap.put(key, dataSourceModel);
						}
					}
					Thread.sleep(config.getInterTime());
				} catch (InterruptedException e) {
					logger.error("HeartBeatDeadTasksScan InterruptedException", e);
				}
			}
		}
	}

	public int getTotalFailureCount() {
		return totalFailureCount;
	}

	public void setTotalFailureCount(int totalFailureCount) {
		this.totalFailureCount = totalFailureCount;
	}

	public int getTotalRiseCount() {
		return totalRiseCount;
	}

	public void setTotalRiseCount(int totalRiseCount) {
		this.totalRiseCount = totalRiseCount;
	}

	public String getDetectingSql() {
		return detectingSql;
	}

	public void setDetectingSql(String detectingSql) {
		this.detectingSql = detectingSql;
	}

	public long getInterTime() {
		return interTime;
	}

	public void setInterTime(long interTime) {
		this.interTime = interTime;
	}

	public Map<String, DataSource> getInitDataSourceMap() {
		return initDataSourceMap;
	}

	public void setInitDataSourceMap(Map<String, DataSource> initDataSourceMap) {
		this.initDataSourceMap = initDataSourceMap;
	}
}
