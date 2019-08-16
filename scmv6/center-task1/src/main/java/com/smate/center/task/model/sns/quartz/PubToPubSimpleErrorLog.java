package com.smate.center.task.model.sns.quartz;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PUB_TOPUBSIMPLE_ERROR")
public class PubToPubSimpleErrorLog implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7309539928306333531L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;
  @Column(name = "ERROR_MSG")
  private String errorMsg;

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }



}

