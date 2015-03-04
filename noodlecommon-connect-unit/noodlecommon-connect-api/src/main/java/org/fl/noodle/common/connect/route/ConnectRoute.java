package org.fl.noodle.common.connect.route;

import java.util.List;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public interface ConnectRoute {
	
	public ConnectAgent selectConnect(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected, String methodKey);
}
