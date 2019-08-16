package com.smate.core.base.dyn.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 动态刷新表(处理成功后，清除,出错时保留并保存错误日志)
 * 
 * @author zk
 *
 */

@Entity
@Table(name = "v_dynamic_refresh")
public class DynamicRefresh implements Serializable {

  private static final long serialVersionUID = 2532808918320675778L;

  private Long id; // 主键
  private Integer fromType; // 动态来源类型(1:个人,2:群组,3:机构主页..)
  private Integer dynTmp; // 动态模版id
  private String errorMg; // 任务错误日志
  private Date createDate; // 创建时间
  private Integer status; // 状态，0:待处理，99:动态生成失败

  @Id
  @Column(name = "ID")
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Column(name = "FROM_TYPE")
  public Integer getFromType() {
    return fromType;
  }

  public void setFromType(Integer fromType) {
    this.fromType = fromType;
  }

  @Column(name = "DYN_TMP")
  public Integer getDynTmp() {
    return dynTmp;
  }

  public void setDynTmp(Integer dynTmp) {
    this.dynTmp = dynTmp;
  }

  @Column(name = "ERROR_MSG")
  public String getErrorMg() {
    return errorMg;
  }

  public void setErrorMg(String errorMg) {
    this.errorMg = errorMg;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
