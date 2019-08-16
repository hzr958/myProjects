package com.smate.web.management.model.analysis.sns;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 所教课程拆分hash.
 * 
 * @author liqinghua
 * 
 */
@Entity
@Table(name = "PERSON_TAUGHT_HASH")
public class PersonTaughtHash implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = 9180370194610008297L;
  // 主键
  private Long id;
  // 人员ID
  private Long psnId;
  // 课程hash
  private Long tauhtHash;

  public PersonTaughtHash() {
    super();
  }

  public PersonTaughtHash(Long psnId, Long tauhtHash) {
    super();
    this.psnId = psnId;
    this.tauhtHash = tauhtHash;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_NAME", sequenceName = "SEQ_PERSON_TAUGHT_HASH", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NAME")
  public Long getId() {
    return id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  @Column(name = "TAUGHT_HASH")
  public Long getTauhtHash() {
    return tauhtHash;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public void setTauhtHash(Long tauhtHash) {
    this.tauhtHash = tauhtHash;
  }

}
