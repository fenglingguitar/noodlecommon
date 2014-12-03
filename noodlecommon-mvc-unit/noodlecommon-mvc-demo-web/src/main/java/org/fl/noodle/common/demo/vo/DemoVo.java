package org.fl.noodle.common.demo.vo;

import java.util.Date;

public class DemoVo implements java.io.Serializable {

	private static final long serialVersionUID = -1L;

	private long id;
	private byte byteTest;
	//transient private Byte byteClassTest;
	private Byte byteClassTest;
	private int intTest;
	private Integer intClassTest;
	private long longTest;
	private Long longClassTest;
	private float floatTest;
	//transient private Float floatClassTest;
	private Float floatClassTest;
	private double doubleTest;
	//transient private Double doubleClassTest;
	private Double doubleClassTest;
	private char charTest;
	private boolean booleanTest;
	private Boolean booleanClassTest;
	private String stringTest;
	private byte[] byteArrayTest;
	private Date dateTest;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	
	public byte getByteTest() {
		return byteTest;
	}
	public void setByteTest(byte byteTest) {
		this.byteTest = byteTest;
	}
	
	public Byte getByteClassTest() {
		return byteClassTest;
	}
	public void setByteClassTest(Byte byteClassTest) {
		this.byteClassTest = byteClassTest;
	}
	
	public int getIntTest() {
		return intTest;
	}
	public void setIntTest(int intTest) {
		this.intTest = intTest;
	}
	
	public Integer getIntClassTest() {
		return intClassTest;
	}
	public void setIntClassTest(Integer intClassTest) {
		this.intClassTest = intClassTest;
	}
	
	public long getLongTest() {
		return longTest;
	}
	public void setLongTest(long longTest) {
		this.longTest = longTest;
	}
	
	public Long getLongClassTest() {
		return longClassTest;
	}
	public void setLongClassTest(Long longClassTest) {
		this.longClassTest = longClassTest;
	}
	
	public float getFloatTest() {
		return floatTest;
	}
	public void setFloatTest(float floatTest) {
		this.floatTest = floatTest;
	}
	
	public Float getFloatClassTest() {
		return floatClassTest;
	}
	public void setFloatClassTest(Float floatClassTest) {
		this.floatClassTest = floatClassTest;
	}
	
	public double getDoubleTest() {
		return doubleTest;
	}
	public void setDoubleTest(double doubleTest) {
		this.doubleTest = doubleTest;
	}
	
	public Double getDoubleClassTest() {
		return doubleClassTest;
	}
	public void setDoubleClassTest(Double doubleClassTest) {
		this.doubleClassTest = doubleClassTest;
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
	
	public Boolean getBooleanClassTest() {
		return booleanClassTest;
	}
	public void setBooleanClassTest(Boolean booleanClassTest) {
		this.booleanClassTest = booleanClassTest;
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
				.append("id:").append(id).append(",")
				.append("byteTest:").append(byteTest).append(",")
				.append("byteClassTest:").append(byteClassTest).append(",")
				.append("intTest:").append(intTest).append(",")
				.append("intClassTest:").append(intClassTest).append(",")
				.append("longTest:").append(longTest).append(",")
				.append("longClassTest:").append(longClassTest).append(",")
				.append("floatTest:").append(floatTest).append(",")
				.append("floatClassTest:").append(floatClassTest).append(",")
				.append("doubleTest:").append(doubleTest).append(",")
				.append("doubleClassTest:").append(doubleClassTest).append(",")
				.append("charTest:").append(charTest).append(",")
				.append("booleanTest:").append(booleanTest).append(",")
				.append("booleanClassTest:").append(booleanClassTest).append(",")
				.append("stringTest:").append(stringTest).append(",")
				.append("byteArrayTest:").append(byteArrayTest).append(",")
				.append("dateTest:").append(dateTest != null ? (new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(dateTest) : null)
				.toString();
	}
}
