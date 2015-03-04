package org.fl.noodle.common.connect.route;

public class WeightConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new WeightConnectRoute();
	}
}
