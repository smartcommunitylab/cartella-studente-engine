package it.smartcommunitylab.csengine.model.dto;

import it.smartcommunitylab.csengine.model.Address;

public class StageDTO extends ExperienceDTO {
  private String type;
  private String duration;
  private Address address;
  private String contact;
  
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public String getDuration() {
    return duration;
  }
  public void setDuration(String duration) {
    this.duration = duration;
  }
  public Address getAddress() {
    return address;
  }
  public void setAddress(Address address) {
    this.address = address;
  }
  public String getContact() {
    return contact;
  }
  public void setContact(String contact) {
    this.contact = contact;
  }
  
}
