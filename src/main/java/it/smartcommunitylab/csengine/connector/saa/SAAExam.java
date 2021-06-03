package it.smartcommunitylab.csengine.connector.saa;

import java.util.ArrayList;
import java.util.List;

public class SAAExam {
	private String extId;
	private String origin;
	private String dateFrom;
	private String dateTo;
	private String qualification;
	private String schoolYear;
	private String type;
	private String instituteRef;
	private boolean honour;
	private String grade;
	private boolean externalCandidate;
	private boolean positiveResult;	
	private List<SAACompetence> competences = new ArrayList<>();
	
	public String getExtId() {
		return extId;
	}
	public void setExtId(String extId) {
		this.extId = extId;
	}
	public String getOrigin() {
		return origin;
	}
	public void setOrigin(String origin) {
		this.origin = origin;
	}
	public String getDateFrom() {
		return dateFrom;
	}
	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}
	public String getDateTo() {
		return dateTo;
	}
	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}
	public String getQualification() {
		return qualification;
	}
	public void setQualification(String qualification) {
		this.qualification = qualification;
	}
	public String getSchoolYear() {
		return schoolYear;
	}
	public void setSchoolYear(String schoolYear) {
		this.schoolYear = schoolYear;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getInstituteRef() {
		return instituteRef;
	}
	public void setInstituteRef(String instituteRef) {
		this.instituteRef = instituteRef;
	}
	public List<SAACompetence> getCompetences() {
		return competences;
	}
	public void setCompetences(List<SAACompetence> competences) {
		this.competences = competences;
	}
	public boolean isHonour() {
		return honour;
	}
	public void setHonour(boolean honour) {
		this.honour = honour;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public boolean isExternalCandidate() {
		return externalCandidate;
	}
	public void setExternalCandidate(boolean externalCandidate) {
		this.externalCandidate = externalCandidate;
	}
	public boolean isPositiveResult() {
		return positiveResult;
	}
	public void setPositiveResult(boolean positiveResult) {
		this.positiveResult = positiveResult;
	}
}
