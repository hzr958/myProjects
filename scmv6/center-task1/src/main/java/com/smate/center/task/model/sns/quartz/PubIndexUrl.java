package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果站外地址实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_PUB_INDEX_URL")
public class PubIndexUrl implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1462238315938735367L;
  /**
   * 
   */
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PUB_INDEX_URL")
  private String pubIndexUrl;
  @Column(name = "PUB_LONG_INDEX_URL") // 32位短地址
  private String pubLongIndexUrl;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public PubIndexUrl() {
    super();
  }

  public PubIndexUrl(Long pubId, String pubIndexUrl, String pubLongIndexUrl, Long psnId, Date updateDate) {
    super();
    this.pubId = pubId;
    this.pubIndexUrl = pubIndexUrl;
    this.pubLongIndexUrl = pubLongIndexUrl;
    this.psnId = psnId;
    this.updateDate = updateDate;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getPubIndexUrl() {
    return pubIndexUrl;
  }

  public void setPubIndexUrl(String pubIndexUrl) {
    this.pubIndexUrl = pubIndexUrl;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getPubLongIndexUrl() {
    return pubLongIndexUrl;
  }

  public void setPubLongIndexUrl(String pubLongIndexUrl) {
    this.pubLongIndexUrl = pubLongIndexUrl;
  }

  @Override
  public String toString() {
    return "PubIndexUrl [pubId=" + pubId + ", pubIndexUrl=" + pubIndexUrl + ", pubLongIndexUrl=" + pubLongIndexUrl
        + ", psnId=" + psnId + ", updateDate=" + updateDate + "]";
  }

}
