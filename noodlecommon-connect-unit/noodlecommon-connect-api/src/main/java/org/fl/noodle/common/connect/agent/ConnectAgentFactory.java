package org.fl.noodle.common.connect.agent;

public interface ConnectAgentFactory {
	public ConnectAgent createConnectAgent(long connectId, String ip, int port, String url);
	public ConnectAgent createConnectAgent(long connectId, String ip, int port, String url, int connectTimeout, int readTimeout);
}
