package org.fengling.noodlecommon.mvc.vo;

import java.util.Map;

public class MapVo {
	
	@SuppressWarnings("rawtypes")
	private Map map;

	@SuppressWarnings("rawtypes")
	public MapVo(Map map) {
		this.map = map;
	}
	
	@SuppressWarnings("rawtypes")
	public void setMap(Map map) {
		this.map = map;
	}

	@SuppressWarnings("rawtypes")
	public Map getMap() {
		return map;
	}
}
