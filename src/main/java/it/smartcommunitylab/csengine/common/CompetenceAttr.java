package it.smartcommunitylab.csengine.common;

public enum CompetenceAttr {
	uri("uri"),
	concentType("concentType"), 
	code("code"),
	preferredLabel("preferredLabel"), //Map<String, String> (lang, value)
	altLabel("altLabel"), //Map<String, String> (lang, value)
	description("description"), //Map<String, String> (lang, value)
	validityFrom("validityFrom"), //String
	validityTo("validityTo"), //String
	assessmentType("assessmentType"), //String
	assessmentValue("assessmentValue"), //String
	assessmentValuePerc("assessmentValuePerc"), //Double
	certifyingOrganisation("certifyingOrganisation"); //Organisation_attr
	
	public final String label;
	
	private CompetenceAttr(String label) {
    this.label = label;
	}

}
