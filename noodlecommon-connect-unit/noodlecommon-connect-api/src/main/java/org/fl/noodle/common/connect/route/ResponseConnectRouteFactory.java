package org.fl.noodle.common.connect.route;

import org.fl.noodle.common.connect.constent.ConnectRouteType;

public class ResponseConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new ResponseConnectRoute(ConnectRouteType.RESPONSE.getCode());
	}
}
