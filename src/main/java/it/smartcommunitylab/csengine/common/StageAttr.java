package it.smartcommunitylab.csengine.common;

public enum StageAttr {
	type("type"),
	duration("duration"),
	address("address"); //Address
	
	public final String label;
	
	private StageAttr(String label) {
    this.label = label;
	}

}
