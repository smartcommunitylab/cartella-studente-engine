package it.smartcommunitylab.csengine.common;

public enum ExpAttr {
	id("id"),
	title("title"),
	description("description"),
	location("location"),
	dateFrom("dateFrom"),
	dateTo("dateTo"),
	validityFrom("validityFrom"),
	validityTo("validityTo"),
	competences("competences"), //List<Compatence>
	organisation("organisation"); //Organisation
	
	public final String label;
	
	private ExpAttr(String label) {
    this.label = label;
	}

}
