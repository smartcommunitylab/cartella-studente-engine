package it.smartcommunitylab.csengine.model;

import java.util.HashMap;
import java.util.Map;

public class DataView {
	private ExtRef identity;
	private Map<String, Object> attributes = new HashMap<>();
	
	public ExtRef getIdentity() {
		return identity;
	}
	public void setIdentity(ExtRef identity) {
		this.identity = identity;
	}
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}
	
}
