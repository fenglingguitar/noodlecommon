package org.fl.noodle.common.connect.route;

public class RandomConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new RandomConnectRoute();
	}
}
