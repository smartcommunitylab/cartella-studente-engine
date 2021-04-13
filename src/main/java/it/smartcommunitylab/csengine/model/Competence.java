package it.smartcommunitylab.csengine.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Competence {
	@Id
	private String id;
	private String uri;
	private String concentType;
	private Map<String, String> preferredLabel;
	private Map<String, String> altLabel;
	private Map<String, DataView> views;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getConcentType() {
		return concentType;
	}
	public void setConcentType(String concentType) {
		this.concentType = concentType;
	}
	public Map<String, String> getPreferredLabel() {
		return preferredLabel;
	}
	public void setPreferredLabel(Map<String, String> preferredLabel) {
		this.preferredLabel = preferredLabel;
	}
	public Map<String, String> getAltLabel() {
		return altLabel;
	}
	public void setAltLabel(Map<String, String> altLabel) {
		this.altLabel = altLabel;
	}
	public Map<String, DataView> getViews() {
		return views;
	}
	public void setViews(Map<String, DataView> views) {
		this.views = views;
	}

}
