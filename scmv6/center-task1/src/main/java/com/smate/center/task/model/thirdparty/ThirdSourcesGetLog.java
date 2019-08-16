package com.smate.center.task.model.thirdparty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 第三方信息 调用记录.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_THIRD_SOURCES_GET_LOG")
public class ThirdSourcesGetLog {
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "V_SEQ_THIRD_SOURCES_GET_LOG", sequenceName = "V_SEQ_THIRD_SOURCES_GET_LOG",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "V_SEQ_THIRD_SOURCES_GET_LOG")
  private Long id;// 主键

  @Column(name = "TYPE")
  private Integer type;// 数据类型 （1：基金机会,2：单位项目,3：通知公告）

  @Column(name = "SOURCE_ID")
  private Long sourceId;// 来源id

  @Column(name = "REQUEST_PARAMS")
  private String requestParams;// 接口请求参数

  @Column(name = "RESULT_STATUS")
  private String resultStatus;// 接口返回状态:success/error

  @Column(name = "RESULT")
  private String result;// 接口返回数据

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  @Column(name = "STATUS")
  private Integer status;// 0 正常调用,1 调用失败,2.响应解析失败,3.响应数据格式不对,4.返回结果 result解析失败

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

  public String getRequestParams() {
    return requestParams;
  }

  public void setRequestParams(String requestParams) {
    this.requestParams = requestParams;
  }

  public String getResultStatus() {
    return resultStatus;
  }

  public void setResultStatus(String resultStatus) {
    this.resultStatus = resultStatus;
  }

  public String getResult() {
    return result;
  }

  public void setResult(String result) {
    this.result = result;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  @Override
  public String toString() {
    return "ThirdSourcesGetLog [id=" + id + ", type=" + type + ", sourceId=" + sourceId + ", requestParams="
        + requestParams + ", resultStatus=" + resultStatus + ", result=" + result + ", createDate=" + createDate
        + ", status=" + status + "]";
  }

}
