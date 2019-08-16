package com.smate.web.v8pub.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.smate.core.base.pub.enums.PubStandardTypeEnum;

/**
 * 标准信息
 * 
 * @author YJ
 *
 *         2019年5月20日
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class StandardInfoDTO extends PubTypeInfoDTO implements Serializable {

  private static final long serialVersionUID = -1486919445207712422L;

  private PubStandardTypeEnum type; // 标准类型
  private String standardNo; // 标准号
  private String obsoleteDate; // 作废日期
  private String implementDate; // 实施日期
  private String icsNo; // ICS分类
  private String domainNo; // 中标分类
  private String publishAgency; // 公布机构
  private String technicalCommittees; // 归口单位


  public PubStandardTypeEnum getType() {
    return type;
  }

  public void setType(PubStandardTypeEnum type) {
    this.type = type;
  }

  public String getStandardNo() {
    return standardNo;
  }

  public void setStandardNo(String standardNo) {
    this.standardNo = standardNo;
  }

  public String getPublishAgency() {
    return publishAgency;
  }

  public void setPublishAgency(String publishAgency) {
    this.publishAgency = publishAgency;
  }

  public String getTechnicalCommittees() {
    return technicalCommittees;
  }

  public void setTechnicalCommittees(String technicalCommittees) {
    this.technicalCommittees = technicalCommittees;
  }

  public String getObsoleteDate() {
    return obsoleteDate;
  }

  public void setObsoleteDate(String obsoleteDate) {
    this.obsoleteDate = obsoleteDate;
  }

  public String getImplementDate() {
    return implementDate;
  }

  public void setImplementDate(String implementDate) {
    this.implementDate = implementDate;
  }

  public String getIcsNo() {
    return icsNo;
  }

  public void setIcsNo(String icsNo) {
    this.icsNo = icsNo;
  }

  public String getDomainNo() {
    return domainNo;
  }

  public void setDomainNo(String domainNo) {
    this.domainNo = domainNo;
  }

  @Override
  public String toString() {
    return "StandardInfoBean{" + "type=" + type + ", standardNo='" + standardNo + '\'' + '\'' + ", publishAgency='"
        + publishAgency + '\'' + ", technicalCommittees='" + technicalCommittees + '\'' + '}';
  }
}
