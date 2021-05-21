package it.smartcommunitylab.csengine.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Experience {
	@Id
	private String id;
	private String personId;
	private String entityType;
	private boolean personal = false; 
	private Map<String, DataView> views = new HashMap<>();
	
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
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public Map<String, DataView> getViews() {
		return views;
	}
	public void setViews(Map<String, DataView> views) {
		this.views = views;
	}
	public boolean isPersonal() {
		return personal;
	}
	public void setPersonal(boolean personal) {
		this.personal = personal;
	}



}
