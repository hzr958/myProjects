package com.smate.center.batch.model.pdwh.pub;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author Administrator 高新产业关键词表
 */
@Entity
@Table(name = "V_KEYWORD_SUBSET_HNT_TMP")
public class PubKeywordsSubsetsHntTmp implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -7547251560439256228L;
  private String id;
  private Integer status;

  public PubKeywordsSubsetsHntTmp() {
    super();
  }

  @Id
  @Column(name = "CATEGORY")
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
