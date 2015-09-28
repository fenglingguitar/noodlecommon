package org.fl.noodle.common.mvc;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.fl.noodle.common.mvc.annotation.NoodleRequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.alibaba.fastjson.JSON;

public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

	private final static Logger logger = LoggerFactory.getLogger(MethodArgumentResolver.class);

	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(NoodleRequestParam.class) != null;
	}

	public Object resolveArgument(MethodParameter parameter,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, 
								  WebDataBinderFactory binderFactory) throws Exception {
				
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		NoodleRequestParam requestParam = parameter.getParameterAnnotation(NoodleRequestParam.class);
		
		String input = request.getParameter(requestParam.name());
		if (input != null && !input.isEmpty()) {
			if (logger.isDebugEnabled()) {
				logger.debug("resolveArgument -> request.getParameter -> input:{}", input);
			}	
			if (requestParam.type().equals("json")) {				
				return JSON.parseObject(input, parameter.getParameterType());
			} else if (requestParam.type().equals("date")) {
				String param = request.getParameter(parameter.getParameterName());	
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = simpleDateFormat.parse(param);
				return date;
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("resolveArgument -> request.getParameter -> input is null");
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
		
		return null;
	}
}