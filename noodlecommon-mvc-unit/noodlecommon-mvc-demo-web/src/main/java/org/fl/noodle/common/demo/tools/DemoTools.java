package org.fl.noodle.common.demo.tools;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fl.noodle.common.demo.vo.DemoVo;
import org.fl.noodle.common.mvc.vo.MapVo;
import org.fl.noodle.common.mvc.vo.PageVo;

public class DemoTools {

	
	public static DemoVo getVo(long id) {
		
		int caseByteLength = 256;
		byte[] caseByteArray = new byte[caseByteLength];
		for (int i=0; i<caseByteLength; i++) {
			caseByteArray[i] = (byte)0x41;
		}
		
		DemoVo demoVo = new DemoVo();	
		demoVo.setId(id);
		demoVo.setByteTest(Byte.MAX_VALUE);
		demoVo.setByteClassTest(Byte.MAX_VALUE);
		demoVo.setIntTest(Integer.MAX_VALUE);
		demoVo.setIntClassTest(Integer.MAX_VALUE);
		demoVo.setLongTest(Long.MAX_VALUE);
		demoVo.setLongClassTest(Long.MAX_VALUE);
		demoVo.setFloatTest(Float.MAX_VALUE);
		demoVo.setFloatClassTest(Float.MAX_VALUE);
		demoVo.setDoubleTest(Double.MAX_VALUE);
		demoVo.setDoubleClassTest(Double.MAX_VALUE);
		demoVo.setCharTest('A');
		demoVo.setBooleanTest(true);
		demoVo.setBooleanClassTest(true);
		demoVo.setStringTest(new String(caseByteArray));
		demoVo.setByteArrayTest(caseByteArray);
		demoVo.setDateTest(new Date());
		
		return demoVo;
	}
	
	public static List<DemoVo> getList() {
		
		List<DemoVo> demoVoList = new ArrayList<DemoVo>();
		for(int i=0; i<35; i++) {			
			demoVoList.add(getVo(i+1));
		}
		
		return demoVoList;
	}
	
	public static PageVo<DemoVo> getPage(long page, long rows) {
		
		List<DemoVo> list = getList();
		
		PageVo<DemoVo> pageVo = new PageVo<DemoVo>();
		pageVo.setPageSize(rows);
		pageVo.setTotalCount(list.size());
		pageVo.setStart(1 + (page - 1) * rows);
		
		if (pageVo.getPageSize() >= pageVo.getTotalCount()) {
			pageVo.setData(list);
		} else {			
			if (pageVo.getStart() + pageVo.getPageSize() > pageVo.getTotalCount()) {
				pageVo.setData(list.subList((int)pageVo.getStart() - 1, (int)pageVo.getTotalCount()));
			} else {			
				pageVo.setData(list.subList((int)pageVo.getStart() - 1, (int)(pageVo.getStart() + pageVo.getPageSize() - 1)));
			}
		}
		
		return pageVo;
	}
	
	public static MapVo<String, DemoVo> getMap(DemoVo vo) {
		
		Map<String, DemoVo> map = new HashMap<String, DemoVo>();
		map.put("input", vo);
		map.put("output", getVo(2));
		
		return new MapVo<String, DemoVo>(map);
	}
	
	public static MapVo<String, List<DemoVo>> getMapList(DemoVo vo) {
		
		Map<String, List<DemoVo>> map = new HashMap<String, List<DemoVo>>();
		
		List<DemoVo> inputList = new ArrayList<DemoVo>();
		inputList.add(vo);
		map.put("input", inputList);
		
		List<DemoVo> outputList = getList();
		map.put("output", outputList);
		
		return new MapVo<String, List<DemoVo>>(map);
	}
}
