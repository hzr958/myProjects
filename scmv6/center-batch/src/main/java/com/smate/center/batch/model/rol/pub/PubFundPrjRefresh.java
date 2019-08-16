package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果项目信息改变，刷新关联的项目.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUBFUND_PRJ_REFRESH")
public class PubFundPrjRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7346211377717491461L;

  // 成果ID
  private Long pubId;
  // 单位ID
  private Long insId;
  // 状态，0等待处理，9处理失败
  private Integer status;

  public PubFundPrjRefresh() {
    super();
  }

  public PubFundPrjRefresh(Long pubId, Long insId) {
    super();
    this.pubId = pubId;
    this.insId = insId;
    this.status = 0;
  }

  @Id
  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
