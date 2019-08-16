package com.smate.center.task.model.innocity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "JY_PATENTS_REQ_RCMD_RESULT")
public class InnoCityReqRcmdForPatResult implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6965364886813979254L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_JY_PATENTS_REQ_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long Id;

  @Column(name = "PATENT_ID")
  private Long patentId;

  @Column(name = "REQ_ID")
  private Long reqId;

  @Column(name = "RCMD_STATUS")
  private Integer status;

  public InnoCityReqRcmdForPatResult() {
    super();
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPatentId() {
    return patentId;
  }

  public void setPatentId(Long patentId) {
    this.patentId = patentId;
  }

  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }
}
