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
}
