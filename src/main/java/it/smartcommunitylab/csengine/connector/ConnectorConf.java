package it.smartcommunitylab.csengine.connector;

public class ConnectorConf {
	private String entityType;
	private String view;
	private int priority;
	private String implementor;
	
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
}
