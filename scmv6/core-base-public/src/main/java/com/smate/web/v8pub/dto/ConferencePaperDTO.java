package com.smate.web.v8pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubConferencePaperTypeEnum;

/**
 * 会议信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConferencePaperDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = 2906479508962203790L;
  private PubConferencePaperTypeEnum paperType = PubConferencePaperTypeEnum.NULL; // 论文类别
                                                                                  // 1：特邀报告
                                                                                  // 2：国内学术会议

  private String name; // 会议名称

  private String organizer; // 会议组织者

  private String papers; // 论文集名

  private String startDate; // 开始日期

  private String endDate; // 结束日期

  private String pageNumber; // 起始页码

  public PubConferencePaperTypeEnum getPaperType() {
    return paperType;
  }

  public void setPaperType(PubConferencePaperTypeEnum paperType) {
    this.paperType = paperType;
  }

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

  public String getPapers() {
    return papers;
  }

  public void setPapers(String papers) {
    this.papers = papers;
  }

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }

}
