package org.fl.noodle.common.trace.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Postman {

	private static ThreadLocal<Map<String, Object>> postmanThreadLocal = new ThreadLocal<Map<String, Object>>();
	
	public final static String POSTMAN_KEY = "postman";
	
	public static void  putParam(String key, Object value) {
		getMap().put(key, value);
	}
	
	public static Object getParam(String key) {
		return getMap().get(key);
	}
	
	public static boolean exitParam(String key) {
		return getMap().containsKey(key);
	}
	
	public static void deleteParam(String key) {
		getMap().remove(key);
	}
	
	public static String toJson() {
		return JSON.toJSONString(getMap(), SerializerFeature.WriteClassName);
	}
	
	@SuppressWarnings("unchecked")
	public static void fromJson(String json) {
		postmanThreadLocal.set((Map<String, Object>) JSON.parse(json));
	}
	
	private static Map<String, Object> getMap() {
		if (postmanThreadLocal.get() == null) {
			postmanThreadLocal.set(new HashMap<String, Object>());
		}
		return postmanThreadLocal.get();
	}
}
