package it.smartcommunitylab.csengine.common;

public enum EntityType {
	exp("exp"),
	stage("stage"),
	exam("exam");
	
	public final String label;
	
	private EntityType(String label) {
    this.label = label;
	}
}
