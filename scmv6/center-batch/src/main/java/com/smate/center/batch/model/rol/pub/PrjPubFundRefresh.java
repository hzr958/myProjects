package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 项目关联上的成果（成果里面的fundinfo编号关联）刷新关联成果.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_PUBFUND_REFRESH")
public class PrjPubFundRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5714413493949108755L;

  // 项目ID
  private Long prjId;
  // 单位ID
  private Long insId;
  // 0等待刷新，9刷新错误
  private Integer status;

  public PrjPubFundRefresh() {
    super();
  }

  public PrjPubFundRefresh(Long prjId, Long insId) {
    super();
    this.prjId = prjId;
    this.insId = insId;
    this.status = 0;
  }

  @Id
  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
