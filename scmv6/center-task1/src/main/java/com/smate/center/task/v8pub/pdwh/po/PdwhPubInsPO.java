package com.smate.center.task.v8pub.pdwh.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果单位关系
 * 
 * @author YJ
 *
 *         2019年3月29日
 */
@Entity
@Table(name = "V_PUB_PDWH_INS")
public class PdwhPubInsPO implements Serializable {

  private static final long serialVersionUID = -6272841708807379901L;

  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // pdwh库pubId，主键

  @Column(name = "INS_ID")
  private Long insId; // 单位id

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

}
