package org.fengling.noodlecommon.dbrwseparate.template;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceSwitch;
import org.fengling.noodlecommon.dbrwseparate.datasource.DataSourceType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class FailoverJdbcTemplate extends JdbcTemplate {
	
	private final static Log logger = LogFactory.getLog(FailoverJdbcTemplate.class);
	
	public int update(String sql, Object... args) throws DataAccessException {
		
		int result = 0;
		
		try {
			DataSourceSwitch.setDataSourceType(DataSourceType.MASTER);
			result = super.update(sql, args);
		} catch (DataAccessException e) {
			if (logger.isErrorEnabled()) {
				logger.error("update -> MASTER update -> DataAccessException: " + e);
			}
			try {
				DataSourceSwitch.setDataSourceType(DataSourceType.SALVE_1);
				result = super.update(sql, args);
				if (logger.isDebugEnabled()) {
					logger.debug("update -> failover success");
				}
			} catch (DataAccessException ce) {
				if (logger.isErrorEnabled()) {
					logger.error("update -> SALVE_1 update -> DataAccessException: " + ce);
				}
			}
		}
		
		return result;
	}
}
