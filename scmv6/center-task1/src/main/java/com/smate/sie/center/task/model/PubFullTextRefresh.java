package com.smate.sie.center.task.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_FULLTEXT_REFRESH")
public class PubFullTextRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4760389156626100170L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;

  @Column(name = "STATUS")
  private Integer status;

  public PubFullTextRefresh() {
    super();
  }

  public PubFullTextRefresh(Long pubId, Integer status) {
    super();
    this.pubId = pubId;
    this.status = status;
  }

  public Long getPubId() {
    return pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
