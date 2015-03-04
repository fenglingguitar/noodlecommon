package org.fl.noodle.common.connect.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public class RandomConnectRoute implements ConnectRoute {

	private final Random random = new Random();
	
	@Override
	public ConnectAgent selectConnect(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected, String methodKey) {
		
		List<ConnectAgent> connectAgentListTemp = connectAgentList;
		
		if (connectAgentListSelected.size() > 0) {
			connectAgentListTemp = new ArrayList<ConnectAgent>(connectAgentList.size());
			connectAgentListTemp.addAll(connectAgentList);
			connectAgentListTemp.removeAll(connectAgentListSelected);
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
