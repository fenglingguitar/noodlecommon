package org.fengling.noodlecommon.util.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonTranslator {

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
}
