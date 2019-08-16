package com.smate.sie.core.base.utils.pub.dom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 期刊论文
 * 
 * @author sjzhou
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalInfoBean extends PubTypeInfoBean {

  private String name = new String();// 期刊名称

  private String ISSN = new String(); // ISSN号

  private Long jid; // 期刊id

  private String publishStatusCode = new String(); // 发表状态(P已发表/A已接收)

  private String publishStatusName = new String(); // 发表状态(P已发表/A已接收)

  private String volumeNo = new String(); // 卷号

  private String issue = new String(); // 期号

  private String startPage = new String(); // 起始页码

  private String endPage = new String();

  private String articleNo = new String();

  /**
   * 期刊id
   * 
   * @return
   */
  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  /**
   * 卷号
   * 
   * @return
   */
  public String getVolumeNo() {
    return volumeNo;
  }

  public String getPublishStatusCode() {
    return publishStatusCode;
  }

  /**
   * 发表状态
   * 
   * @return
   */
  public void setPublishStatusCode(String publishStatusCode) {
    this.publishStatusCode = publishStatusCode;
  }

  public void setVolumeNo(String volumeNo) {
    this.volumeNo = volumeNo;
  }

  /**
   * 期号
   * 
   * @return
   */
  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getISSN() {
    return ISSN;
  }

  public void setISSN(String iSSN) {
    ISSN = iSSN;
  }

  public String getPublishStatusName() {
    return publishStatusName;
  }

  public String getStartPage() {
    return startPage;
  }

  public String getEndPage() {
    return endPage;
  }

  public String getArticleNo() {
    return articleNo;
  }

  public void setPublishStatusName(String publishStatusName) {
    this.publishStatusName = publishStatusName;
  }

  public void setStartPage(String startPage) {
    this.startPage = startPage;
  }

  public void setEndPage(String endPage) {
    this.endPage = endPage;
  }

  public void setArticleNo(String articleNo) {
    this.articleNo = articleNo;
  }

  @Override
  public String toString() {
    return "JournalInfoBean [name=" + name + ", ISSN=" + ISSN + ", jid=" + jid + ", publishStatusCode="
        + publishStatusCode + ", publishStatusName=" + publishStatusName + ", volumeNo=" + volumeNo + ", issue=" + issue
        + ", startPage=" + startPage + ", endPage=" + endPage + ", articleNo=" + articleNo + "]";
  }

}
