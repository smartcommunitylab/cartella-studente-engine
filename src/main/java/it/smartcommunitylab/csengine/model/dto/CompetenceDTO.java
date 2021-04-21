package it.smartcommunitylab.csengine.model.dto;

public class CompetenceDTO {
	private String id;
	private String uri;
	private String concentType;
	private String preferredLabel;
	private String altLabel;

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

	public String getPreferredLabel() {
		return preferredLabel;
	}

	public void setPreferredLabel(String preferredLabel) {
		this.preferredLabel = preferredLabel;
	}

	public String getAltLabel() {
		return altLabel;
	}

	public void setAltLabel(String altLabel) {
		this.altLabel = altLabel;
	}

}
