package com.smate.center.task.model.sns.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * NSFC项目在scm中的分类结果表，用于以后对应的项目群组推荐
 * 
 * 
 */
@Entity
@Table(name = "NSFC_PRJ_SCM_CATEGORY")
public class NsfcPrjCategory implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = 5263950638403863658L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_NSFC_PROJECT", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;

  @Column(name = "NSFC_PRJ_NO")
  private String nsfcPrjNo;

  @Column(name = "SCM_CATEGORY_NO")
  private Long scmCategoryId;

  public NsfcPrjCategory() {
    super();
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public String getNsfcPrjNo() {
    return nsfcPrjNo;
  }

  public void setNsfcPrjNo(String nsfcPrjNo) {
    this.nsfcPrjNo = nsfcPrjNo;
  }

  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }



}
