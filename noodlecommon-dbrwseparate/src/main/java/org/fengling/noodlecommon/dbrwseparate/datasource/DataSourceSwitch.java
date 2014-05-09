 package org.fengling.noodlecommon.dbrwseparate.datasource;

public class DataSourceSwitch {
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	  
	public static void setDataSourceType(String dbType) {
		contextHolder.set(dbType);
	}

	public static String getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}   
}
