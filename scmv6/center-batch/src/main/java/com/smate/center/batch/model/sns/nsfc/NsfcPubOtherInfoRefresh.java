package com.smate.center.batch.model.sns.nsfc;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 成果其他信息刷新实体(基金委成果在线).
 * 
 * @author xys
 *
 */
@Entity
@Table(name = "NSFC_PUB_OTHERINFO_REFRESH")
public class NsfcPubOtherInfoRefresh implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3243122556156762215L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId;// 成果id
  @Column(name = "STATUS")
  private Integer status = 0;// 0-待刷新，1-刷新成功，99-刷新失败
  @Column(name = "ERROR_INFO")
  private String errorInfo;// 错误信息

  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public String getErrorInfo() {
    return errorInfo;
  }

  public void setErrorInfo(String errorInfo) {
    this.errorInfo = errorInfo;
  }

}
