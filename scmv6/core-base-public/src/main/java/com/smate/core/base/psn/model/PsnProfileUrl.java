package com.smate.core.base.psn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人主页URL实体.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_PROFILE_URL")
public class PsnProfileUrl implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -8202742376107745212L;
  private Long psnId;
  private String url;
  private Date updateDate = new Date();
  private String psnIndexUrl;

  public PsnProfileUrl(Long psnId, String url, Date updateDate, String psnIndexUrl) {
    super();
    this.psnId = psnId;
    this.url = url;
    this.updateDate = updateDate;
    this.psnIndexUrl = psnIndexUrl;
  }

  public PsnProfileUrl() {
    super();
  }

  public PsnProfileUrl(Long psnId) {
    super();
    this.psnId = psnId;
  }

  public PsnProfileUrl(Long psnId, String url) {
    super();
    this.psnId = psnId;
    this.url = url;
  }

  public PsnProfileUrl(String psnIndexUrl, Long psnId) {
    super();
    this.psnId = psnId;
    this.psnIndexUrl = psnIndexUrl;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "URL")
  public String getUrl() {
    return url;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "PSN_INDEX_URL")
  public String getPsnIndexUrl() {
    return psnIndexUrl;
  }

  public void setPsnIndexUrl(String psnIndexUrl) {
    this.psnIndexUrl = psnIndexUrl;
  }

}
