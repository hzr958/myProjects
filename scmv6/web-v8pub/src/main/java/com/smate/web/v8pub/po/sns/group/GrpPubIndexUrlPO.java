package com.smate.web.v8pub.po.sns.group;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 群组成果站外地址实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GRP_PUB_INDEX_URL")
public class GrpPubIndexUrlPO implements Serializable {
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_V_GRP_PUB_INDEX_URL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;
  @Column(name = "GRP_ID")
  private Long grpId;
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "PUB_INDEX_URL")
  private String pubIndexUrl;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public GrpPubIndexUrlPO() {
    super();
  }

  public GrpPubIndexUrlPO(Long id, Long grpId, Long pubId, String pubIndexUrl, Long psnId, Date updateDate) {
    super();
    Id = id;
    this.grpId = grpId;
    this.pubId = pubId;
    this.pubIndexUrl = pubIndexUrl;
    this.psnId = psnId;
    this.updateDate = updateDate;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
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

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  @Override
  public String toString() {
    return "GrpPubIndexUrl [Id=" + Id + ", grpId=" + grpId + ", pubId=" + pubId + ", pubIndexUrl=" + pubIndexUrl
        + ", psnId=" + psnId + ", updateDate=" + updateDate + "]";
  }

}
