package org.fl.noodle.common.net.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketConnect {

	private Socket socket;
	private OutputStream outputStream = null;
	private InputStream inputStream = null;
	
	private String ip;
	private int port;
	private int connectTimeout;
	private int readTimeout;
	private String encoding = "utf-8";
	
	public SocketConnect(String ip, int port, int connectTimeout, int readTimeout) {
		this.ip = ip;
		this.port = port;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
	}
	
	public SocketConnect(String ip, int port, int connectTimeout, int readTimeout, String encoding) {
		this.ip = ip;
		this.port = port;
		this.connectTimeout = connectTimeout;
		this.readTimeout = readTimeout;
		this.encoding = encoding;
	}

	public void connect() throws IOException {
		if (!isConnected()) {
			socket = new Socket();
            socket.setReuseAddress(true);
            socket.setKeepAlive(true);
            socket.setTcpNoDelay(true);
            socket.setSoLinger(true, 0);
            socket.connect(new InetSocketAddress(ip, port), connectTimeout);
            socket.setSoTimeout(readTimeout);
            outputStream = socket.getOutputStream(); 
			inputStream = socket.getInputStream(); 
		}
	}

	public void close() throws IOException {
		if (isConnected()) {
			outputStream.close();
			inputStream.close();
			if (!socket.isClosed()) {
                socket.close();
            }
		}
	}
	
	public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }
	
	public String send(String name, String data, int readTimeout) throws Exception {
		
		connect();
		
		socket.setSoTimeout(readTimeout);
		
		byte[] bName = name.getBytes(encoding);
		byte[] bData = data.getBytes(encoding);
		
		byte[] sizeBuf = new byte[4];
		
		sizeBuf[0] = (byte) ((bName.length >>> 24) & 0xFF);
		sizeBuf[1] = (byte) ((bName.length >>> 16) & 0xFF);
		sizeBuf[2] = (byte) ((bName.length >>>  8) & 0xFF);
		sizeBuf[3] = (byte) ((bName.length >>>  0) & 0xFF);
		
		outputStream.write(sizeBuf, 0, 4);
		outputStream.write(bName, 0, bName.length);
		
		sizeBuf[0] = (byte) ((bData.length >>> 24) & 0xFF);
		sizeBuf[1] = (byte) ((bData.length >>> 16) & 0xFF);
		sizeBuf[2] = (byte) ((bData.length >>>  8) & 0xFF);
		sizeBuf[3] = (byte) ((bData.length >>>  0) & 0xFF);
		
		outputStream.write(sizeBuf, 0, 4);
		outputStream.write(bData, 0, bData.length);
		
		int recvSizeBufSize = 0;
		int recvSizeBufNextSize = 0;
		do {
			recvSizeBufNextSize = 4 - recvSizeBufSize;
			int size = inputStream.read(sizeBuf, recvSizeBufSize, recvSizeBufNextSize);
			if (size == -1) {
				throw new java.net.SocketException();
			}
			recvSizeBufSize += size;
		} while (recvSizeBufSize < 4);
		
		int bufRecvSize = 
				((sizeBuf[0] & 255) << 24) +
                ((sizeBuf[1] & 255) << 16) +
                ((sizeBuf[2] & 255) <<  8) +
                ((sizeBuf[3] & 255) <<  0);
		int bufRecvSizeApply = (bufRecvSize / 64 + 1) * 64;
		
		byte[] recvBuf = new byte[bufRecvSizeApply];
		
		int recvBufSize = 0;
		int recvBufNextSize = 0;
		do {
			recvBufNextSize = bufRecvSize - recvBufSize;
			int size = inputStream.read(recvBuf, recvBufSize, recvBufNextSize);
			if (size == -1) {
				throw new java.net.SocketException();
			}
			recvBufSize += size;
		} while (recvBufSize < bufRecvSize);
		
		return new String(recvBuf, 0, bufRecvSize, encoding);
	}
}
