package com.smate.center.open.model.grp;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 群组站外地址实体类
 * 
 * @author zzx
 *
 */
@Entity
@Table(name = "V_GRP_INDEX_URL")
public class GrpIndexUrl implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  @Id
  @Column(name = "GRP_ID")
  private Long grpId;
  @Column(name = "GRP_INDEX_URL")
  private String grpIndexUrl;
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "UPDATE_DATE")
  private Date updateDate;

  public GrpIndexUrl() {
    super();
  }

  public GrpIndexUrl(Long grpId, String grpIndexUrl, Long psnId, Date updateDate) {
    super();
    this.grpId = grpId;
    this.grpIndexUrl = grpIndexUrl;
    this.psnId = psnId;
    this.updateDate = updateDate;
  }

  public Long getGrpId() {
    return grpId;
  }

  public void setGrpId(Long grpId) {
    this.grpId = grpId;
  }

  public String getGrpIndexUrl() {
    return grpIndexUrl;
  }

  public void setGrpIndexUrl(String grpIndexUrl) {
    this.grpIndexUrl = grpIndexUrl;
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
    return "GrpIndexUrl [grpId=" + grpId + ", grpIndexUrl=" + grpIndexUrl + ", psnId=" + psnId + ", updateDate="
        + updateDate + "]";
  }

}
