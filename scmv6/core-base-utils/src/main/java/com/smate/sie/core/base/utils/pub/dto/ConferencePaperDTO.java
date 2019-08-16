package com.smate.sie.core.base.utils.pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 会议论文
 * 
 * @author ZSJ
 *
 * @date 2019年1月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConferencePaperDTO extends PubTypeInfoDTO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1685913347234272837L;

  private String name; // 会议名称

  private String organizer; // 会议组织者

  private String startDate; // 开始日期

  private String endDate; // 结束日期

  private String country; // 国家/地区

  private String city; // 城市

  public String getOrganizer() {
    return organizer;
  }

  public void setOrganizer(String organizer) {
    this.organizer = organizer;
  }

  public String getStartDate() {
    return startDate;
  }

  public void setStartDate(String startDate) {
    this.startDate = startDate;
  }

  public String getEndDate() {
    return endDate;
  }

  public void setEndDate(String endDate) {
    this.endDate = endDate;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountry() {
    return country;
  }

  public String getCity() {
    return city;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public void setCity(String city) {
    this.city = city;
  }

  @Override
  public String toString() {
    return "ConferencePaperBean [name=" + name + ", organizer=" + organizer + ", startDate=" + startDate + ", endDate="
        + endDate + ", country=" + country + ", city=" + city + "]";
  }

}
