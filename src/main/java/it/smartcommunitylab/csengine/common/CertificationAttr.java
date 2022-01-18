package it.smartcommunitylab.csengine.common;

public enum CertificationAttr {
	type("type"), //String
	duration("duration"), //String
	address("address"), //Address
	grade("grade"), //String
	language("language"), //String
	level("level"); //String
	
	public final String label;
	
	private CertificationAttr(String label) {
    this.label = label;
	}

}
