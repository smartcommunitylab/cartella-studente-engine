package it.smartcommunitylab.csengine.common;

public enum EducatinalActivityAttr {
	type("type"),
	duration("duration"),
	address("address"),
	institute("institute");
	
	public final String label;
	
	private EducatinalActivityAttr(String label) {
		this.label = label;
	}
}
