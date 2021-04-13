package it.smartcommunitylab.csengine.model.dto;

import it.smartcommunitylab.csengine.model.Organisation;

public class OrganisationDTO {
	private String id;
	private String name;
	private String description;
	private String address;
	private String phone;
	private String email;
	private String pec;
	private String fiscalCode; 
	
	public OrganisationDTO() {}
	
	public OrganisationDTO(Organisation o) {
		this.id = o.getId();
		this.name = o.getName();
		this.description = o.getDescription();
		this.address = o.getAddress();
		this.phone = o.getPhone();
		this.email = o.getEmail();
		this.pec = o.getPec();
		this.fiscalCode = o.getFiscalCode();
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	
}
