package com.smate.web.v8pub.po.pdwh;

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
@Table(name = "V_PDWH_PUB_INDEX_URL")
public class PdwhPubIndexUrl {

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PUB_INDEX_URL")
  private String pubIndexUrl;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public PdwhPubIndexUrl() {
    super();
  }

  public PdwhPubIndexUrl(Long pubId, String pubIndexUrl, Long psnId, Date updateDate) {
    super();
    this.pubId = pubId;
    this.pubIndexUrl = pubIndexUrl;
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

  @Override
  public String toString() {
    return "PubIndexUrl [pubId=" + pubId + ", pubIndexUrl=" + pubIndexUrl + ", psnId=" + psnId + ", updateDate="
        + updateDate + "]";
  }

}
