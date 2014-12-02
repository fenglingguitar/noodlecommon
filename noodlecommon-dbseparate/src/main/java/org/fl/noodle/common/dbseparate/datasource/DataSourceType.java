package org.fl.noodle.common.dbseparate.datasource;

public enum DataSourceType {
	
	MASTER("master"), 
	SALVE_1("slave_1"), 
	SALVE_2("slave_2"), 
	SALVE_3("slave_3");
	
	private String typeName;
	
	DataSourceType(String typeName) {
		this.typeName = typeName;
	}

	public String typeName() {
		return typeName;
	}
	
	public static DataSourceType checkType(String typeName) {
		if(typeName.equals("master")) {
			return DataSourceType.MASTER;
		} else if (typeName.equals("slave_1")) {
			return DataSourceType.SALVE_1;
		} else if (typeName.equals("slave_2")) {
			return DataSourceType.SALVE_2;
		} else if (typeName.equals("slave_3")) {
			return DataSourceType.SALVE_3;
		} else {
			return null;
		}
	}
}
