package com.smate.center.task.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 成果在crossref中的分类表
 * 
 * @author zll
 *
 */
@Entity
@Table(name = "PUB_CATEGORY_CROSSREF")
public class PubCategoryCrossref implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 8525914253212493723L;
  private Long id;
  private Long pubId;
  private String crossrefCategory;
  private Long scmCategoryId;

  public PubCategoryCrossref() {
    super();
  }

  public PubCategoryCrossref(Long pubId, String crossrefCategory) {
    super();
    this.pubId = pubId;
    this.crossrefCategory = crossrefCategory;
  }

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PUB_CATEGORY_CROSSREF", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "PUB_ID")
  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  @Column(name = "CROSSREF_CATEGORY")
  public String getCrossrefCategory() {
    return crossrefCategory;
  }

  public void setCrossrefCategory(String crossrefCategory) {
    this.crossrefCategory = crossrefCategory;
  }

  @Column(name = "SCM_CATEGORY_ID")
  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }

}
