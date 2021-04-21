package it.smartcommunitylab.csengine.model.dto;

import it.smartcommunitylab.csengine.model.Address;

public class OrganisationDTO {
	private String id;
	private String name;
	private String description;
	private Address address;
	private String phone;
	private String email;
	private String pec;
	private String fiscalCode; 
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPec() {
		return pec;
	}
	public void setPec(String pec) {
		this.pec = pec;
	}
	public String getFiscalCode() {
		return fiscalCode;
	}
	public void setFiscalCode(String fiscalCode) {
		this.fiscalCode = fiscalCode;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
}
