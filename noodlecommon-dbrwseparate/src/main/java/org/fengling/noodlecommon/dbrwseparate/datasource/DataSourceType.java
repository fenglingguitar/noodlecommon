package org.fengling.noodlecommon.dbrwseparate.datasource;

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
		switch(typeName) {
		case "master":
			return DataSourceType.MASTER;
		case "slave_1":
			return DataSourceType.SALVE_1;
		case "slave_2":
			return DataSourceType.SALVE_2;
		case "slave_3":
			return DataSourceType.SALVE_3;
		default:
			return null;
		}
	}
}
