package org.fl.noodle.common.mvc.vo;

import java.util.Map;

public class MapVo<K,V> {
	
	private Map<K,V> map;

	public MapVo() {
	}
	
	public MapVo(Map<K,V> map) {
		this.map = map;
	}
	
	public void setMap(Map<K,V> map) {
		this.map = map;
	}

	public Map<K,V> getMap() {
		return map;
	}
}
