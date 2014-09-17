package org.fengling.noodlecommon.util.json;

import java.util.Date;

import org.junit.Test;

public class JsonTranslatorTest {

	private static CaseObject caseObject;
	
	static {
		int caseByteLength = 256;
		byte[] caseByteArray = new byte[caseByteLength];
		for (int i=0; i<caseByteLength; i++) {
			caseByteArray[i] = (byte)0x41;
		}
		
		caseObject = new CaseObject();
		caseObject.setByteTest(Byte.MAX_VALUE);
		caseObject.setIntTest(Integer.MAX_VALUE);
		caseObject.setLongTest(Long.MAX_VALUE);
		caseObject.setFloatTest(Float.MAX_VALUE);
		caseObject.setDoubleTest(Double.MAX_VALUE);
		caseObject.setCharTest('1');
		caseObject.setBooleanTest(true);
		caseObject.setStringTest(new String(caseByteArray));
		caseObject.setByteArrayTest(caseByteArray);
		caseObject.setDateTest(new Date());
	}
	
	@Test
	public void testToStringObject() throws Exception {
		String jsonStr = JsonTranslator.toString(caseObject);
		System.out.println("toString json string: " + jsonStr);
		System.out.println("toString json string size: " + jsonStr.length());
	}

	@Test
	public void testToStringWithClassName() throws Exception {
		String jsonStr = JsonTranslator.toStringWithClassName(caseObject);
		System.out.println("toStringWithClassName json string: " + jsonStr);
		System.out.println("toStringWithClassName json string size: " + jsonStr.length());
	}

	@Test
	public void testToByteArray() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArray(caseObject);
		System.out.println("toByteArray json string: " + jsonByte);
		System.out.println("toByteArray json string size: " + jsonByte.length);
	}

	@Test
	public void testToByteArrayWithClassName() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArrayWithClassName(caseObject);
		System.out.println("toByteArrayWithClassName json string: " + jsonByte);
		System.out.println("toByteArrayWithClassName json string size: " + jsonByte.length);
	}

	@Test
	public void testFromString() throws Exception {
		String jsonStr = JsonTranslator.toString(caseObject);
		CaseObject caseObjectResult = JsonTranslator.fromString(jsonStr, CaseObject.class);
		System.out.println("fromString json string: " + caseObjectResult);
	}

	@Test
	public void testFromStringWithClassName() throws Exception {
		String jsonStr = JsonTranslator.toStringWithClassName(caseObject);
		CaseObject caseObjectResult = (CaseObject) JsonTranslator.fromStringWithClassName(jsonStr);
		System.out.println("fromStringWithClassName json string: " + caseObjectResult);
	}

	@Test
	public void testFromByteArray() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArray(caseObject);
		CaseObject caseObjectResult = JsonTranslator.fromByteArray(jsonByte, CaseObject.class);
		System.out.println("fromByteArray json string: " + caseObjectResult);
	}

	@Test
	public void testFromByteArrayWithClassName() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArrayWithClassName(caseObject);
		CaseObject caseObjectResult = (CaseObject) JsonTranslator.fromByteArrayWithClassName(jsonByte);
		System.out.println("fromByteArrayWithClassName json string: " + caseObjectResult);
	}

	@Test
	public void testToStringObjectWithDate() throws Exception {
		String jsonStr = JsonTranslator.toStringWithDate(caseObject);
		System.out.println("toStringWithDate json string: " + jsonStr);
		System.out.println("toStringWithDate json string size: " + jsonStr.length());
		CaseObject caseObjectResult = JsonTranslator.fromString(jsonStr, CaseObject.class);
		System.out.println("toStringWithDate json object: " + caseObjectResult);
	}

	@Test
	public void testToStringWithClassNameWithDate() throws Exception {
		String jsonStr = JsonTranslator.toStringWithClassNameWithDate(caseObject);
		System.out.println("toStringWithClassNameWithDate json string: " + jsonStr);
		System.out.println("toStringWithClassNameWithDate json string size: " + jsonStr.length());
		CaseObject caseObjectResult = (CaseObject) JsonTranslator.fromStringWithClassName(jsonStr);
		System.out.println("toStringWithClassNameWithDate json object: " + caseObjectResult);
	}

	@Test
	public void testToByteArrayWithDate() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArrayWithDate(caseObject);
		System.out.println("toByteArrayWithDate json string: " + jsonByte);
		System.out.println("toByteArrayWithDate json string size: " + jsonByte.length);
		CaseObject caseObjectResult = JsonTranslator.fromByteArray(jsonByte, CaseObject.class);
		System.out.println("toByteArrayWithDate json object: " + caseObjectResult);
	}

	@Test
	public void testToByteArrayWithClassNameWithDate() throws Exception {
		byte[] jsonByte = JsonTranslator.toByteArrayWithClassNameWithDate(caseObject);
		System.out.println("toByteArrayWithClassNameWithDate json string: " + jsonByte);
		System.out.println("toByteArrayWithClassNameWithDate json string size: " + jsonByte.length);
		CaseObject caseObjectResult = (CaseObject) JsonTranslator.fromByteArrayWithClassName(jsonByte);
		System.out.println("toByteArrayWithClassNameWithDate json object: " + caseObjectResult);
	}
	
	public static class CaseObject implements java.io.Serializable {
		
		private static final long serialVersionUID = -1L;
		
		private byte byteTest;
		private int intTest;
		private long longTest;
		private float floatTest;
		private double doubleTest;
		private char charTest;
		private boolean booleanTest;
		private String stringTest;
		private byte[] byteArrayTest;
		private Date dateTest;
		
		public byte getByteTest() {
			return byteTest;
		}
		public void setByteTest(byte byteTest) {
			this.byteTest = byteTest;
		}
		public int getIntTest() {
			return intTest;
		}
		public void setIntTest(int intTest) {
			this.intTest = intTest;
		}
		public long getLongTest() {
			return longTest;
		}
		public void setLongTest(long longTest) {
			this.longTest = longTest;
		}
		public float getFloatTest() {
			return floatTest;
		}
		public void setFloatTest(float floatTest) {
			this.floatTest = floatTest;
		}
		public double getDoubleTest() {
			return doubleTest;
		}
		public void setDoubleTest(double doubleTest) {
			this.doubleTest = doubleTest;
		}
		public char getCharTest() {
			return charTest;
		}
		public void setCharTest(char charTest) {
			this.charTest = charTest;
		}
		public boolean isBooleanTest() {
			return booleanTest;
		}
		public void setBooleanTest(boolean booleanTest) {
			this.booleanTest = booleanTest;
		}
		public String getStringTest() {
			return stringTest;
		}
		public void setStringTest(String stringTest) {
			this.stringTest = stringTest;
		}
		public byte[] getByteArrayTest() {
			return byteArrayTest;
		}
		public void setByteArrayTest(byte[] byteArrayTest) {
			this.byteArrayTest = byteArrayTest;
		}
		public Date getDateTest() {
			return dateTest;
		}
		public void setDateTest(Date dateTest) {
			this.dateTest = dateTest;
		}

		public String toString() {
			return (new StringBuilder())
					.append("[")
					.append("byteTest:").append(byteTest).append(", ")
					.append("intTest:").append(intTest).append(", ")
					.append("longTest:").append(longTest).append(", ")
					.append("floatTest:").append(floatTest).append(", ")
					.append("doubleTest:").append(doubleTest).append(", ")
					.append("charTest:").append(charTest).append(", ")
					.append("booleanTest:").append(booleanTest).append(", ")
					.append("stringTest:").append(stringTest).append(", ")
					.append("byteArrayTest:").append(byteArrayTest).append(", ")
					.append("dateTest:").append(dateTest)
					.append("]")
					.toString();
		}
	}
}
