package it.smartcommunitylab.csengine.common;

public enum StageAttr {
	type("type"), //String
	duration("duration"), //String
	address("address"), //Address
	contact("contact"); //String
	
	public final String label;
	
	private StageAttr(String label) {
    this.label = label;
	}

}
