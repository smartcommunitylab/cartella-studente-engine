package it.smartcommunitylab.csengine.common;

public enum EducationAttr {
	type("type"),
	duration("duration"),
	qualification("qualification"),
	honour("honour"),
	grade("grade"),
	dateExam("dateExam");
	
	public final String label;
	
	private EducationAttr(String label) {
		this.label = label;
	}

}
