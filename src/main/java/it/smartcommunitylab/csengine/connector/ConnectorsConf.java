package it.smartcommunitylab.csengine.connector;

import java.util.List;
import java.util.Map;

public class ConnectorsConf {
	private List<ConnectorConf> connectors;
	private Map<String, List<Map<String, String>>> identityMap;
	
	public List<ConnectorConf> getConnectors() {
		return connectors;
	}
	public void setConnectors(List<ConnectorConf> connectors) {
		this.connectors = connectors;
	}
	public Map<String, List<Map<String, String>>> getIdentityMap() {
		return identityMap;
	}
	public void setIdentityMap(Map<String, List<Map<String, String>>> identityMap) {
		this.identityMap = identityMap;
	}
	
}
