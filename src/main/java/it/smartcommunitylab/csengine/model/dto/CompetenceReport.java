package it.smartcommunitylab.csengine.model.dto;

import java.util.List;
import java.util.Map;

import it.smartcommunitylab.csengine.model.Experience;

public class CompetenceReport {
	Map<String, Object> competence;
	List<Experience> experiences;
	
	public Map<String, Object> getCompetence() {
		return competence;
	}
	public void setCompetence(Map<String, Object> competence) {
		this.competence = competence;
	}
	public List<Experience> getExperiences() {
		return experiences;
	}
	public void setExperiences(List<Experience> experiences) {
		this.experiences = experiences;
	}
}
