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
@Table(name = "REQUIREMENT_PATENT_RCMD_RESULT")
public class InnoCityPatRcmdForReqResult implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3166963233540637756L;

  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_REQUIREMENT_PATENT_RCMD", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @Column(name = "ID")
  private Long Id;

  @Column(name = "REQ_ID")
  private Long reqId;

  @Column(name = "PATENT_ID")
  private Long patentId;

  @Column(name = "RCMD_STATUS")
  private Integer status;

  public InnoCityPatRcmdForReqResult() {
    super();
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

  public Long getPatentId() {
    return patentId;
  }

  public void setPatentId(Long patentId) {
    this.patentId = patentId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
