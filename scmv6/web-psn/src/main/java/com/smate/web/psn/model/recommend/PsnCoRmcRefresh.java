package com.smate.web.psn.model.recommend;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员合作者推荐刷新表.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_CO_RMC_REFRESH")
public class PsnCoRmcRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6480232069318900896L;

  // 人员ID
  private Long psnId;
  // 刷新状态
  private Integer status;
  // 标记时间
  private Date mkAt;

  public PsnCoRmcRefresh() {
    super();
  }

  public PsnCoRmcRefresh(Long psnId) {
    super();
    this.psnId = psnId;
  }

  @Id
  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  @Column(name = "MK_AT")
  public Date getMkAt() {
    return mkAt;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setMkAt(Date mkAt) {
    this.mkAt = mkAt;
  }

}
