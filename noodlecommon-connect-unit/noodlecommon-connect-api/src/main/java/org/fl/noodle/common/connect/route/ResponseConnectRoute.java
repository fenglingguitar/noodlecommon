package org.fl.noodle.common.connect.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class ResponseConnectRoute implements ConnectRoute {

	private final Random random = new Random();
	
	@Override
	public ConnectAgent selectConnect(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected, String methodKey) {
		
		List<ConnectAgent> connectAgentListTemp = connectAgentList;
		
		if (connectAgentListSelected.size() > 0) {
			connectAgentListTemp = new ArrayList<ConnectAgent>(connectAgentList.size());
			connectAgentListTemp.addAll(connectAgentList);
			connectAgentListTemp.removeAll(connectAgentListSelected);
		}
		
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
		
		int connectAgentListTempSize = 0;
		
		while ((connectAgentListTempSize = connectAgentListTemp.size()) > 0) {
			try {
				return connectAgentList.get(random.nextInt(connectAgentListTempSize));
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		
		return null;
	}
}
