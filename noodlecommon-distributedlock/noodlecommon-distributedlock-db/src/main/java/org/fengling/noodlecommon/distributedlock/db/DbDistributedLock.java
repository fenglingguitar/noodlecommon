package org.fengling.noodlecommon.distributedlock.db;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.distributedlock.api.AbstractDistributedLock;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class DbDistributedLock extends AbstractDistributedLock {
	
	private final static Log logger = LogFactory.getLog(DbDistributedLock.class);

	private JdbcTemplate jdbcTemplate;
	
	private long lockId = 1;
	private String tableName = "DISTRIBUTED_LOCK";
	private long rowId = 1;
	
	private String sqlGetDiffTime;
	private String sqlGetAlive;
	private String sqlKeepAlive;
	private String sqlReleaseAlive;
	
	protected void init() throws Exception {
		
		sqlGetDiffTime = "SELECT CURRENT_TIMESTAMP FROM DUAL";
		sqlGetAlive = "UPDATE " + tableName + " SET SET_ID = ?, OVERTIME = ? WHERE OVERTIME < ? AND ID = ?";
		sqlKeepAlive = "UPDATE " + tableName + " SET SET_ID = ?, OVERTIME = ? WHERE SET_ID = ? AND ID = ?";
		sqlReleaseAlive = "UPDATE " + tableName + " SET SET_ID = ?, OVERTIME = ? WHERE SET_ID = ? AND ID = ?";
		
		String sqlInsert = "INSERT INTO " + tableName + " SELECT " + rowId + " AS ID, 0 AS OVERTIME, 0 AS SET_ID FROM DUAL WHERE (SELECT COUNT(*) FROM " + tableName + " WHERE ID=" + rowId + ")=0";
		
		try {
			jdbcTemplate.update(sqlInsert);
		} catch (Exception e) {	
			if (logger.isErrorEnabled()) {
				logger.error("init -> jdbcTemplate update insert -> Exception: " + e);
			}
			throw e;
		}
	}

	@Override
	protected long getDiffTime() {
		
		long diffTime = 0;
		List<Long> diffTimeList = null;
		try {
			diffTimeList = jdbcTemplate.query(sqlGetDiffTime, new RowMapper<Long>() {
				@Override
				public Long mapRow(ResultSet resultSet, int index)
						throws SQLException {
					return resultSet.getTimestamp(1).getTime();
				}
			});
		} catch (Exception e) {	
			if (logger.isErrorEnabled()) {
				logger.error("getDiffTime -> jdbcTemplate query -> Exception: " + e);
			}
		}
		if (diffTimeList != null && diffTimeList.size() > 0) {
			diffTime = System.currentTimeMillis() - diffTimeList.get(0);
		}
		return diffTime;
	}

	@Override
	protected boolean getAlive() {
		
		long now = System.currentTimeMillis() + diffTime;
		try {
			if (jdbcTemplate.update(sqlGetAlive, new Object[] {
					lockId,
					now + intervalTime,
					now,
					rowId
			}) == 1) {
				return true;
			}
		} catch (Exception e) {	
			if (logger.isErrorEnabled()) {
				logger.error("getAlive -> jdbcTemplate update -> Exception: " + e);
			}
		}
		return false;
	}

	@Override
	protected boolean keepAlive() {
		
		long now = System.currentTimeMillis() + diffTime;
		try {
			if (jdbcTemplate.update(sqlKeepAlive, new Object[] {
					lockId,
					now + intervalTime,
					lockId,
					rowId
			}) == 1) {
				return true;
			}
		} catch (Exception e) {	
			if (logger.isErrorEnabled()) {
				logger.error("keepAlive -> jdbcTemplate update -> Exception: " + e);
			}
		}
		return false;
	}

	@Override
	protected boolean releaseAlive() {
		
		try {	
			if (jdbcTemplate.update(sqlReleaseAlive, new Object[] {
					0,
					0,
					lockId,
					rowId
			}) == 1) {
				return true;
			}
		} catch (Exception e) {	
			if (logger.isErrorEnabled()) {
				logger.error("releaseAlive -> jdbcTemplate update -> Exception: " + e);
			}
		}
		return false;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public void setLockId(long lockId) {
		this.lockId = lockId;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public void setRowId(long rowId) {
		this.rowId = rowId;
	}
}
