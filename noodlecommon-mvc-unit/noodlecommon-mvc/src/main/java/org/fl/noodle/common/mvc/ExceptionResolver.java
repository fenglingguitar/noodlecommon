package org.fl.noodle.common.mvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.mvc.exception.JsonException;
import org.fl.noodle.common.mvc.exception.ViewException;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;



import flexjson.JSONSerializer;


public class ExceptionResolver extends AbstractMessageSendProcessor
		implements HandlerExceptionResolver {

	protected final Log logger = LogFactory.getLog(ExceptionResolver.class);
	
	protected ExceptionResolver(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		
		if (logger.isErrorEnabled()) {
			logger.error("ResolveException -> Exception: " + ex);
		}
		
		if (ex instanceof JsonException) {
			try {
				Map<String, String> map = new HashMap<String, String>();
				map.put("result", "false");
				map.put("promptMessage", ((JsonException)ex).getPromptMessage());
				map.put("errorMessage", ex.getMessage());
				
				JSONSerializer serializer = new JSONSerializer();
				String json = serializer.deepSerialize(map);
				
				writeWithMessageConverters(json, webRequest);
				return new ModelAndView();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("ResolveException -> Resolve JsonException Error, Exception: " + e);
				}
			}
		} else if (ex instanceof ViewException) {
			try {
				writeWithMessageConverters("<script>alert(\"" + ((ViewException)ex).getPromptMessage() + "\");history.go(-1);</script>", webRequest);
				return new ModelAndView();
			} catch (Exception e) {
				if (logger.isErrorEnabled()) {
					logger.error("ResolveException -> Resolve ViewException Error, Exception: " + e);
				}
			} 
		}
		
		return new ModelAndView("error_500");
	}
}
