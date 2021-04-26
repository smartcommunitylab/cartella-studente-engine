package it.smartcommunitylab.csengine.common;

public enum PersonAttr {
	fiscalCode("fiscalCode"),
	name("name"),
	surname("surname"),
	address("address"), //Address
	birthdate("birthdate"),
	phone("phone"),
	email("email");
	
	public final String label;
	
	private PersonAttr(String label) {
    this.label = label;
	}

}
