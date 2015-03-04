package org.fl.noodle.common.monitor.performance.persistence;

import java.util.List;
import java.util.Set;

public interface PerformancePersistence {

	/*
	 * LIST查询
	 */
	public <T> List<T> queryList(String keyName, double min, double max, Class<T> clazz) throws Exception;

	/*
	 * 插入
	 */
	public void insert(String keyName, double score, Object vo) throws Exception;

	/*
	 * 区域删除
	 */
	public long deletes(String keyName, double min, double max) throws Exception;
	
	/*
	 * 得到全部Key
	 */
	public Set<String> getKeys() throws Exception;
}
