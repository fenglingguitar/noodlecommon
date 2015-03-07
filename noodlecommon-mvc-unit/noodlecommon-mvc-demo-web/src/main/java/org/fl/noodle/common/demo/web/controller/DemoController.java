package org.fl.noodle.common.demo.web.controller;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.demo.tools.DemoTools;
import org.fl.noodle.common.demo.vo.DemoVo;
import org.fl.noodle.common.mvc.annotation.NoodleRequestParam;
import org.fl.noodle.common.mvc.annotation.NoodleResponseBody;
import org.fl.noodle.common.mvc.exception.ApiException;
import org.fl.noodle.common.mvc.vo.MapVo;
import org.fl.noodle.common.mvc.vo.PageVo;
import org.fl.noodle.common.mvc.vo.VoidVo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "demo")
public class DemoController {
	
	protected final Log logger = LogFactory.getLog(DemoController.class);
	
	@RequestMapping(value = "/querypage")
	@NoodleResponseBody
	public PageVo<DemoVo> queryPage(@NoodleRequestParam DemoVo vo, String page, String rows) throws Exception {
		page = page != null && !page.equals("") ? page : "0";
		rows = rows != null && !rows.equals("") ? rows : "0";
		logger.info("queryPage -> input vo: " + vo);
		logger.info("queryPage -> page: " + page);
		logger.info("queryPage -> rows: " + rows);
		return DemoTools.getPage(Long.parseLong(page), Long.parseLong(rows));
	}
	
	@RequestMapping(value = "/querylist")
	@NoodleResponseBody
	public List<DemoVo> queryList(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("queryList -> input vo: " + vo);
		return DemoTools.getList();
	}
	
	@RequestMapping(value = "/querymap")
	@NoodleResponseBody
	public MapVo<String, DemoVo> queryMap(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("queryMap -> input vo: " + vo);
		return DemoTools.getMap(vo);
	}
	
	@RequestMapping(value = "/querymaplist")
	@NoodleResponseBody
	public MapVo<String, List<DemoVo>> queryMapList(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return DemoTools.getMapList(vo);
	}
	
	@RequestMapping(value = "/queryone")
	@NoodleResponseBody
	public DemoVo queryOne(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("queryMapList -> input vo: " + vo);
		return DemoTools.getVo(2);
	}
	
	@RequestMapping(value = "/insertone")
	@NoodleResponseBody
	public VoidVo insertOne(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("insertOne -> input vo: " + vo);
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertmultiple")
	@NoodleResponseBody
	public VoidVo insertMultiple(@NoodleRequestParam(name="input1") DemoVo vo1, @NoodleRequestParam(name="input2") DemoVo vo2) throws Exception {
		logger.info("insertMultiple -> input1 vo: " + vo1);
		logger.info("insertMultiple -> input2 vo: " + vo2);
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertarray")
	@NoodleResponseBody
	public VoidVo insertArray(@NoodleRequestParam DemoVo[] vos) throws Exception {
		for(DemoVo demoVo : vos) {
			logger.info("insertArray -> input vos: " + demoVo);
		}
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/insertlist")
	@NoodleResponseBody
	public VoidVo insertList(@NoodleRequestParam List<DemoVo> voList) throws Exception {
		for(DemoVo demoVo : voList) {
			logger.info("insertList -> input vos: " + demoVo);
		}
		return VoidVo.VOID;
	}
	
	@RequestMapping(value = "/querymaplist-java")
	@NoodleResponseBody(type="json-java")
	public MapVo<String, List<DemoVo>> queryMapListJava(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("queryMapListJava -> input vo: " + vo);
		return DemoTools.getMapList(vo);
	}
	
	@RequestMapping(value = "/throwapiexception")
	@NoodleResponseBody
	public VoidVo throwApiException(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("throwException -> input vo: " + vo);
		throw new ApiException("错误API测试", "抛出API错误");
	}
	
	@RequestMapping(value = "/throwexception")
	@NoodleResponseBody
	public VoidVo throwException(@NoodleRequestParam DemoVo vo) throws Exception {
		logger.info("throwException -> input vo: " + vo);
		throw new Exception("抛出错误");
	}
	
	@RequestMapping(value = "/string")
	@ResponseBody
	public String string(String input) throws Exception {
		logger.info("string -> input content: " + input);
		return input;
	}
}
