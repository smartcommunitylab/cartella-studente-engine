package it.smartcommunitylab.csengine.model.dto;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import it.smartcommunitylab.csengine.model.Experience;

public class ExperienceDTO {
	private String id;
	private String personId;
	private String organisationId;
	private String title;
	private String description;
	private String entityType;
		
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dateFrom;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate dateTo;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validityFrom;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	private LocalDate validityTo;
	
	private List<String> competences;

	public ExperienceDTO() {}
	
	public ExperienceDTO(Experience e) {
		this.id = e.getId();
		this.personId = e.getPersonId();
		this.organisationId = e.getOrganisationId();
		this.title = e.getTitle();
		this.description = e.getDescription();
		this.entityType = e.getEntityType();
		this.dateFrom = e.getDateFrom();
		this.dateTo = e.getDateTo();
		this.validityFrom = e.getValidityFrom();
		this.validityTo = e.getValidityTo();
		if(e.getCompetences() != null) {
			this.competences = e.getCompetences();
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDate getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(LocalDate dateFrom) {
		this.dateFrom = dateFrom;
	}

	public LocalDate getDateTo() {
		return dateTo;
	}

	public void setDateTo(LocalDate dateTo) {
		this.dateTo = dateTo;
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

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getPersonId() {
		return personId;
	}

	public void setPersonId(String personId) {
		this.personId = personId;
	}

	public String getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(String organisationId) {
		this.organisationId = organisationId;
	}

	public List<String> getCompetences() {
		return competences;
	}

	public void setCompetences(List<String> competences) {
		this.competences = competences;
	}

}
