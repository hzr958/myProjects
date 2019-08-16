package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PRJ_FULLTEXT_REFRESH")
public class SiePrjFullTextRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4760389156626100170L;

  @Id
  @Column(name = "PRJ_ID")
  private Long prjId;

  @Column(name = "STATUS")
  private Integer status;

  public SiePrjFullTextRefresh() {
    super();
  }

  public SiePrjFullTextRefresh(Long prjId, Integer status) {
    super();
    this.prjId = prjId;
    this.status = status;
  }

  public Long getPrjId() {
    return prjId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setPrjId(Long prjId) {
    this.prjId = prjId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
