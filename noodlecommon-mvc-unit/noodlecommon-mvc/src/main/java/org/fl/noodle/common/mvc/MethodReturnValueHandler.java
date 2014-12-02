package org.fl.noodle.common.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.mvc.annotation.ResponseBody;
import org.fl.noodle.common.mvc.vo.MapVo;
import org.fl.noodle.common.mvc.vo.PageVo;
import org.fl.noodle.common.mvc.vo.VoidVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class MethodReturnValueHandler extends AbstractMessageSendProcessor implements HandlerMethodReturnValueHandler {
	
	protected final Log logger = LogFactory.getLog(MethodReturnValueHandler.class);
	
	protected MethodReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}
		
	public boolean supportsReturnType(MethodParameter returnType) {
		return returnType.getMethod().getAnnotation(ResponseBody.class) != null;
		
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void handleReturnValue(Object returnValue,
								  MethodParameter returnType, 
								  ModelAndViewContainer mavContainer, 
								  NativeWebRequest webRequest) throws Exception {
		
		if (returnValue != null) {
			ResponseBody responseBody = returnType.getMethodAnnotation(ResponseBody.class);
			if (responseBody.type().equals("json")) {
				if (returnValue instanceof PageVo) {
					PageVo pageVo = (PageVo)returnValue;
					Map map = new HashMap();
					map.put("page", pageVo.getCurrentPageNo());
					map.put("total", pageVo.getTotalPageCount());
					map.put("records", pageVo.getTotalCount());
					map.put("rows", pageVo.getData());
					sendMessage(mavContainer, webRequest, map);
				} else if (returnValue instanceof MapVo) {
					Map map = ((MapVo)returnValue).getMap();
					sendMessage(mavContainer, webRequest, map);
				} else if (returnValue instanceof VoidVo) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("result", "true");
					sendMessage(mavContainer, webRequest, map);
				} else {
					sendMessage(mavContainer, webRequest, returnValue);
				}
			} else {
				sendNull(mavContainer, webRequest);
			}
		} else {
			sendNull(mavContainer, webRequest);
		}
	}
	
	private void sendMessage(ModelAndViewContainer mavContainer, NativeWebRequest webRequest, Object object) throws Exception {
		String json = JSON.toJSONStringWithDateFormat(object, "yyyy-MM-dd HH:mm:ss.SSS", SerializerFeature.WriteClassName);
		if (logger.isDebugEnabled()) {
			logger.debug("handleReturnValue -> output json -> " + json);
		}	
		mavContainer.setRequestHandled(true);
		writeWithMessageConverters(json, webRequest);
	}
	
	private void sendNull(ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("handleReturnValue -> output json -> null");
		}	
		mavContainer.setRequestHandled(true);
		writeWithMessageConverters("", webRequest);
	}
}