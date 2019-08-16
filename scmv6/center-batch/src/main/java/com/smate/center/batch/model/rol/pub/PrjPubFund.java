package com.smate.center.batch.model.rol.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 项目关联上的成果（成果里面的fundinfo编号关联）.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PRJ_PUBFUND")
public class PrjPubFund implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 4315376455509837267L;

  private Long id;
  // 项目ID
  private Long prjId;
  // 成果ID
  private Long pubId;
  // 单位ID
  private Long insId;
  // 1:关联上，9用户删除
  private Integer status;

  public PrjPubFund() {
    super();
  }

  public PrjPubFund(Long prjId, Long pubId, Long insId) {
    super();
    this.prjId = prjId;
    this.pubId = pubId;
    this.insId = insId;
    this.status = 1;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PRJ_PUBFUND", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PRJ_ID")
  public Long getPrjId() {
    return prjId;
  }

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

  public void setId(Long id) {
    this.id = id;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
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
