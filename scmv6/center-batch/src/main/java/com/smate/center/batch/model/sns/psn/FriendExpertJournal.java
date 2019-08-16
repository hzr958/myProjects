package com.smate.center.batch.model.sns.psn;

import java.io.Serializable;

/**
 * 好友-->同行专家
 * 
 * @author zk
 * 
 */
public class FriendExpertJournal implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3258798702683797955L;
  private Long psnId; // 人员id
  private String issn; // 期刊ISSN
  private String titleZh; // 期刊中文名
  private String titleEn;// 期刊英文名
  private String title; // 期刊名

  public Long getPsnId() {
    return psnId;
  }

  public String getIssn() {
    return issn;
  }

  public String getTitleZh() {
    return titleZh;
  }

  public String getTitleEn() {
    return titleEn;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIssn(String issn) {
    this.issn = issn;
  }

  public void setTitleZh(String titleZh) {
    this.titleZh = titleZh;
  }

  public void setTitleEn(String titleEn) {
    this.titleEn = titleEn;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

}
