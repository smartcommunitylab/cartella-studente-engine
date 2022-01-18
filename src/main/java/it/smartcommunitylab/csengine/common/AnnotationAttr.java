package it.smartcommunitylab.csengine.common;

public enum AnnotationAttr {
	note("note"),
	tags("tags");
	
	public final String label;
	
	private AnnotationAttr(String label) {
    this.label = label;
	}

}
