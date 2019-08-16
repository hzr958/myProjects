package com.smate.web.v8pub.dom;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 期刊信息
 * 
 * @author houchuanjie
 * @date 2018/05/30 16:59
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class JournalInfoBean extends PubTypeInfoBean implements Serializable {

  private static final long serialVersionUID = -5534894802032500492L;

  private String name = new String();// 期刊名称

  private Long jid; // 期刊id

  private String publishStatus = "P"; // 发表状态(P已发表/A已接收)

  private String volumeNo = new String(); // 卷号

  private String issue = new String(); // 期号

  private String pageNumber = new String(); // 起始页码或者文章号

  private String ISSN = new String(); // ISSN号

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
   * 发表状态
   * 
   * @return
   */
  public String getPublishStatus() {
    return publishStatus;
  }

  public void setPublishStatus(String publishStatus) {
    this.publishStatus = publishStatus;
  }

  /**
   * 卷号
   * 
   * @return
   */
  public String getVolumeNo() {
    return volumeNo;
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

  public String getPageNumber() {
    return pageNumber;
  }

  public void setPageNumber(String pageNumber) {
    this.pageNumber = pageNumber;
  }

  @Override
  public String toString() {
    return "JournalInfoBean{" + "jid=" + jid + ", publishStatus='" + publishStatus + '\'' + '\'' + ", volume='"
        + volumeNo + '\'' + ", ISSN='" + ISSN + '\'' + ", issue='" + issue + '\'' + ", pageNumber=" + pageNumber + '}';
  }
}
