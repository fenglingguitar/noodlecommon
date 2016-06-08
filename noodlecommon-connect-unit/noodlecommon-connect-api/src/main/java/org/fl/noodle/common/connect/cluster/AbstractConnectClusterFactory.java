package org.fl.noodle.common.connect.cluster;

import org.fl.noodle.common.connect.distinguish.ConnectDistinguish;

public abstract class AbstractConnectClusterFactory implements ConnectClusterFactory {

	protected ConnectDistinguish connectDistinguish;

	public void setConnectDistinguish(ConnectDistinguish connectDistinguish) {
		this.connectDistinguish = connectDistinguish;
	}
}
