package org.fl.noodle.common.demo.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
	
	protected final Log logger = LogFactory.getLog(DemoController.class);
	
	@RequestMapping(value = "/querypage")
	@ResponseBody
	public PageVo<DemoVo> queryPage(@RequestParam DemoVo vo, String page, String rows) throws Exception {
		page = page != null && !page.equals("") ? page : "0";
		rows = rows != null && !rows.equals("") ? rows : "0";
		logger.info("queryPage -> input vo: " + vo);
		logger.info("queryPage -> page: " + page);
		logger.info("queryPage -> rows: " + rows);
		return getPage(Long.parseLong(page), Long.parseLong(rows));
	}
	
	@RequestMapping(value = "/querylist")
	@ResponseBody
	public List<DemoVo> queryList(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryList -> input vo: " + vo);
		return getList();
	}
	
	@RequestMapping(value = "/querymap")
	@ResponseBody
	public MapVo<String, DemoVo> queryMap(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMap -> input vo: " + vo);
		return getMap(vo);
	}
	
	@RequestMapping(value = "/querymaplist")
	@ResponseBody
	public MapVo<String, List<DemoVo>> queryMapList(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return getMapList(vo);
	}
	
	@RequestMapping(value = "/queryone")
	@ResponseBody
	public DemoVo queryOne(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return getVo(2);
	}
	
	@RequestMapping(value = "/insertone")
	@ResponseBody
	public VoidVo insertOne(@RequestParam DemoVo vo) throws Exception {
		logger.info("insertOne -> input vo: " + vo);
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertmultiple")
	@ResponseBody
	public VoidVo insertMultiple(@RequestParam(name="input1") DemoVo vo1, @RequestParam(name="input2") DemoVo vo2) throws Exception {
		logger.info("insertMultiple -> input1 vo: " + vo1);
		logger.info("insertMultiple -> input2 vo: " + vo2);
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertarray")
	@ResponseBody
	public VoidVo insertArray(@RequestParam DemoVo[] vos) throws Exception {
		for(DemoVo demoVo : vos) {
			logger.info("insertArray -> input vos: " + demoVo);
		}
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertlist")
	@ResponseBody
	public VoidVo insertList(@RequestParam List<DemoVo> voList) throws Exception {
		for(DemoVo demoVo : voList) {
			logger.info("insertList -> input vos: " + demoVo);
		}
		return VoidVo.VOID;
	}
	
	private DemoVo getVo(long id) {
		
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
	
	private List<DemoVo> getList() {
		
		List<DemoVo> demoVoList = new ArrayList<DemoVo>();
		for(int i=0; i<35; i++) {			
			demoVoList.add(getVo(i+1));
		}
		
		return demoVoList;
	}
	
	private PageVo<DemoVo> getPage(long page, long rows) {
		
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
	
	private MapVo<String, DemoVo> getMap(DemoVo vo) {
		
		Map<String, DemoVo> map = new HashMap<String, DemoVo>();
		map.put("input", vo);
		map.put("output", getVo(2));
		
		return new MapVo<String, DemoVo>(map);
	}
	
	private MapVo<String, List<DemoVo>> getMapList(DemoVo vo) {
		
		Map<String, List<DemoVo>> map = new HashMap<String, List<DemoVo>>();
		
		List<DemoVo> inputList = new ArrayList<DemoVo>();
		inputList.add(vo);
		map.put("input", inputList);
		
		List<DemoVo> outputList = getList();
		map.put("output", outputList);
		
		return new MapVo<String, List<DemoVo>>(map);
	}
}
