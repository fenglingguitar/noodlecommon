package org.fl.noodle.common.mvc;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fl.noodle.common.mvc.annotation.RequestParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;



import flexjson.JSONDeserializer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import flexjson.factories.ArrayObjectFactory;
import flexjson.transformer.DateTransformer;


public class MethodArgumentResolver implements HandlerMethodArgumentResolver {

	protected final Log logger = LogFactory.getLog(getClass());
	
	@SuppressWarnings("rawtypes")
	private JSONDeserializer jSONDeserializer = new JSONDeserializer();
	
	public MethodArgumentResolver() {
		jSONDeserializer.use(Date.class, new MyDateTransformer("yyyy-MM-dd HH:mm:ss.SSS"));
		jSONDeserializer.use(byte.class, new MyTransformer());
		jSONDeserializer.use(int.class, new MyTransformer());
		jSONDeserializer.use(long.class, new MyTransformer());
		jSONDeserializer.use(ArrayList.class, new ArrayObjectFactory());
	}
	
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(RequestParam.class) != null;
	}

	public Object resolveArgument(MethodParameter parameter,
								  ModelAndViewContainer mavContainer,
								  NativeWebRequest webRequest, 
								  WebDataBinderFactory binderFactory) throws Exception {
				
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
		
		Object object = null;
		
		if (requestParam.type().equals("json")) {
			String json = request.getParameter(requestParam.name());
			if (json != null && !json.equals("")) {
				if (logger.isDebugEnabled()) {
					logger.debug(json);
				}				
				Class<?> parameterType = parameter.getParameterType();
				if (parameterType.getConstructors().length > 0) {
					object = jSONDeserializer.deserialize(json, parameterType);
				} else {
					String className = parameterType.getCanonicalName();
					Class<?> T = Class.forName(className.substring(0,className.length()-2));
					object = (Object[]) jSONDeserializer.use("values", T).deserialize(json);
				}
			} else {
				Class<?> parameterType = parameter.getParameterType();
				if (parameterType.getConstructors().length > 0) {
					object = parameterType.newInstance();
				} else {
					String className = parameterType.getCanonicalName();
					Class<?> T = Class.forName(className.substring(0,className.length()-2));
					Object[] objects = (Object[]) Array.newInstance(T, (int) 0);
					object = objects;
				}
			}
		} else if (requestParam.type().equals("string")) {
			String param = request.getParameter(parameter.getParameterName());
			param = URLDecoder.decode(param, "utf-8");	
			object = (String) param;
		} else if (requestParam.type().equals("date")) {
			String param = request.getParameter(parameter.getParameterName());	
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date date = simpleDateFormat.parse(param);
			object = date;
		} else if (requestParam.type().equals("session")) {
			object = (HttpSession) request.getSession(true);
		} else {
			object = request.getParameter(parameter.getParameterName());
		}
		
		return object;
	}
	
	private class MyDateTransformer extends DateTransformer {
		
		public MyDateTransformer(String format) {
			super(format);
		}

		@SuppressWarnings("rawtypes")
		@Override
		public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
			
			String datatime = (String)value;
			if (datatime.length() > 0) {
				if (datatime.lastIndexOf(".") == -1) {
					value = datatime.concat(".000");
				}
			} else {
				return null;
			}
			
			return super.instantiate(context, value, targetType, targetClass);
		}
	}
	
	private class MyTransformer implements ObjectFactory {
		
		@SuppressWarnings("rawtypes")
		@Override
		public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
			Object object = null;
			if (targetType.equals(byte.class) && value.getClass().equals(String.class)) {
				if (value != null && !value.equals("")) {
					object = Byte.parseByte((String) value);
				} else {
					object = (byte) 0;
				}
			} else if (targetType.equals(int.class) && value.getClass().equals(String.class)) {
				if (value != null && !value.equals("")) {
					object = Integer.parseInt((String) value);
				} else {
					object = (int) 0;
				}
			} else if (targetType.equals(long.class) && value.getClass().equals(String.class)) {
				if (value != null && !value.equals("")) {
					object = Long.parseLong((String) value);
				} else {
					object = (long) 0;
				}
			} else {
				object = value;
			}
			return object;
		}
	}
}