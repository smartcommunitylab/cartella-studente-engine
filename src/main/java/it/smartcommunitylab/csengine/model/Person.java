package it.smartcommunitylab.csengine.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Person {
	@Id
	private String id;
	private String fiscalCode;
	private Map<String, DataView> views = new HashMap<>();
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
