package org.fl.noodle.common.connect.route;

public class ResponseConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new ResponseConnectRoute();
	}
}
