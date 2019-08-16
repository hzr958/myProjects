package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员关键词刷新纪录表.
 * 
 * @author zjh
 *
 */
@Entity
@Table(name = "psn_kw_ept_refresh")
public class PsnKwEptRefresh implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 6508108447668288369L;
  private Long psnId;
  // 0等待处理，9错误
  private Integer status;

  public PsnKwEptRefresh() {
    super();
  }

  public PsnKwEptRefresh(Long psnId, Integer status) {
    super();
    this.psnId = psnId;
    this.status = status;
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

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
