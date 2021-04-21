package it.smartcommunitylab.csengine.common;

public enum StageAttr {
	type("type"),
	duration("duration"),
	location("location");
	
	public final String label;
	
	private StageAttr(String label) {
    this.label = label;
	}

}
