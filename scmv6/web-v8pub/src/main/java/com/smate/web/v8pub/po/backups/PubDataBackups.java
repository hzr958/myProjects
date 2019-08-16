package com.smate.web.v8pub.po.backups;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_PUB_DATA_BACKUPS")
public class PubDataBackups implements Serializable {

  private static final long serialVersionUID = -4163177698368399765L;

  @Id
  @Column(name = "PUB_ID")
  private Long pubId; // 成果主键

  @Column(name = "DATA_TYPE")
  private Integer dataType; // 数据的类型，0未sns库，1为pdwh库

  @Column(name = "STATUS")
  private Integer status; // 成果备份状态，0尚未处理，1处理成功，99处理失败

  @Column(name = "GMT_MODIFIED")
  private Date gmtModified = new Date(); // 成果备份处理的时间

  @Column(name = "ERROR_MSG")
  private String errorMsg; // 错误信息


  public Long getPubId() {
    return pubId;
  }

  public void setPubId(Long pubId) {
    this.pubId = pubId;
  }

  public Integer getDataType() {
    return dataType;
  }

  public void setDataType(Integer dataType) {
    this.dataType = dataType;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getGmtModified() {
    return gmtModified;
  }

  public void setGmtModified(Date gmtModified) {
    this.gmtModified = gmtModified;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }


  @Override
  public String toString() {
    return "PubDataBackups{" + "pubId=" + pubId + ", dataType=" + dataType + ", status=" + status + ", gmtModified='"
        + gmtModified + '\'' + ", errorMsg='" + errorMsg + '\'' + '}';
  }
}
