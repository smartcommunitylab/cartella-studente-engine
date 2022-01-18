package it.smartcommunitylab.csengine.common;

public enum EntityType {
	person("person"),
	exp("exp"),
	certification("certification"),
	educationalActivity("educationalActivity"),
	education("education");
	
	public final String label;
	
	private EntityType(String label) {
    this.label = label;
	}
}
