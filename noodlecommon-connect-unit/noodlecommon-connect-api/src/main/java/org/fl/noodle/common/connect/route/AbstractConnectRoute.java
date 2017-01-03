package org.fl.noodle.common.connect.route;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.fl.noodle.common.connect.agent.ConnectAgent;

public abstract class AbstractConnectRoute implements ConnectRoute {
	
	protected String type;
	
	public AbstractConnectRoute(String type) {
		this.type = type;
	}

	@Override
	public ConnectAgent selectConnect(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected, String methodKey) {
		
		List<ConnectAgent> connectAgentListTemp = getValidconnectAgentList(connectAgentList, connectAgentListSelected);
		if (connectAgentListTemp.size() == 0) {
			return null;
		}
		
		ConnectAgent connectAgent = doSelectConnect(connectAgentListTemp, methodKey);
		
		if (connectAgent != null) {
			return connectAgent;
		}
		
		return randomSelectConnect(connectAgentListTemp);
	}
	
	protected abstract ConnectAgent doSelectConnect(List<ConnectAgent> connectAgentList, String methodKey);
	
	protected List<ConnectAgent> getValidconnectAgentList(List<ConnectAgent> connectAgentList, List<ConnectAgent> connectAgentListSelected) {
		
		List<ConnectAgent> connectAgentListTemp = connectAgentList;
		
		if (connectAgentListSelected.size() > 0) {
			connectAgentListTemp = new ArrayList<ConnectAgent>(connectAgentList.size());
			connectAgentListTemp.addAll(connectAgentList);
			connectAgentListTemp.removeAll(connectAgentListSelected);
		}
		
		return connectAgentListTemp;
	}
	
	protected ConnectAgent randomSelectConnect(List<ConnectAgent> connectAgentList) {
		
		Random random = new Random();
		
		int connectAgentListTempSize = 0;
		
		while ((connectAgentListTempSize = connectAgentList.size()) > 0) {
			try {
				return connectAgentList.get(random.nextInt(connectAgentListTempSize));
			} catch (ArrayIndexOutOfBoundsException e) {
				continue;
			}
		}
		
		return null;
	}
	
	@Override
	public String getType() {
		return type;
	}
}
