package it.smartcommunitylab.csengine.common;

public enum GeoPointAttr {
	latitude("latitude"),
	longitude("longitude");
	
	public final String label;
	
	private GeoPointAttr(String label) {
    this.label = label;
	}

}
