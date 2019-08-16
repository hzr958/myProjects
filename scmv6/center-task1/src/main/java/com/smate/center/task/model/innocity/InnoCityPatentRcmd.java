package com.smate.center.task.model.innocity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 个人基本信息.
 * 
 * @author lichangwen
 * 
 */
@Entity
@Table(name = "PATENT_RCMD")
public class InnoCityPatentRcmd implements Serializable {


  /**
   * 
   */
  private static final long serialVersionUID = -2803516138580992901L;
  private Long pdwhPubId;
  private Long reqId;

  public InnoCityPatentRcmd() {
    super();
  }

  @Id
  @Column(name = "PDWH_PUB_ID")
  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  @Column(name = "REQ_ID")
  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }

}
