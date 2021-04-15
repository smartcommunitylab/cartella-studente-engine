package it.smartcommunitylab.csengine.model.dto;

import it.smartcommunitylab.csengine.model.Person;

public class PersonDTO {
	private String id;
	private String name;
	private String surname;
	private String fiscalCode;

	public PersonDTO() {}
	
	public PersonDTO(Person p) {
		this.id = p.getId();
		this.name = p.getName();
		this.surname = p.getSurname();
		this.fiscalCode = p.getFiscalCode();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getFiscalCode() {
		return fiscalCode;
	}

	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}
}
