package org.fl.noodle.common.connect.aop;

import java.util.HashMap;
import java.util.Map;

public class ConnectThreadLocalStorage {
	
	private static final ThreadLocal<Map<StorageType, Object>> threadLocalMap = new ThreadLocal<Map<StorageType, Object>>();
	
	public static void put(StorageType storageType, Object object) {
		init();
		threadLocalMap.get().put(storageType, object);
	}
	
	public static Object get(StorageType storageType) {
		init();
		return threadLocalMap.get().get(storageType);
	}
	
	public static void remove(StorageType storageType) {
		init();
		threadLocalMap.get().remove(storageType);
	}
	
	private static void init() {
		if (threadLocalMap.get() == null) {
			threadLocalMap.set(new HashMap<StorageType, Object>());
		}
	}
	
	public static enum StorageType {
		
		AGENT(1);
		
		private int code;

		private StorageType(int code) {
			this.code = code;
		}
		
		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}  
	}
}
