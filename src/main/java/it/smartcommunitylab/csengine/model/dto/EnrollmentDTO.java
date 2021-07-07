package it.smartcommunitylab.csengine.model.dto;

public class EnrollmentDTO extends ExperienceDTO {
  private String schoolYear;
  private String course;
  private String classroom;
  
  public String getSchoolYear() {
    return schoolYear;
  }
  public void setSchoolYear(String schoolYear) {
    this.schoolYear = schoolYear;
  }
  public String getCourse() {
    return course;
  }
  public void setCourse(String course) {
    this.course = course;
  }
  public String getClassroom() {
    return classroom;
  }
  public void setClassroom(String classroom) {
    this.classroom = classroom;
  }
  
}
