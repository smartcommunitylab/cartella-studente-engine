package it.smartcommunitylab.csengine.common;

public enum ExamAttr {
	type("type"), //String
	qualification("qualification"), //String
	honour("honour"), //Boolean
	grade("grade"), //String
	result("result"), //Boolean
	externalCandidate("externalCandidate"); //Boolean
	
	public final String label;
	
	private ExamAttr(String label) {
    this.label = label;
	}

}
