package it.smartcommunitylab.csengine.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ExpCompetence {
	@Id
	private String id;
	private String personId;
	private String experienceId;
	private String competenceId;
	
	public ExpCompetence() {}
	
	public ExpCompetence(String personId, String experienceId, String competenceId) {
		this.personId = personId;
		this.experienceId = experienceId;
		this.competenceId = competenceId;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getExperienceId() {
		return experienceId;
	}
	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}
	public String getCompetenceId() {
		return competenceId;
	}
	public void setCompetenceId(String competenceId) {
		this.competenceId = competenceId;
	}

}
