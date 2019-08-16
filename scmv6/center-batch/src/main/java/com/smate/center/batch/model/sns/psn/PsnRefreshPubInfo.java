package com.smate.center.batch.model.sns.psn;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 人员成果信息冗余刷新.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PSN_REFRESH_PUB_INFO")
public class PsnRefreshPubInfo implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -1797419892695083575L;

  // 成果ID
  private Long pubId;
  // 人员ID
  private Long psnId;
  // 是否删除
  private Integer isDel = 0;
  // 状态：0待处理，9错误
  private Integer status = 0;

  public PsnRefreshPubInfo() {
    super();
  }

  public PsnRefreshPubInfo(Long pubId, Long psnId) {
    super();
    this.pubId = pubId;
    this.psnId = psnId;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "IS_DEL")
  public Integer getIsDel() {
    return isDel;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setIsDel(Integer isDel) {
    this.isDel = isDel;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
