package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*
 * 
 * 人员分类结果表
 * 
 * 
 */
@Entity
@Table(name = "PSN_DISC_SCM")
public class PsnDiscScm implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 7839891030483184291L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_CATEGROY_MAP_BASE_CNKI", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;

  @Column(name = "PSN_ID")
  private Long psnId;

  @Column(name = "SCM_DISC")
  private Long scmDiscNo;

  public PsnDiscScm() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getScmDiscNo() {
    return scmDiscNo;
  }

  public void setScmDiscNo(Long scmDiscNo) {
    this.scmDiscNo = scmDiscNo;
  }


}
