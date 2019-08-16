package com.smate.center.task.v8pub.pdwh.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "V_PUB_PDWH_REPEAT_RECORD")
public class PdwhPubRepeatRecordPO {

  @Id
  @SequenceGenerator(name = "V_SEQ_PUB_PDWH_REPEAT_RECORD", sequenceName = "V_SEQ_PUB_PDWH_REPEAT_RECORD",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_PUB_PDWH_REPEAT_RECORD")
  @Column(name = "ID")
  private Long id; // 逻辑id

  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库id

  @Column(name = "DUP_PUB_ID")
  private Long dupPubId; // 查重到的成果id

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Long getDupPubId() {
    return dupPubId;
  }

  public void setDupPubId(Long dupPubId) {
    this.dupPubId = dupPubId;
  }

}
