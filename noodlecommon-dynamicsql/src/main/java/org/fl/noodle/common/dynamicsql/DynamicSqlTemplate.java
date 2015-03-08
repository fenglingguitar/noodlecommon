package org.fl.noodle.common.dynamicsql;

import java.util.List;
import java.util.Map;

import org.fl.noodle.common.mvc.vo.PageVo;

public interface DynamicSqlTemplate {
	
	/*
	 * 分页查询
	 */
	public <T> PageVo<T> queryPage(String sqlName, Map<String, Object> params, int pageIndex, int pageSize, Class<T> clazz) throws Exception;
	
	/*
	 * LIST查询
	 */
	public <T> List<T> queryList(String sqlName, Map<String, Object> params, Class<T> clazz) throws Exception;
	
	/*
	 * 分页查询, 用字符串SQL
	 */
	public <T> PageVo<T> queryPageSql(String strSql, Map<String, Object> params, int pageIndex, int pageSize, Class<T> clazz) throws Exception;
	
	/*
	 * LIST查询, 用字符串SQL
	 */
	public <T> List<T> queryListSql(String strSql, Map<String, Object> params, Class<T> clazz) throws Exception;
	
	/*
	 * 普通插入
	 */
	public <T> T insert(Object vo, Class<T> clazz) throws Exception;
	
	/*
	 * 普通更新
	 * 以主键为条件更新
	 * vo中为非NULL的域，更新为vo中的值；vo中为NULL的域更新为NULL值
	 */
	public <T> void update(Object vo, Class<T> clazz) throws Exception;
	
	/*
	 * 普通更新
	 * 以主键为条件更新
	 * vo中为非NULL的域，更新为vo中的值；vo中为NULL的域更新为NULL值（只更新数组fieldNameArray中的域）
	 */
	public <T> int updateInclude(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception;
	
	/*
	 * 普通更新
	 * 以主键为条件更新
	 * vo中为非NULL的域，更新为vo中的值；vo中为NULL的域更新为NULL值（除了数组fieldNameArray中的域）
	 */
	public <T> int updateExcept(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception;

	/*
	 * 非NULL更新
	 * 以主键为条件更新
	 * vo中为非NULL的域，更新为vo中的值；vo中为NULL的域不更新
	 */
	public <T> int updateNonull(Object vo, Class<T> clazz) throws Exception;
		
	/*
	 * 非NULL非ID更新
	 * 以数组fieldNameArray指明的域为条件更新
	 * vo中为非NULL的域，更新为vo中的值；vo中为NULL的域不更新
	 */
	public <T> int updateNonullNoById(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception;
	
	/*
	 * SQL更新
	 */
	public int updateSql(String sqlName, Map<String, Object> params) throws Exception;
	
	/*
	 * 普通删除
	 * 以主键为条件删除
	 */
	public <T> void delete(Object vo, Class<T> clazz) throws Exception;
	
	/*
	 * 非ID删除
	 * 以数组fieldNameArray指明的域为条件删除
	 */
	public <T> void deleteNoById(Object vo, Class<T> clazz, String[] fieldNameArray) throws Exception;
	
	
	public <T> T load(Object vo, Class<T> clazz) throws Exception;
	
	
	public <T> T insertOrUpdate(Object vo, Class<T> clazz) throws Exception;
	
	
	public void flush() throws Exception;
}
