package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PAT_FULLTEXT_REFRESH")
public class PatFullTextRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4760389156626100170L;

  @Id
  @Column(name = "PAT_ID")
  private Long patId;

  @Column(name = "STATUS")
  private Integer status;

  public PatFullTextRefresh() {
    super();
  }

  public PatFullTextRefresh(Long patId, Integer status) {
    super();
    this.patId = patId;
    this.status = status;
  }

  public Long getPatId() {
    return patId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setPatId(Long patId) {
    this.patId = patId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
