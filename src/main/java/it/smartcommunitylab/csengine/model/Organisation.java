package it.smartcommunitylab.csengine.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Organisation {
	@Id
	private String id;
	private String name;
	private String description;
	private String address;
	private String phone;
	private String email;
	private String pec;
	private String fiscalCode; 
	private GeoJsonPoint location;
	private Map<String, DataView> views = new HashMap<>();
	
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
	public Map<String, DataView> getViews() {
		return views;
	}
	public void setViews(Map<String, DataView> views) {
		this.views = views;
	}
	public GeoJsonPoint getLocation() {
		return location;
	}
	public void setLocation(GeoJsonPoint location) {
		this.location = location;
	}
	
	
}
