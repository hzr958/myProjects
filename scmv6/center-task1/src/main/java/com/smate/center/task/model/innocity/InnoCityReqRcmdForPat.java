package com.smate.center.task.model.innocity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JY_PATENTS_REQ_RCMD")
public class InnoCityReqRcmdForPat implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 936444001317644970L;
  @Id
  @Column(name = "PATENT_ID")
  private Long patentId;

  @Column(name = "RCMD_TIME")
  private Date rcmdTime;

  @Column(name = "EXTRACT_KEYWORDS")
  private String extractKws;

  @Column(name = "STATUS")
  private Integer status;

  public InnoCityReqRcmdForPat() {
    super();
  }

  public Long getPatentId() {
    return patentId;
  }

  public void setPatentId(Long patentId) {
    this.patentId = patentId;
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
