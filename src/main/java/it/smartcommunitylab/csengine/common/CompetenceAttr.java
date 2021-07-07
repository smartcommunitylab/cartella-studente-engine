package it.smartcommunitylab.csengine.common;

public enum CompetenceAttr {
	uri("uri"),
	concentType("concentType"), 
	preferredLabel("preferredLabel"), //Map<String, String> (lang, value)
	altLabel("altLabel"), //Map<String, String> (lang, value)
	description("description"), //Map<String, String> (lang, value)
	validityFrom("validityFrom"), //String
	validityTo("validityTo"), //String
	assessment("assessment"), //String
	certifyingOrganisation("certifyingOrganisation"); //Organisation_attr
	
	public final String label;
	
	private CompetenceAttr(String label) {
    this.label = label;
	}

}
