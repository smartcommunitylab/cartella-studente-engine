package it.smartcommunitylab.csengine.model;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class FileDocument {
	@Id
	private String id;
	private String personId;
	private String experienceId;
	private String fileKey;
	private String externalUri;
	private String filename;
	private String contentType;
	private LocalDate dataUpload;
	private Long size;
	@Transient
	private String localPath;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getFileKey() {
		return fileKey;
	}
	public void setFileKey(String fileKey) {
		this.fileKey = fileKey;
	}
	public String getExternalUri() {
		return externalUri;
	}
	public void setExternalUri(String externalUri) {
		this.externalUri = externalUri;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public LocalDate getDataUpload() {
		return dataUpload;
	}
	public void setDataUpload(LocalDate dataUpload) {
		this.dataUpload = dataUpload;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getExperienceId() {
		return experienceId;
	}
	public void setExperienceId(String experienceId) {
		this.experienceId = experienceId;
	}
	public String getPersonId() {
		return personId;
	}
	public void setPersonId(String personId) {
		this.personId = personId;
	}
	public String getLocalPath() {
		return localPath;
	}
	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	} 
	
}
