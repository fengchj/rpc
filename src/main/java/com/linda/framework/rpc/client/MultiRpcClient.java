package com.linda.framework.rpc.client;

import com.linda.framework.rpc.net.RpcMultiConnector;
import com.linda.framework.rpc.nio.RpcNioConnector;

public class MultiRpcClient extends AbstractRpcClient{
	
	private MultiClientRemoteExecutor executor;
	private RpcMultiConnector connector;
	private int connections = 2;

	public AbstractClientRemoteExecutor getRemoteExecutor() {
		return executor;
	}
	
	public int getConnections() {
		return connections;
	}

	public void setConnections(int connections) {
		this.connections = connections;
	}

	@Override
	public void initConnector(int threadCount) {
		checkConnector();
		connector.setHost(host);
		connector.setPort(port);
		connector.setConnectionCount(connections);
		connector.setExecutorThreadCount(threadCount);
		executor = new MultiClientRemoteExecutor(connector);
	}

	private void checkConnector() {
		if(connector==null){
			connector = new RpcMultiConnector();
			if(connectorClass==null){
				connectorClass = RpcNioConnector.class;
			}
			connector.setConnectorClass(connectorClass);
		}
	}
}
