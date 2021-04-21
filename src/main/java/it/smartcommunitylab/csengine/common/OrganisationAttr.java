package it.smartcommunitylab.csengine.common;

public enum OrganisationAttr {
	fiscalCode("fiscalCode"),
	name("name"),
	description("description"),
	address("address"), //Address
	location("location"), //GeoJsonPoint
	phone("phone"),
	email("email"),
	pec("pec");
	
	public final String label;
	
	private OrganisationAttr(String label) {
    this.label = label;
	}

}
