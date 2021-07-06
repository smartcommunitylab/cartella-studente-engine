package it.smartcommunitylab.csengine.common;

public enum PersonAttr {
	fiscalCode("fiscalCode"), //String
	name("name"), //String
	surname("surname"), //String
	address("address"), //Address
	birthdate("birthdate"), //String
	phone("phone"), //String
	email("email"); //String
	
	public final String label;
	
	private PersonAttr(String label) {
    this.label = label;
	}

}
