package com.smate.center.task.model.pdwh.quartz;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果地址作者匹配任务状态记录表
 * 
 * @author LIJUN
 * @date 2018年3月27日
 */
@Entity
@Table(name = "PDWH_MATCH_TASK_RECORD")
public class PdwhMatchTaskRecord implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 3087673841281166928L;
  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId;
  @Column(name = "MATCH_STATUS")
  private Integer matchStatus;// 1 都匹配上 2地址匹配出错 ，3作者匹配出错
  @Column(name = "UPDATE_TIME")
  private Date updateTime;
  @Column(name = "ERR_MSG")
  private String errMsg;

  public PdwhMatchTaskRecord() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Integer getMatchStatus() {
    return matchStatus;
  }

  public void setMatchStatus(Integer matchStatus) {
    this.matchStatus = matchStatus;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }

}
