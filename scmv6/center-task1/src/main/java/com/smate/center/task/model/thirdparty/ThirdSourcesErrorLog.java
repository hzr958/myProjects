package com.smate.center.task.model.thirdparty;

import javax.persistence.*;
import java.util.Date;

/**
 * 第三方信息 获取数据错误日志记录.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_THIRD_SOURCES_ERROR_LOG")
public class ThirdSourcesErrorLog {
  @Id
  @Column(name = "ID")
  @SequenceGenerator (name = "V_SEQ_THIRD_SOURCES_ERROR_LOG", sequenceName = "V_SEQ_THIRD_SOURCES_ERROR_LOG",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_THIRD_SOURCES_ERROR_LOG")
  private Long id;// 主键

  @Column(name = "TYPE")
  private Integer type;// 数据类型 （1：基金机会,2：单位项目,3：通知公告）

  @Column(name = "SOURCE_ID")
  private Long sourceId;// 来源id

  @Column(name = "ERROR_DATA")
  private String errorData;// 错误数据

  @Column(name = "ERROR_MSG")
  private String errorMsg;// 错误描述

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public Long getSourceId() {
    return sourceId;
  }

  public void setSourceId(Long sourceId) {
    this.sourceId = sourceId;
  }

  public String getErrorData() {
    return errorData;
  }

  public void setErrorData(String errorData) {
    this.errorData = errorData;
  }

  public String getErrorMsg() {
    return errorMsg;
  }

  public void setErrorMsg(String errorMsg) {
    this.errorMsg = errorMsg;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Override
  public String toString() {
    return "ThirdSourcesErrorLog [id=" + id + ", type=" + type + ", sourceId=" + sourceId + ", errorData=" + errorData
        + ", errorMsg=" + errorMsg + ", createDate=" + createDate + "]";
  }



}
