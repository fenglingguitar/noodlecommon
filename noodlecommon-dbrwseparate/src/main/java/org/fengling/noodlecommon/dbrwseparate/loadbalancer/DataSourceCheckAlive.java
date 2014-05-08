package org.fengling.noodlecommon.dbrwseparate.loadbalancer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataSourceCheckAlive {
		
	private final static Log logger = LogFactory.getLog(DataSourceCheckAlive.class); 

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
			if (logger.isErrorEnabled()) {
				logger.error("CheckAliveDataSource -> Database connection error, SQLState: " + e.getSQLState() + ", Exception: " + e);
			}
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					if (logger.isErrorEnabled()) {
						logger.error("CheckAliveDataSource -> Database rs close error, SQLState: " + e.getSQLState() + ", Exception: " + e);
					}
				}
			}

			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException e) {
					if (logger.isErrorEnabled()) {
						logger.error("CheckAliveDataSource -> preparedStatement close error, SQLState: " + e.getSQLState() + ", Exception: " + e);
					}
				}
			}

			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					if (logger.isErrorEnabled()) {
						logger.error("CheckAliveDataSource -> connection close error, SQLState: " + e.getSQLState() + ", Exception: " + e);
					}
				}
			}
		}

		return result;
	}
}
