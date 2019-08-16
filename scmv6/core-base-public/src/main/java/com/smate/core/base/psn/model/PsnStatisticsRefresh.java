package com.smate.core.base.psn.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人统计数刷新记录表.
 * 
 * @author tsz
 *
 * @date 2018年9月19日
 */
@Entity
@Table(name = "V_PSN_STATISTICS_REFRESH")
public class PsnStatisticsRefresh {
  @Id
  @Column(name = "PSN_ID")
  private Long psnId;
  @Column(name = "STATUS")
  private Integer status; // 0待处理，1处理成功,2处理失败 一般不更新为状态1
  @Column(name = "UPDATE_DATE")
  private Date updateDate; // 最后处理时间,如果状态没变化，那么最后一次处理是成功的
  @Column(name = "MSG")
  private String msg; // 消息

  public PsnStatisticsRefresh() {
    super();
  }

  /**
   * 全部参数. 2018年9月19日
   */
  public PsnStatisticsRefresh(Long psnId, Integer status, Date updateDate, String msg) {
    super();
    this.psnId = psnId;
    this.status = status;
    this.updateDate = updateDate;
    this.msg = msg;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }
}
