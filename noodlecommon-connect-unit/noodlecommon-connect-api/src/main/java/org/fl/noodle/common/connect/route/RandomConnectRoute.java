package org.fl.noodle.common.connect.route;

import java.util.List;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class RandomConnectRoute extends AbstractConnectRoute {
	
	public RandomConnectRoute(String type) {
		super(type);
	}

	@Override
	protected ConnectAgent doSelectConnect(List<ConnectAgent> connectAgentList, String methodKey) {
		return null;
	}
}
