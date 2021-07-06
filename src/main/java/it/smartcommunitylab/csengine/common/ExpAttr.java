package it.smartcommunitylab.csengine.common;

public enum ExpAttr {
	title("title"), //String
	description("description"), //String
	location("location"), //GeoPoint
	dateFrom("dateFrom"), //String
	dateTo("dateTo"), //String
	validityFrom("validityFrom"), //String
	validityTo("validityTo"), //String
	competences("competences"), //List<Compatence_attr>
	organisation("organisation"); //Organisation_attr
	
	public final String label;
	
	private ExpAttr(String label) {
    this.label = label;
	}

}
