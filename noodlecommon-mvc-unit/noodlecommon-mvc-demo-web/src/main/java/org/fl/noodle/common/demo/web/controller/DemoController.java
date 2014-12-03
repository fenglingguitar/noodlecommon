package org.fl.noodle.common.demo.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.demo.tools.DemoTools;
import org.fl.noodle.common.demo.vo.DemoVo;
import org.fl.noodle.common.mvc.annotation.RequestParam;
import org.fl.noodle.common.mvc.annotation.ResponseBody;
import org.fl.noodle.common.mvc.exception.ApiException;
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
		return DemoTools.getPage(Long.parseLong(page), Long.parseLong(rows));
	}
	
	@RequestMapping(value = "/querylist")
	@ResponseBody
	public List<DemoVo> queryList(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryList -> input vo: " + vo);
		return DemoTools.getList();
	}
	
	@RequestMapping(value = "/querymap")
	@ResponseBody
	public MapVo<String, DemoVo> queryMap(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMap -> input vo: " + vo);
		return DemoTools.getMap(vo);
	}
	
	@RequestMapping(value = "/querymaplist")
	@ResponseBody
	public MapVo<String, List<DemoVo>> queryMapList(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return DemoTools.getMapList(vo);
	}
	
	@RequestMapping(value = "/queryone")
	@ResponseBody
	public DemoVo queryOne(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return DemoTools.getVo(2);
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
	
	@RequestMapping(value = "/querymaplist-java")
	@ResponseBody(type="json-java")
	public MapVo<String, List<DemoVo>> queryMapListJava(@RequestParam DemoVo vo) throws Exception {
		logger.info("queryMapListJava -> input vo: " + vo);
		return DemoTools.getMapList(vo);
	}
	
	@RequestMapping(value = "/throwapiexception")
	@ResponseBody
	public VoidVo throwApiException(@RequestParam DemoVo vo) throws Exception {
		logger.info("throwException -> input vo: " + vo);
		throw new ApiException("错误API测试", "抛出API错误");
	}
	
	@RequestMapping(value = "/throwexception")
	@ResponseBody
	public VoidVo throwException(@RequestParam DemoVo vo) throws Exception {
		logger.info("throwException -> input vo: " + vo);
		throw new Exception("抛出错误");
	}
}
