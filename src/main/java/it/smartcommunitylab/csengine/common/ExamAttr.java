package it.smartcommunitylab.csengine.common;

public enum ExamAttr {
	TYPE("type"),
	QUALIFICATION("qualification"),
	HONOUR("honour"),
	GRADE("grade"),
	RESULT("result"),
	EXTCANDIDATE("externalCandidate");
	
	public final String label;
	
	private ExamAttr(String label) {
    this.label = label;
	}

}
