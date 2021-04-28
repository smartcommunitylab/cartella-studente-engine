package it.smartcommunitylab.csengine.common;

public enum CompetenceAttr {
	uri("uri"),
	concentType("concentType"), 
	preferredLabel("preferredLabel"), //Map<String, String> (lang, value)
	altLabel("altLabel"), //Map<String, String> (lang, value)
	validityFrom("validityFrom"),
	validityTo("validityTo"),
	assessment("assessment"),
	certifyingOrganisation("certifyingOrganisation"); //Organisation_attr
	
	public final String label;
	
	private CompetenceAttr(String label) {
    this.label = label;
	}

}
