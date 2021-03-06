package com.smate.center.batch.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 推荐系统同步文献信息标记.
 * 
 * @author lqh
 * 
 */
@Entity
@Table(name = "rcmd_sync_refinfo")
public class RcmdSyncRefInfo implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -6635143324667998567L;
  // 文献ID
  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  // 人员ID
  @Column(name = "PSN_ID")
  private Long psnId;
  // 是否删除
  @Column(name = "IS_DEL")
  private Integer isDel = 0;
  // 状态：0待处理，9错误
  @Column(name = "STATUS")
  private Integer status = 0;

  public RcmdSyncRefInfo() {
    super();
  }

  public RcmdSyncRefInfo(Long pubId, Long psnId, Integer isDel) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
    this.isDel = isDel;
    this.status = 0;
  }

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getIsDel() {
    return isDel;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
