package it.smartcommunitylab.csengine.common;

public enum EnrollmentAttr {
	schoolYear("schoolYear"),
	course("course"),
	classroom("classroom");
	
	public final String label;
	
	private EnrollmentAttr(String label) {
    this.label = label;
	}

}
