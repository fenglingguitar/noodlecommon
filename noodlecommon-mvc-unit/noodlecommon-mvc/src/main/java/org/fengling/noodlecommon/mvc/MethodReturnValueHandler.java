package org.fengling.noodlecommon.mvc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fengling.noodlecommon.mvc.annotation.ResponseBody;
import org.fengling.noodlecommon.mvc.vo.MapVo;
import org.fengling.noodlecommon.mvc.vo.PageVo;
import org.fengling.noodlecommon.mvc.vo.VoidVo;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;


import flexjson.JSONSerializer;
import flexjson.transformer.DateTransformer;


public class MethodReturnValueHandler extends AbstractMessageSendProcessor 
		implements HandlerMethodReturnValueHandler {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
	private JSONSerializer serializer = new JSONSerializer();
	
	protected MethodReturnValueHandler() {
		super(null);
		serializer.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss.SSS"), Date.class);
	}
	
	protected MethodReturnValueHandler(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
		serializer.transform(new DateTransformer("yyyy-MM-dd HH:mm:ss.SSS"), Date.class);
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
					String json = serializer.deepSerialize(map);
					mavContainer.setRequestHandled(true);
					writeWithMessageConverters(json, webRequest);
				} else if (returnValue instanceof MapVo) {
					Map map = ((MapVo)returnValue).getMap();
					String json = serializer.deepSerialize(map);
					mavContainer.setRequestHandled(true);
					writeWithMessageConverters(json, webRequest);
				} else if (returnValue instanceof List) {
					List dataList = (List)returnValue;
					String json = serializer.deepSerialize(dataList);
					mavContainer.setRequestHandled(true);
					writeWithMessageConverters(json, webRequest);
				} else if (returnValue instanceof VoidVo) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("result", "true");
					String json = serializer.deepSerialize(map);
					mavContainer.setRequestHandled(true);
					writeWithMessageConverters(json, webRequest);
				} else {
					String json = serializer.deepSerialize(returnValue);
					mavContainer.setRequestHandled(true);
					writeWithMessageConverters(json, webRequest);
				}
			} else {
				sendNull(mavContainer, webRequest);
			}
		} else {
			sendNull(mavContainer, webRequest);
		}
	}
	
	private void sendNull(ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
		mavContainer.setRequestHandled(true);
		writeWithMessageConverters("", webRequest);
	}
}