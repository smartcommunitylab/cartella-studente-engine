package it.smartcommunitylab.csengine.common;

public enum ExamAttr {
	type("type"),
	qualification("qualification"),
	honour("honour"),
	grade("grade"),
	result("result"),
	externalCandidate("externalCandidate");
	
	public final String label;
	
	private ExamAttr(String label) {
    this.label = label;
	}

}
