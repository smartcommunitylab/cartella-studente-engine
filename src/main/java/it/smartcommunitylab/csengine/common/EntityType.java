package it.smartcommunitylab.csengine.common;

public enum EntityType {
	person("person"),
	exp("exp"),
	certification("certification"),
	educatinalActivity("educatinalActivity"),
	education("education");
	
	public final String label;
	
	private EntityType(String label) {
    this.label = label;
	}
}
