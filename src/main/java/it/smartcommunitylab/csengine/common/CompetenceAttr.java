package it.smartcommunitylab.csengine.common;

public enum CompetenceAttr {
	uri("uri"),
	concentType("concentType"),
	preferredLabel("preferredLabel"),
	altLabel("altLabel"), //Address
	validityFrom("validityFrom"), //GeoJsonPoint
	validityTo("validityTo"),
	assessment("assessment"),
	certifyingOrganisation("certifyingOrganisation");
	
	public final String label;
	
	private CompetenceAttr(String label) {
    this.label = label;
	}

}
