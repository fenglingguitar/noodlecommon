package org.fl.noodle.common.connect.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class WeightConnectRoute implements ConnectRoute {

	private final Random random = new Random();
	
	@Override
	public ConnectAgent selectConnect(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected, String methodKey) {
		
		List<ConnectAgent> connectAgentListTemp = connectAgentList;
		
		if (connectAgentListSelected.size() > 0) {
			connectAgentListTemp = new ArrayList<ConnectAgent>(connectAgentList.size());
			connectAgentListTemp.addAll(connectAgentList);
			connectAgentListTemp.removeAll(connectAgentListSelected);
		}
		
		int totalWeight = 0;
		boolean sameWeight = true;
		int lastWeight = -1;
		for (ConnectAgent connectAgent : connectAgentList) {
			int weight = connectAgent.getWeight();
			totalWeight += weight;
			if (sameWeight && lastWeight != -1 && weight != lastWeight) {
                sameWeight = false; 
            }
			lastWeight = weight;
		}
		
        if (totalWeight > 0 && !sameWeight) {
            int offset = random.nextInt(totalWeight);
            for (ConnectAgent connectAgent : connectAgentList) {
            	offset -= connectAgent.getWeight();
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
