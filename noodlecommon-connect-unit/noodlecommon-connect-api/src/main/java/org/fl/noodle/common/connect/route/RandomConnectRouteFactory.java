package org.fl.noodle.common.connect.route;

import org.fl.noodle.common.connect.constent.ConnectRouteType;

public class RandomConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new RandomConnectRoute(ConnectRouteType.RANDOM.getCode());
	}
}
