package com.smate.web.v8pub.dom;

import java.io.Serializable;

/**
 * 行业组件
 * 
 * @author YJ
 *
 *         2019年5月23日
 */
public class IndustryBean implements Serializable {

  private static final long serialVersionUID = -4962707691593509547L;

  private Long pubId; // 成果id
  private Long industryId; // 行业id

  public Long getIndustryId() {
    return industryId;
  }

  public void setIndustryId(Long industryId) {
    this.industryId = industryId;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }
}
