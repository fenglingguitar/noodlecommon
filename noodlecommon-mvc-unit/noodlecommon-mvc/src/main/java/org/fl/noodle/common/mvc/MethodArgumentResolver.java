package org.fl.noodle.common.mvc;

import java.lang.reflect.Array;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.mvc.annotation.RequestParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;

public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

	protected final Log logger = LogFactory.getLog(MethodArgumentResolver.class);

	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(RequestParam.class) != null;
	}

	public Object resolveArgument(MethodParameter parameter,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, 
								  WebDataBinderFactory binderFactory) throws Exception {
				
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		
		if (requestParam.type().equals("json")) {
			String json = request.getParameter(requestParam.name());
			if (json != null && !json.isEmpty()) {
				if (logger.isDebugEnabled()) {
					logger.debug("resolveArgument -> input json -> " + json);
				}				
				return JSON.parseObject(json, parameter.getParameterType());
			} else {
				if (logger.isDebugEnabled()) {
					logger.debug("resolveArgument -> input json -> null");
				}
				Class<?> parameterType = parameter.getParameterType();
				if (parameterType.getConstructors().length > 0) {
					return parameterType.newInstance();
				} else {
					String className = parameterType.getCanonicalName();
					Class<?> T = Class.forName(className.substring(0, className.length()-2));
					return (Object[]) Array.newInstance(T, (int)0);
				}
			}
		} 
		
		return null;
	}
}