package it.smartcommunitylab.csengine.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

@Document
public class ExpCompetence {
	@Id
	private String id;
	private String personId;
	private String experienceId;
	private String competenceId;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validityFrom;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validityTo;
	
	private String assessment;
	
	private String certifyingOrganisationId;
	
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

	public LocalDate getValidityFrom() {
		return validityFrom;
	}

	public void setValidityFrom(LocalDate validityFrom) {
		this.validityFrom = validityFrom;
	}

	public LocalDate getValidityTo() {
		return validityTo;
	}

	public void setValidityTo(LocalDate validityTo) {
		this.validityTo = validityTo;
	}

	public String getAssessment() {
		return assessment;
	}

	public void setAssessment(String assessment) {
		this.assessment = assessment;
	}

	public String getCertifyingOrganisationId() {
		return certifyingOrganisationId;
	}

	public void setCertifyingOrganisationId(String certifyingOrganisationId) {
		this.certifyingOrganisationId = certifyingOrganisationId;
	}

}
