package com.smate.center.task.v8pub.sns.po;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 基准库成果ID和SNS个人库成果ID的关系备份表
 * 
 */

@Entity
@Table(name = "V_PUB_PDWH_SNS_RELATION_BAK")
public class PubPdwhSnsRelationBak implements Serializable {

  private static final long serialVersionUID = -7913048931771947903L;
  @Id
  @Column(name = "PDWH_PUB_ID")
  private Long pdwhPubId; // 基准库成果id

  @Column(name = "STATUS")
  private Integer status;// 任务状态：0.待执行，1.正常，99.失败

  @Column(name = "ERR_MSG")
  private String errMsg;// 错误信息

  public PubPdwhSnsRelationBak() {
    super();
  }

  public Long getPdwhPubId() {
    return pdwhPubId;
  }

  public void setPdwhPubId(Long pdwhPubId) {
    this.pdwhPubId = pdwhPubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }
}
