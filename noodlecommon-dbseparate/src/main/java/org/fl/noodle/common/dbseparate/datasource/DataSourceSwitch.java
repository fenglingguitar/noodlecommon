 package org.fl.noodle.common.dbseparate.datasource;

public class DataSourceSwitch {
	
	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();  
	  
	public static void setDataSourceType(DataSourceType dataSourceType) {
		contextHolder.set(dataSourceType.typeName());
	}

	public static String getDataSourceType() {
		return contextHolder.get();
	}

	public static void clearDataSourceType() {
		contextHolder.remove();
	}   
}
