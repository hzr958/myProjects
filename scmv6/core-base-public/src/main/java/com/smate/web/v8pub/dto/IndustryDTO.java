package com.smate.web.v8pub.dto;

import java.io.Serializable;

/**
 * 行业组件
 * 
 * @author YJ
 *
 *         2019年5月23日
 */
public class IndustryDTO implements Serializable {

  private static final long serialVersionUID = -4962707691593509547L;

  private Long pubId; // 成果id
  private String industryCode; // 行业id
  private String industryName; // 科技领域名
  private String industryName_En; // 科技领域英文名

  public IndustryDTO() {

  }

  public IndustryDTO(String industryCode, String industryName, String industryName_En) {
    super();
    this.industryCode = industryCode;
    this.industryName = industryName;
    this.industryName_En = industryName_En;
  }

  public String getIndustryCode() {
    return industryCode;
  }

  public void setIndustryCode(String industryCode) {
    this.industryCode = industryCode;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getIndustryName() {
    return industryName;
  }

  public void setIndustryName(String industryName) {
    this.industryName = industryName;
  }

  public String getIndustryName_En() {
    return industryName_En;
  }

  public void setIndustryName_En(String industryName_En) {
    this.industryName_En = industryName_En;
  }
}
