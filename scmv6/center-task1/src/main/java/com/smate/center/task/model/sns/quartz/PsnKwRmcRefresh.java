package com.smate.center.task.model.sns.quartz;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人特征关键词推荐刷新记录.
 * 
 * @author zk
 * 
 */
@Entity
@Table(name = "PSN_KW_RMC_REFRESH")
public class PsnKwRmcRefresh {
  private Long psnId;
  // 状态：0待处理，9处理失败
  private Integer status;
  // 最后标记时间
  private Date mkAt;

  public PsnKwRmcRefresh() {
    super();
  }

  public PsnKwRmcRefresh(Long psnId, Integer status) {
    super();
    this.psnId = psnId;
    this.status = status;
    this.mkAt = new Date();
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

  public void setMkAt(Date mkAt) {
    this.mkAt = mkAt;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
