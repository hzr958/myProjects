package com.smate.center.task.model.sns.pub;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 统计人员的公开成果和公开项目后台任务的表
 */
@Entity
@Table(name = "PSN_STATISTICS_PUB_PRJ")
public class PsnStatisticsPubPrj {
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  /**
   * 公开成果统计数
   */
  @Column(name = "PUB_SUM")
  private Long pubSum;
  /**
   * 公开项目统计数
   */
  @Column(name = "PRJ_SUM")
  private Long prjSum;
  /**
   * 处理状态0未处理、1已处理、-1处理错误
   */
  @Column(name = "STATUS")
  private Integer status;

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getPubSum() {
    return pubSum;
  }

  public void setPubSum(Long pubSum) {
    this.pubSum = pubSum;
  }

  public Long getPrjSum() {
    return prjSum;
  }

  public void setPrjSum(Long prjSum) {
    this.prjSum = prjSum;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
