package org.fl.noodle.common.monitor.performance.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpClient {
	
	private final static Logger logger = LoggerFactory.getLogger(UdpClient.class);
	
	private DatagramSocket datagramSocket;
	
	private String ip = "127.0.0.1";
	private int port = 12345;
	
	public UdpClient() {
	}
	
	public UdpClient(String ip, int port) throws Exception {
		this.port = port;
		this.ip = ip;
	}
	
	public void start() throws Exception {		
		datagramSocket = new DatagramSocket();
	}
	
	public void destroy() throws Exception {		
		datagramSocket.close();
	}
	
	public void send(String data) {
		
		byte[] sendBuf = data.getBytes();
		
		try {
			datagramSocket.send(new DatagramPacket(sendBuf, sendBuf.length, InetAddress.getByName(ip), port));
		} catch (Exception e) {
			logger.error("send -> datagramSocket.send -> ip:{}, port:{}, Exception:{}", ip, port, e.getMessage());
		}
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
