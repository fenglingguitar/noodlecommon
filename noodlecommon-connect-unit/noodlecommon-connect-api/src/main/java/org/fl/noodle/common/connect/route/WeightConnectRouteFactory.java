package org.fl.noodle.common.connect.route;

import org.fl.noodle.common.connect.constent.ConnectRouteType;

public class WeightConnectRouteFactory implements ConnectRouteFactory {

	@Override
	public ConnectRoute createConnectRoute() {
		return new WeightConnectRoute(ConnectRouteType.WEIGHT.getCode());
	}
}
