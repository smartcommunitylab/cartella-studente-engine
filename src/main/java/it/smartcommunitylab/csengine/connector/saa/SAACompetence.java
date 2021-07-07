package it.smartcommunitylab.csengine.connector.saa;

import java.util.HashMap;
import java.util.Map;

public class SAACompetence {
	private String uri;
	private String concentType;
	private Map<String, String> preferredLabel = new HashMap<>();
	private Map<String, String> altLabel = new HashMap<>();
	private Map<String, String> description = new HashMap<>();

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

	public Map<String, String> getDescription() {
		return description;
	}

	public void setDescription(Map<String, String> description) {
		this.description = description;
	}


}
