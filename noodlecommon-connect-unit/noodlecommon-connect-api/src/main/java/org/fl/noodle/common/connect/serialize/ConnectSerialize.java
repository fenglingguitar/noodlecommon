package org.fl.noodle.common.connect.serialize;

public interface ConnectSerialize {
	
	public String serializationToString(Object object) throws Exception;
	public byte[] serializationToByte(Object object) throws Exception;
	public <T> T deserializationFromString(String string, Class<T> clazz) throws Exception;
	public <T> T deserializationFromByte(byte[] byteArray, Class<T> clazz) throws Exception;
}
