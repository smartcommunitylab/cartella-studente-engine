package it.smartcommunitylab.csengine.connector;

import java.util.ArrayList;
import java.util.List;

public class ConnectorConf {
	private String entityType;
	private String view;
	private int priority;
	private String implementor;
	private List<KeyMap> identityMap = new ArrayList<>();
	
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getView() {
		return view;
	}
	public void setView(String view) {
		this.view = view;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public String getImplementor() {
		return implementor;
	}
	public void setImplementor(String implementor) {
		this.implementor = implementor;
	}
	public List<KeyMap> getIdentityMap() {
		return identityMap;
	}
	public void setIdentityMap(List<KeyMap> identityMap) {
		this.identityMap = identityMap;
	}
}
