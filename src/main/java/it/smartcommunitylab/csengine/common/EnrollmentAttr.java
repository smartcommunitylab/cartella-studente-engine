package it.smartcommunitylab.csengine.common;

public enum EnrollmentAttr {
	schoolYear("schoolYear"), //String
	course("course"), //String
	classroom("classroom"); //String
	
	public final String label;
	
	private EnrollmentAttr(String label) {
    this.label = label;
	}

}
