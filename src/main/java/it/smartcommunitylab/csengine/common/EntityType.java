package it.smartcommunitylab.csengine.common;

public enum EntityType {
	STAGE("stage"),
	EXAM("exam");
	
	public final String label;
	
	private EntityType(String label) {
    this.label = label;
	}
}
