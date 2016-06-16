package org.fl.noodle.common.connect.route;

import java.util.List;
import java.util.Random;
import org.fl.noodle.common.connect.agent.ConnectAgent;

public class WeightConnectRoute extends AbstractConnectRoute {

	@Override
	protected ConnectAgent doSelectConnect(List<ConnectAgent> connectAgentList, String methodKey) {
		
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
        	Random random = new Random();
            int offset = random.nextInt(totalWeight);
            for (ConnectAgent connectAgent : connectAgentList) {
            	offset -= connectAgent.getWeight();
                if (offset < 0) {
                    return connectAgent;
                }
            }
        }
        
		return null;
	}
}
