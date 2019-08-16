package com.smate.center.data.model.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "PDWH_PUB_KEYWORDS_CATEGORY")
public class PdwhPubKeywordsCategory implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 631257999922878980L;
  private Long id;
  private Long pubId;
  private String zhKeywords;
  private String enKeywords;
  private Long scmCategoryId;
  private Integer status;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_PDWH_PUB_KEYWORDS_CATEGORY", allocationSize = 1)
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

  @Column(name = "ZH_KEYWORDS")
  public String getZhKeywords() {
    return zhKeywords;
  }

  public void setZhKeywords(String zhKeywords) {
    this.zhKeywords = zhKeywords;
  }

  @Column(name = "EN_KEYWORDS")
  public String getEnKeywords() {
    return enKeywords;
  }

  public void setEnKeywords(String enKeywords) {
    this.enKeywords = enKeywords;
  }

  @Column(name = "SCM_CATEGORY_ID")
  public Long getScmCategoryId() {
    return scmCategoryId;
  }

  public void setScmCategoryId(Long scmCategoryId) {
    this.scmCategoryId = scmCategoryId;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
