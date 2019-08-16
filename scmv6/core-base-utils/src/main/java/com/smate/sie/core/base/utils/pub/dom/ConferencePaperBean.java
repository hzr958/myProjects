package com.smate.sie.core.base.utils.pub.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 会议论文
 * 
 * @author ZSJ
 *
 * @author 2019年1月31日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConferencePaperBean extends PubTypeInfoBean {

  private String name = new String(); // 会议名称

  private String organizer = new String(); // 会议组织者

  private String startDate = new String(); // 开始日期

  private String endDate = new String(); // 结束日期

  private String country = new String(); // 国家/地区

  private String city = new String(); // 城市

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
