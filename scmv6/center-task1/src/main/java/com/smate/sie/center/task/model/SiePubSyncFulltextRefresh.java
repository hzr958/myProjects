package com.smate.sie.center.task.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 同步SNS全文表
 * 
 * @author yxy
 */
@Entity
@Table(name = "PUB_SYNC_FULLTEXT_REFRESH")
public class SiePubSyncFulltextRefresh {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_SYNC_FULLTEXT_REFRESH", allocationSize = 1)
  @Column(name = "ID")
  private Long syncId;
  @Column(name = "SNS_PUB_ID")
  private Long snsPubId = 0L;
  @Column(name = "FULLTEXT_ID")
  private Long fulltextId = 0L;
  @Column(name = "STATUS")
  private Integer status;
  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public Long getSyncId() {
    return syncId;
  }

  public Long getSnsPubId() {
    return snsPubId;
  }

  public Long getFulltextId() {
    return fulltextId;
  }

  public Integer getStatus() {
    return status;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setSyncId(Long syncId) {
    this.syncId = syncId;
  }

  public void setSnsPubId(Long snsPubId) {
    this.snsPubId = snsPubId;
  }

  public void setFulltextId(Long fulltextId) {
    this.fulltextId = fulltextId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
