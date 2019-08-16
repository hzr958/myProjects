package com.smate.center.task.model.thirdparty;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 第三方信息来源 类型记录.
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "V_THIRD_SOURCES_TYPE")
public class ThirdSourcesType {
  @Id
  @Column(name = "ID")
  private Long id;// 主键

  @Column(name = "TYPE")
  private Integer type;// 数据类型 （1：基金机会,2：单位项目,3：通知公告）

  @Column(name = "SOURCE_ID")
  private Long sourceId;// 来源id

  @Column(name = "STATUS")
  private Integer status;// 状态：0可用，1不可用

  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  @Column(name = "LAST_GET_DATE")
  private Date lastGetDate;// 最后获取信息时间

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

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getLastGetDate() {
    return lastGetDate;
  }

  public void setLastGetDate(Date lastGetDate) {
    this.lastGetDate = lastGetDate;
  }

  @Override
  public String toString() {
    return "ThirdSourcesType [id=" + id + ", type=" + type + ", sourceId=" + sourceId + ", status=" + status
        + ", createDate=" + createDate + ", lastGetDate=" + lastGetDate + "]";
  }


}
