package org.fl.noodle.common.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.fl.noodle.common.mvc.vo.ResultVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;

public class ExceptionResolver extends AbstractMessageSendProcessor implements HandlerExceptionResolver {

	private final static Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);
	
	protected ExceptionResolver(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		
		ServletWebRequest webRequest = new ServletWebRequest(request, response);
		
		logger.error("ResolveException -> Exception:{}", ex);
		
		try {
			writeWithMessageConverters(JSON.toJSONString(new ResultVo("false", ex.getMessage())), webRequest);
		} catch (Exception e) {
			logger.error("ResolveException -> Resolve JsonException Error, Exception:{}", e);
		}
		
		return new ModelAndView();
	}
}
