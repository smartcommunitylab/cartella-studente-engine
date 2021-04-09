package it.smartcommunitylab.csengine.model.dto;

import it.smartcommunitylab.csengine.model.Experience;

public class ExamDTO extends ExperienceDTO {
	private String type;
	private String qualification;
	private Boolean honour;
	private String grade;
	private Boolean result;
	private Boolean externalCandidate;
	
	public ExamDTO() {}
	
	public ExamDTO(Experience e) {
		super(e);
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public Boolean getHonour() {
		return honour;
	}
	public void setHonour(Boolean honour) {
		this.honour = honour;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
	public Boolean getExternalCandidate() {
		return externalCandidate;
	}
	public void setExternalCandidate(Boolean externalCandidate) {
		this.externalCandidate = externalCandidate;
	}
}
