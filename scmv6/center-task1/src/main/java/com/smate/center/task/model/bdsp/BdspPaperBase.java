package com.smate.center.task.model.bdsp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 论文
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "BDSP_PAPER_BASE")
public class BdspPaperBase implements Serializable {
  private static final long serialVersionUID = 1L;
  /**
   * 成果id
   */
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  /**
   * 年份
   */
  @Column(name = "PUBLISH_YEAR")
  private Integer publishYear;
  /**
   * jid
   */
  @Column(name = "JID")
  private Long jid;
  /**
   * 类型
   */
  @Column(name = "PUB_TYPE")
  private Integer pubType;
  /**
   * 项目资助号
   */
  @Column(name = "SUPPORT_ID")
  private String supportId;
  @Column(name = "FUNDINFO")
  private String fundinfo;
  @Column(name = "CREATE_DATE")
  private Date createDate;


  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public String getFundinfo() {
    return fundinfo;
  }

  public void setFundinfo(String fundinfo) {
    this.fundinfo = fundinfo;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getPublishYear() {
    return publishYear;
  }

  public void setPublishYear(Integer publishYear) {
    this.publishYear = publishYear;
  }

  public Long getJid() {
    return jid;
  }

  public void setJid(Long jid) {
    this.jid = jid;
  }

  public Integer getPubType() {
    return pubType;
  }

  public void setPubType(Integer pubType) {
    this.pubType = pubType;
  }

  public String getSupportId() {
    return supportId;
  }

  public void setSupportId(String supportId) {
    this.supportId = supportId;
  }


}
