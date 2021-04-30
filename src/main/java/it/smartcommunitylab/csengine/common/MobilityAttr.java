package it.smartcommunitylab.csengine.common;

public enum MobilityAttr {
	type("type"),
	duration("duration"),
	address("address"), //Address
	language("language");
	
	public final String label;
	
	private MobilityAttr(String label) {
    this.label = label;
	}

}
