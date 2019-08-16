package com.smate.web.management.model.journal;

import java.io.Serializable;

/**
 * 给人员推荐基准文献：关键词匹配所用form.
 */

public class JnlRecLeftForm implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3598575052327517407L;

  private Long psnId;

  /**
   * 质量：1-5星，默认最低3星
   */
  private String degrees;

  /**
   * 相关类型：1最新推荐，2本人发表过，3好友发表过
   */
  private String relatedType;

  /**
   * 关键词
   */
  private String keyword;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getDegrees() {
    return degrees;
  }

  public void setDegrees(String degrees) {
    this.degrees = degrees;
  }

  public String getRelatedType() {
    return relatedType;
  }

  public void setRelatedType(String relatedType) {
    this.relatedType = relatedType;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

}
