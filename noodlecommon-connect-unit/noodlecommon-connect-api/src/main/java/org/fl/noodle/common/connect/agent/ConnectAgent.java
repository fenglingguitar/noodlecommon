package org.fl.noodle.common.connect.agent;

public interface ConnectAgent {
	
	public void connect() throws Exception;
	public void reconnect() throws Exception;
	public void close();

	public boolean isHealthyConnect();	
	public long getConnectId();
	public boolean isSameConnect(String ip, int port, String url, String type);
	
	public Object getProxy();
	
	public void setWeight(int weight);
	public int getWeight();

	public void calculate();
	public long getAvgTime(String methodKey);
}
