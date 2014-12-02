package org.fl.noodle.common.demo.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fl.noodle.common.demo.vo.DemoVo;
import org.fl.noodle.common.mvc.annotation.RequestParam;
import org.fl.noodle.common.mvc.annotation.ResponseBody;
import org.fl.noodle.common.mvc.vo.MapVo;
import org.fl.noodle.common.mvc.vo.PageVo;
import org.fl.noodle.common.mvc.vo.VoidVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "demo")
public class DemoController {
	
	@RequestMapping(value = "/querypage")
	@ResponseBody
	public PageVo<DemoVo> queryPage(@RequestParam DemoVo vo, String page, String rows) throws Exception {
		page = page != null && !page.equals("") ? page : "0";
		rows = rows != null && !rows.equals("") ? rows : "0";
		return getPage(vo, Long.parseLong(page), Long.parseLong(rows));
	}
	
	@RequestMapping(value = "/querylist")
	@ResponseBody
	public List<DemoVo> queryList(@RequestParam DemoVo vo) throws Exception {
		return getList(vo);
	}
	
	@RequestMapping(value = "/querymap")
	@ResponseBody
	public MapVo<String, DemoVo> queryMap(@RequestParam DemoVo vo) throws Exception {
		return getMap(vo);
	}
	
	@RequestMapping(value = "/queryvo")
	@ResponseBody
	public DemoVo queryVo(@RequestParam DemoVo vo) throws Exception {
		return vo;
	}
	
	@RequestMapping(value = "/insertvo")
	@ResponseBody
	public VoidVo insert(@RequestParam DemoVo vo) throws Exception {
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertarray")
	@ResponseBody
	public VoidVo inserts(@RequestParam DemoVo[] vos) throws Exception {
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertlist")
	@ResponseBody
	public VoidVo insertList(@RequestParam List<DemoVo> voList) throws Exception {
		return VoidVo.VOID;
	}
	
	private DemoVo getVo(int id) {
		
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
	
	private List<DemoVo> getList(DemoVo vo) {
		
		List<DemoVo> demoVoList = new ArrayList<DemoVo>();
		demoVoList.add(vo);
		for(int i=0; i<35; i++) {			
			demoVoList.add(getVo(i+2));
		}
		
		return demoVoList;
	}
	
	private PageVo<DemoVo> getPage(DemoVo vo, long page, long rows) {
		
		List<DemoVo> list = getList(vo);
		
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
	
	private MapVo<String, DemoVo> getMap(DemoVo vo) {
		
		Map<String, DemoVo> map = new HashMap<String, DemoVo>();
		map.put("input", vo);
		map.put("other", getVo(1));
		
		return new MapVo<String, DemoVo>(map);
	}
}
