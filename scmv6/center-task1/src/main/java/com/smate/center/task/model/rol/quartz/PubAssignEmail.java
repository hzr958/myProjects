package com.smate.center.task.model.rol.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * ISI成果匹配-成果指派拆分EMAIL.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PUB_ASSIGN_EMAIL")
public class PubAssignEmail implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3002787000699131124L;

  private Long id;
  private Long pubId;
  private Long pubInsId;
  private String email;
  private Long insId;
  private Integer seqNo;

  public PubAssignEmail() {
    super();
  }

  public PubAssignEmail(Long pubId, Long pubInsId, String email, Long insId, Integer seqNo) {
    super();
    this.pubId = pubId;
    this.pubInsId = pubInsId;
    this.email = email;
    this.seqNo = seqNo;
    this.insId = insId;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_ASSIGN_EMAIL", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  @Column(name = "PUB_INS_ID")
  public Long getPubInsId() {
    return pubInsId;
  }

  @Column(name = "EMAIL")
  public String getEmail() {
    return email;
  }

  @Column(name = "SEQ_NO")
  public Integer getSeqNo() {
    return seqNo;
  }

  @Column(name = "INS_ID")
  public Long getInsId() {
    return insId;
  }

  public void setInsId(Long insId) {
    this.insId = insId;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setPubInsId(Long pubInsId) {
    this.pubInsId = pubInsId;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSeqNo(Integer seqNo) {
    this.seqNo = seqNo;
  }
}
