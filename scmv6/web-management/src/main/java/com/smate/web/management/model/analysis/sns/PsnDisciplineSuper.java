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
 * 个人研究领域父二级信息MODEL.
 * 
 * @author chenxiangrong
 * 
 */
@Entity
@Table(name = "PSN_DISCIPLINE_SUPER")
public class PsnDisciplineSuper implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -7919984888477839410L;
  private Long id;
  private Long psnId;
  private Long discId;
  private Long superDiscId;

  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PSN_DISCIPLINE_SUPER", allocationSize = 1)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PSN_ID")
  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  @Column(name = "DISC_ID")
  public Long getDiscId() {
    return discId;
  }

  public void setDiscId(Long discId) {
    this.discId = discId;
  }

  @Column(name = "SUPER_DISCID")
  public Long getSuperDiscId() {
    return superDiscId;
  }

  public void setSuperDiscId(Long superDiscId) {
    this.superDiscId = superDiscId;
  }

}
