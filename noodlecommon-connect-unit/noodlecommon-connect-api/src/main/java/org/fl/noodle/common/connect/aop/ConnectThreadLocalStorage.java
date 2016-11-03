package org.fl.noodle.common.connect.aop;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectThreadLocalStorage {
	
	private final static Logger logger = LoggerFactory.getLogger(ConnectThreadLocalStorage.class);
	
	private static final ThreadLocal<Map<String, Object>> threadLocalMap = new ThreadLocal<Map<String, Object>>();
	
	public static void put(String storageTypeCode, Object object) {
		init();
		if (threadLocalMap.get().containsKey(storageTypeCode)) {
			logger.warn("put -> thread local already has a same storage type -> {}", storageTypeCode);
		}
		threadLocalMap.get().put(storageTypeCode, object);
	}
	
	public static Object get(String storageTypeCode) {
		init();
		return threadLocalMap.get().get(storageTypeCode);
	}
	
	public static void remove(String storageTypeCode) {
		init();
		threadLocalMap.get().remove(storageTypeCode);
	}
	
	public static boolean contain(String storageTypeCode) {
		init();
		return threadLocalMap.get().containsKey(storageTypeCode);
	}
	
	private static void init() {
		if (threadLocalMap.get() == null) {
			threadLocalMap.set(new HashMap<String, Object>());
		}
	}
	
	public static enum StorageType {
		
		AGENT("StorageType.Agent");
		
		private String code;

		private StorageType(String code) {
			this.code = code;
		}
		
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}  
	}
}
