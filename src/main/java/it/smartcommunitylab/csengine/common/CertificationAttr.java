package it.smartcommunitylab.csengine.common;

public enum CertificationAttr {
	type("type"),
	duration("duration"),
	address("address"), //Address
	contact("contact"),
	grade("grade"),
	language("language"),
	level("level");
	
	public final String label;
	
	private CertificationAttr(String label) {
    this.label = label;
	}

}
