package org.fengling.noodlecommon.util.json;

import java.util.Date;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

public class JsonTranslator {

	private static SerializeConfig serializeConfig = new SerializeConfig();
	private static String dateFormat;
	static {
	    dateFormat = "yyyy-MM-dd HH:mm:ss";
	    serializeConfig.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
	}
	
	
	public static String toString(Object object) throws Exception {
		return JSON.toJSONString(object);
	}

	public static String toStringWithClassName(Object object) throws Exception {
		return JSON.toJSONString(object, SerializerFeature.WriteClassName);
	}

	public static byte[] toByteArray(Object object) throws Exception {
		return toString(object).getBytes();
	}

	public static byte[] toByteArrayWithClassName(Object object) throws Exception {
		return toStringWithClassName(object).getBytes();
	}

	
	public static <T> T fromString(String string, Class<T> clazz) throws Exception {
		return JSON.parseObject(string, clazz);
	}

	public static Object fromStringWithClassName(String string) throws Exception {
		return JSON.parse(string);
	}

	public static <T> T fromByteArray(byte[] byteArray, Class<T> clazz) throws Exception {
		return fromString(new String(byteArray), clazz);
	}

	public static Object fromByteArrayWithClassName(byte[] byteArray) throws Exception {
		return fromStringWithClassName(new String(byteArray));
	}
	
	
	public static String toStringWithDate(Object object) throws Exception {
		return JSON.toJSONStringWithDateFormat(object, dateFormat);
	}

	public static String toStringWithClassNameWithDate(Object object) throws Exception {
		return JSON.toJSONStringWithDateFormat(object, dateFormat, SerializerFeature.WriteClassName);
	}

	public static byte[] toByteArrayWithDate(Object object) throws Exception {
		return toStringWithDate(object).getBytes();
	}

	public static byte[] toByteArrayWithClassNameWithDate(Object object) throws Exception {
		return toStringWithClassNameWithDate(object).getBytes();
	}
}
