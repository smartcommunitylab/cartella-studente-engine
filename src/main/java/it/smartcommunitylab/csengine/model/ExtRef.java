package it.smartcommunitylab.csengine.model;

public class ExtRef {
	private String extUri;
	private String origin;
	
	public ExtRef() {}
	
	public ExtRef(String extUri, String origin) {
		this.extUri = extUri;
		this.origin = origin;
	}
	
	public String getExtUri() {
		return extUri;
	}
	public void setExtUri(String extUri) {
		this.extUri = extUri;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
}
