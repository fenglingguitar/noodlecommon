package org.fengling.noodlecommon.dbrwseparate.datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSourceCheckAlive {
		
	private final static Log logger = LogFactory.getLog(DataSourceCheckAlive.class); 

	public static DataSourceCheckResult CheckAliveDataSource(DataSource dataSource,String detectingSql){
		
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
			logger.error("database connection error=" + e.getSQLState(), e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					logger.error("database rs close error" + e.getSQLState(), e);
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					logger.error("database preparedStatement close error" + e.getSQLState(), e);
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					logger.error("database connection close error" + e.getSQLState(), e);
				}
			}
		}

		return new DataSourceCheckResult(result, "");
	}
}
