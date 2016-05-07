package org.fl.noodle.common.dbseparate.loadbalancer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.sql.DataSource;

import org.fl.noodle.common.dbseparate.datasource.DataSourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadBalancerManagerImpl implements LoadBalancerManager {
	
	private final static Logger logger = LoggerFactory.getLogger(LoadBalancerManagerImpl.class);
		
	private CopyOnWriteArrayList<DataSourceModel> aliveSourcesList = new CopyOnWriteArrayList<DataSourceModel>();
	private CopyOnWriteArrayList<DataSourceModel> deadSourcesList = new CopyOnWriteArrayList<DataSourceModel>();

	private CopyOnWriteArrayList<DataSourceModel> dataSourcesSelectList = new CopyOnWriteArrayList<DataSourceModel>();
	private Map<String, DataSourceModel> dataSourcesMap = new ConcurrentHashMap<String, DataSourceModel>();
	private Map<String, Integer> weightMap = new ConcurrentHashMap<String, Integer>();
	
	private int totalFailureCount = 3;
	private int totalRiseCount = 2;
	private long interTime = 2000;
	private String detectingSql = "SELECT CURRENT_TIMESTAMP FROM DUAL";
	
	private Map<String, DataSource> initDataSourceMap;
	private Map<String, Integer> initWeightMap;

	public void start() {
		
		if (initWeightMap != null) {			
			for (Map.Entry<String, Integer> entry : initWeightMap.entrySet()) {
				weightMap.put(entry.getKey(), entry.getValue());
			}
		}

		for (Map.Entry<String, DataSource> entry : initDataSourceMap.entrySet()) {
			DataSourceModel dataSourceModel = new DataSourceModel();
			dataSourceModel.setDataSource(entry.getValue());
			dataSourceModel.setDataSourceType(entry.getKey());
			aliveSourcesList.add(dataSourceModel);
			dataSourcesMap.put(entry.getKey(), dataSourceModel);
			addDataSourcesSelectList(dataSourceModel);
		}
		
		Thread dataSourceBeatAlive = new Thread(new HeartBeatAliveTasksScan());
		dataSourceBeatAlive.setDaemon(true);
		dataSourceBeatAlive.setName("dataSourceHeartBeatAlive");
		dataSourceBeatAlive.start();

		Thread dataSourceDeadHeartBeat = new Thread(new HeartBeatDeadTasksScan());
		dataSourceDeadHeartBeat.setDaemon(true);
		dataSourceDeadHeartBeat.setName("dataSourceDeadHeartBeat");
		dataSourceDeadHeartBeat.start();
	}
	
	@Override
	public DataSourceType getAliveDataSource() {
		return DataSourceType.checkType(getAliveDataSourceFromList(dataSourcesSelectList).getDataSourceType());
	}
	
	@Override
	public DataSourceType getOtherAliveDataSource(List<DataSourceType> dataSourceTypeList) {
		
		List<DataSourceModel> otherDataSourcesSelectList = new LinkedList<DataSourceModel>();
		otherDataSourcesSelectList.addAll(dataSourcesSelectList);
		for (DataSourceType dataSourceTypeIt : dataSourceTypeList) {
			while(otherDataSourcesSelectList.remove(dataSourcesMap.get(dataSourceTypeIt.typeName())));
		}
		
		return DataSourceType.checkType(getAliveDataSourceFromList(otherDataSourcesSelectList).getDataSourceType());
	}
	
	@Override
	public boolean checkIsAliveDataSource(DataSourceType dataSourceType) {
		for (DataSourceModel dataSourceModel : aliveSourcesList) {
			if (dataSourceModel.getDataSourceType().equals(dataSourceType.typeName())) {
				return true;
			}
		}
		return false;
	}
	
	private DataSourceModel getAliveDataSourceFromList(List<?> dataSourcesList) {
		
		DataSourceModel dataSourceModel = null;
		
		int dataSourcesListSize = 0;
		
		while ((dataSourcesListSize = dataSourcesList.size()) > 0) {
			
			int index = (int) Math.round(Math.random() * (dataSourcesListSize - 1));
			try {
				dataSourceModel = (DataSourceModel) dataSourcesList.get(index);
				break;
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		
		return dataSourceModel;
	}
	
	private void addDataSourcesSelectList(DataSourceModel dataSourceModel) {
		if (weightMap.containsKey(dataSourceModel.getDataSourceType())) {
			int count = weightMap.get(dataSourceModel.getDataSourceType());
			if (count > 0) {
				List<DataSourceModel> dataSourceModelList = new ArrayList<DataSourceModel>(count);
				for (int i=0; i<count; i++) {
					dataSourceModelList.add(dataSourceModel);
				}
				dataSourcesSelectList.addAll(dataSourceModelList);
			} else {
				dataSourcesSelectList.add(dataSourceModel);
			}
		} else {
			dataSourcesSelectList.add(dataSourceModel);
		}
	}
	
	private void removeDataSourcesSelectList(DataSourceModel dataSourceModel) {
		if (weightMap.containsKey(dataSourceModel.getDataSourceType())) {
			int count = weightMap.get(dataSourceModel.getDataSourceType());
			if (count > 0) {
				List<DataSourceModel> dataSourceModelList = new ArrayList<DataSourceModel>(count);
				for (int i=0; i<count; i++) {
					dataSourceModelList.add(dataSourceModel);
				}
				dataSourcesSelectList.removeAll(dataSourceModelList);
			} else {
				dataSourcesSelectList.remove(dataSourceModel);
			}
		} else {
			dataSourcesSelectList.remove(dataSourceModel);
		}
	}
	
	class HeartBeatAliveTasksScan implements Runnable {

		public void run() {
			
			while (true) {
				
				for (DataSourceModel dataSourceModel : aliveSourcesList) {
					boolean status = DataSourceCheckAlive.CheckAliveDataSource(dataSourceModel.getDataSource(), detectingSql);
					AtomicInteger failureCount = dataSourceModel.getFailureCount();
					if (!status) {
						if (failureCount.incrementAndGet() > totalFailureCount) {
							removeDataSourcesSelectList(dataSourceModel);
							aliveSourcesList.remove(dataSourceModel);
							failureCount.set(0);
							deadSourcesList.add(dataSourceModel);
							logger.error("HeartBeatAliveTasksScan -> run -> DataSource dead and remove aliveSourcesList, DataSource:{}", dataSourceModel.getDataSourceType());
						} 
					} else {
						failureCount.set(0);
					}
				}

				try {
					Thread.sleep(interTime);
				} catch (InterruptedException e) {
					logger.error("HeartBeatAliveTasksScan -> run -> HeartBeatAliveTasksScan InterruptedException, Exception:{}", e);
				}
			}
		}
	}

	class HeartBeatDeadTasksScan implements Runnable {

		public void run() {
			
			while (true) {
				
				for (DataSourceModel dataSourceModel : deadSourcesList) {
					boolean status = DataSourceCheckAlive.CheckAliveDataSource(dataSourceModel.getDataSource(), detectingSql);
					AtomicInteger riseCount = dataSourceModel.getRiseCount();
					if (status) {
						if (riseCount.incrementAndGet() >= totalRiseCount) {
							deadSourcesList.remove(dataSourceModel);
							riseCount.set(0);
							aliveSourcesList.add(dataSourceModel);
							addDataSourcesSelectList(dataSourceModel);
							logger.info("HeartBeatAliveTasksScan -> run -> DataSource alive and remove deadSourcesList, DataSource:{}", dataSourceModel.getDataSourceType());
						}
					} else {
						riseCount.set(0);
					}
				}
				
				try {
					Thread.sleep(interTime);
				} catch (InterruptedException e) {
					logger.error("HeartBeatAliveTasksScan -> run -> HeartBeatDeadTasksScan InterruptedException, Exception:{}", e);
				}
			}
		}
	}

	public void setTotalFailureCount(int totalFailureCount) {
		this.totalFailureCount = totalFailureCount;
	}

	public void setTotalRiseCount(int totalRiseCount) {
		this.totalRiseCount = totalRiseCount;
	}

	public void setDetectingSql(String detectingSql) {
		this.detectingSql = detectingSql;
	}

	public void setInterTime(long interTime) {
		this.interTime = interTime;
	}

	public void setInitDataSourceMap(Map<String, DataSource> initDataSourceMap) {
		this.initDataSourceMap = initDataSourceMap;
	}

	public void setInitWeightMap(Map<String, Integer> initWeightMap) {
		this.initWeightMap = initWeightMap;
	}
	
	public static class DataSourceCheckAlive {
		
		private final static Logger logger = LoggerFactory.getLogger(DataSourceCheckAlive.class); 

		public static boolean CheckAliveDataSource(DataSource dataSource, String detectingSql){
			
			boolean result = false;
			Connection conn = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				conn = dataSource.getConnection();
				if (detectingSql == null || detectingSql.equalsIgnoreCase("")) {
					rs = conn.getMetaData().getTables(null, null, "PROBABLYNOT", new String[] { "TABLE" });
				} else {
					pstmt = conn.prepareStatement(detectingSql);
					pstmt.execute();
				}
				result = true;
			} catch (SQLException e) {
				logger.error("CheckAliveDataSource -> Database connection error, SQLState:{}, Exception:{}", e.getSQLState(), e);
			} finally {
				if (rs != null) {
					try {
						rs.close();
					} catch (SQLException e) {
						logger.error("CheckAliveDataSource -> Database rs close error, SQLState:{}, Exception:{}", e.getSQLState(), e);
					}
				}

				if (pstmt != null) {
					try {
						pstmt.close();
					} catch (SQLException e) {
						logger.error("CheckAliveDataSource -> preparedStatement close error, SQLState:{}, Exception:{}", e.getSQLState(), e);
					}
				}

				if (conn != null) {
					try {
						conn.close();
					} catch (SQLException e) {
						logger.error("CheckAliveDataSource -> connection close error, SQLState:{}, Exception:{}", e.getSQLState(), e);
					}
				}
			}

			return result;
		}
	}
}
