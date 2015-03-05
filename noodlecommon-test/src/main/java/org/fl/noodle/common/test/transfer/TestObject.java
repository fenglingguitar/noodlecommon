package org.fl.noodle.common.test.transfer;

import java.io.Serializable;
import java.util.Date;

public class TestObject implements Serializable {

	private static final long serialVersionUID = -1212853164232251154L;
	
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
				.append("byteTest:").append(byteTest).append(", ")
				.append("intTest:").append(intTest).append(", ")
				.append("intTest:").append(intTest).append(", ")
				.append("floatTest:").append(floatTest).append(", ")
				.append("doubleTest:").append(doubleTest).append(", ")
				.append("charTest:").append(charTest).append(", ")
				.append("booleanTest:").append(booleanTest).append(", ")
				.append("stringTest:").append(stringTest).append(", ")
				.append("byteArrayTest:").append(byteArrayTest).append(", ")
				.append("dateTest:").append(dateTest)
				.toString();
	}
	
	private transient static TestObject testObject;
	
	static {
		int caseByteLength = 256;
		byte[] caseByteArray = new byte[caseByteLength];
		for (int i=0; i<caseByteLength; i++) {
			caseByteArray[i] = (byte)0x41;
		}
		
		testObject = new TestObject();
		testObject.setByteTest(Byte.MAX_VALUE);
		testObject.setIntTest(Integer.MAX_VALUE);
		testObject.setLongTest(Long.MAX_VALUE);
		testObject.setFloatTest(Float.MAX_VALUE);
		testObject.setDoubleTest(Double.MAX_VALUE);
		testObject.setCharTest('1');
		testObject.setBooleanTest(true);
		testObject.setStringTest(new String(caseByteArray));
		testObject.setByteArrayTest(caseByteArray);
		testObject.setDateTest(new Date());
	}
	
	public static TestObject getInstence() {
		return testObject;
	}
}
