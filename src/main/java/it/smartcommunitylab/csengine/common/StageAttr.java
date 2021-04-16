package it.smartcommunitylab.csengine.common;

public enum StageAttr {
	TYPE("type"),
	DURATION("duration"),
	LOCATION("location");
	
	public final String label;
	
	private StageAttr(String label) {
    this.label = label;
	}

}
