package org.fl.noodle.common.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fl.noodle.common.mvc.annotation.NoodleResponseBody;
import org.fl.noodle.common.mvc.vo.MapVo;
import org.fl.noodle.common.mvc.vo.PageVo;
import org.fl.noodle.common.mvc.vo.VoidVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MethodReturnValueHandler extends AbstractMessageSendProcessor implements HandlerMethodReturnValueHandler {
	
	private final static Logger logger = LoggerFactory.getLogger(MethodReturnValueHandler.class);
	
	protected MethodReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}
		
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getMethod().getAnnotation(NoodleResponseBody.class) != null;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void handleReturnValue(Object returnValue,
								  MethodParameter returnType, 
								  ModelAndViewContainer mavContainer, 
								  NativeWebRequest webRequest) throws Exception {
		
		if (returnValue != null) {
			NoodleResponseBody responseBody = returnType.getMethodAnnotation(NoodleResponseBody.class);
			if (returnValue instanceof PageVo) {
				PageVo pageVo = (PageVo)returnValue;
				Map map = new HashMap();
				map.put("page", pageVo.getCurrentPageNo());
				map.put("total", pageVo.getTotalPageCount());
				map.put("records", pageVo.getTotalCount());
				map.put("rows", pageVo.getData());
				sendMessage(mavContainer, webRequest, serialize(responseBody.type(), map));
			} else if (returnValue instanceof MapVo) {
				Map map = ((MapVo)returnValue).getMap();
				sendMessage(mavContainer, webRequest, serialize(responseBody.type(), map));
			} else if (returnValue instanceof VoidVo) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("result", "true");
				sendMessage(mavContainer, webRequest, serialize(responseBody.type(), map));
			} else {
				sendMessage(mavContainer, webRequest, serialize(responseBody.type(), returnValue));
			}
		} else {
			sendNull(mavContainer, webRequest);
		}
	}
	
	private String serialize(String type, Object object) {
		
		String serializeString = "";
		
		if (type.equals("json")) {			
			serializeString = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss");
		} else if (type.equals("json-java")) {			
			serializeString = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss", SerializerFeature.WriteClassName);
		} else {
			if (logger.isErrorEnabled()) {
				logger.debug("handleReturnValue -> serialize string -> exception:No this serialize type");
			}
		}
		
		if (logger.isDebugEnabled()) {
			logger.debug("handleReturnValue -> output serialize string -> output:{}", serializeString);
		}

		return serializeString;
	}
	
	private void sendMessage(ModelAndViewContainer mavContainer, NativeWebRequest webRequest, String serializeString) throws Exception {
		mavContainer.setRequestHandled(true);
		writeWithMessageConverters(serializeString, webRequest);
	}
	
	private void sendNull(ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("handleReturnValue -> output serialize string -> output:null");
		}	
		mavContainer.setRequestHandled(true);
		writeWithMessageConverters("", webRequest);
	}
}