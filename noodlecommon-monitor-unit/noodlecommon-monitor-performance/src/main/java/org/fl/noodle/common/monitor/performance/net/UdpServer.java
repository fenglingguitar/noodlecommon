package org.fl.noodle.common.monitor.performance.net;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.fl.noodle.common.util.thread.ExecutorThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServer {
	
	private final static Logger logger = LoggerFactory.getLogger(UdpServer.class);
	
	private DatagramSocket datagramSocket;

	private volatile boolean stopSign = false;
	
	private int port = 12345;
	
	private int bufSize = 2048;
	private int threadCountRecv = 10;
	private long getInterval = 1000;
	private int soRcvbuf = 131072;
	
	private BlockingQueue<String> recvList = new LinkedBlockingDeque<String>();
	private ExecutorService executorServiceWait = null;
	
	public UdpServer() {
	}
	
	public UdpServer(int port) throws Exception {
		this.port = port;
	}
	
	public void start() throws Exception {		
		
		datagramSocket = new DatagramSocket(null);
		datagramSocket.setReceiveBufferSize(soRcvbuf);
		datagramSocket.bind(new InetSocketAddress(port));
		
		executorServiceWait = Executors.newFixedThreadPool(threadCountRecv, new ExecutorThreadFactory(this.getClass().getName(), true));
		
		for (int i=0; i<threadCountRecv; i++) {
			executorServiceWait.execute(new Runnable() {
				@Override
				public void run() {
					byte[] recvBuf = new byte[bufSize];
					DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
					while (!stopSign) {
						try {
							datagramSocket.receive(recvPacket);
							String recvStr = new String(recvPacket.getData(), 0, recvPacket.getLength());
							recvList.put(recvStr);
						} catch (Exception e) {
							logger.error("run -> datagramSocket.receive -> port:{}, Exception:{}", port, e.getMessage());
						}
					}
					datagramSocket.close();
				}
			});
		}
	}
	
	public void destroy() throws Exception {	
		stopSign = true;
		executorServiceWait.shutdown();
	}
	
	public String get() {
		String recvStr = null; 
		try {
			recvStr = recvList.poll(getInterval, TimeUnit.MILLISECONDS);
		} catch (Exception e) {
			logger.error("get -> recvList.poll -> port:{}, Exception:{}", port, e.getMessage());
		}
		return recvStr;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public void setBufSize(int bufSize) {
		this.bufSize = bufSize;
	}
	
	public void setThreadCountRecv(int threadCountRecv) {
		this.threadCountRecv = threadCountRecv;
	}
	
	public void setGetInterval(long getInterval) {
		this.getInterval = getInterval;
	}

	public void setSoRcvbuf(int soRcvbuf) {
		this.soRcvbuf = soRcvbuf;
	}
}
