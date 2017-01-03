package org.fl.noodle.common.connect.route;

import java.util.List;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class ResponseConnectRoute extends AbstractConnectRoute {

	public ResponseConnectRoute(String type) {
		super(type);
	}

	@Override
	protected ConnectAgent doSelectConnect(List<ConnectAgent> connectAgentList, String methodKey) {
		
		long totalAvgTime = 0;
		boolean sameAvgTime = true;
		long lastAvgTime = -1;
		for (ConnectAgent connectAgent : connectAgentList) {
			long avgTime = connectAgent.getAvgTime(methodKey);
			totalAvgTime += avgTime;
			if (sameAvgTime && lastAvgTime != -1 && avgTime != lastAvgTime) {
                sameAvgTime = false; 
            }
			lastAvgTime = avgTime;
		}
		
        if (totalAvgTime > 0 && !sameAvgTime) {
            long offset = (long)(Math.random() * totalAvgTime * 2);
            for (ConnectAgent connectAgent : connectAgentList) {
            	offset -= (totalAvgTime - connectAgent.getAvgTime(methodKey));
                if (offset < 0) {
                    return connectAgent;
                }
            }
        }
		
		return null;
	}
}
