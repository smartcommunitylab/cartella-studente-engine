package it.smartcommunitylab.csengine.common;

public enum EntityType {
	person("person"),
	exp("exp"),
	stage("stage"),
	exam("exam"),
	certification("certification"),
	mobility("mobility"),
	enrollment("enrollment");
	
	public final String label;
	
	private EntityType(String label) {
    this.label = label;
	}
}
