package it.smartcommunitylab.csengine.common;

public enum MobilityAttr {
	type("type"), //String
	duration("duration"), //String
	address("address"), //Address
	language("language"); //String
	
	public final String label;
	
	private MobilityAttr(String label) {
    this.label = label;
	}

}
