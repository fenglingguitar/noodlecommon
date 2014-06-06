package org.fengling.noodlecommon.mvc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.ServletWebRequest;

public class MessageSendProcessor extends AbstractMessageSendProcessor {

	protected final Log logger = LogFactory.getLog(getClass());
	
	protected MessageSendProcessor(List<HttpMessageConverter<?>> messageConverters) {
		super(messageConverters);
	}

	public void alertMessageAndReplace(String message, String gotoPath, HttpServletRequest request, HttpServletResponse response) {
		
		ServletWebRequest webRequest = new ServletWebRequest(request, response);

		try {
			writeWithMessageConverters("<script>alert('" + message + "');window.location.replace('" + gotoPath + "');</script>", webRequest);
		} catch (Exception e) {
			if (logger.isErrorEnabled()) {
				logger.error("alertMessageAndReplace -> writeWithMessageConverters error, Exception: " + e);
			}
		} 
	}
}
