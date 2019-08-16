package com.smate.center.task.model.innocity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "REQUIREMENT_PATENT_RCMD")
public class InnoCityPatRcmdForReq implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 936444001317644970L;
  @Id
  @Column(name = "REQ_ID")
  private Long reqId;

  @Column(name = "RCMD_TIME")
  private Date rcmdTime;

  @Column(name = "EXTRACT_KEYWORDS")
  private String extractKws;

  @Column(name = "STATUS")
  private Integer status;

  public InnoCityPatRcmdForReq() {
    super();
  }

  public Long getReqId() {
    return reqId;
  }

  public void setReqId(Long reqId) {
    this.reqId = reqId;
  }


  public Date getRcmdTime() {
    return rcmdTime;
  }

  public void setRcmdTime(Date rcmdTime) {
    this.rcmdTime = rcmdTime;
  }

  public String getExtractKws() {
    return extractKws;
  }

  public void setExtractKws(String extractKws) {
    this.extractKws = extractKws;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
