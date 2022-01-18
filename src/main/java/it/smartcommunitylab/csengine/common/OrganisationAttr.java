package it.smartcommunitylab.csengine.common;

public enum OrganisationAttr {
	fiscalCode("fiscalCode"), //String
	name("name"), //String
	description("description"), //String
	address("address"), //Address
	location("location"), //GeoPoint
	phone("phone"), //String
	email("email"), //String
	pec("pec"), //String
	link("link"); //String
	
	public final String label;
	
	private OrganisationAttr(String label) {
    this.label = label;
	}

}
