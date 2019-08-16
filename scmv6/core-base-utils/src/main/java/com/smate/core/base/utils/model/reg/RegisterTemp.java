package com.smate.core.base.utils.model.reg;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 注册回调处理表.
 * 
 * @author zzx
 * 
 */
@Entity
@Table(name = "V_REGISTER_TEMP")
public class RegisterTemp {
  @Id
  @Column(name = "TOKEN")
  private Long token;// 主建
  @Column(name = "EMAIL")
  private String email;// 邮件
  @Column(name = "PARAM")
  private String param;// 必要参数
  @Column(name = "STATUS")
  private Integer status;// 状态 0=新建；1=已同意等待注册；2=已处理；
  @Column(name = "OPERATOR_ID")
  private Long operatorId;// 操作人（生成该记录的人）
  @Column(name = "PSN_ID")
  private Long psnId;// 注册后生成的psnId
  @Column(name = "TEMP_TYPE")
  private Integer tempType;// 回调类型：1=群组邀请站外人员
  @Column(name = "UPDATE_DATE")
  private Date updateDate;// 更新时间
  @Column(name = "CREATE_DATE")
  private Date createDate;// 创建时间

  public Long getToken() {
    return token;
  }

  public void setToken(Long token) {
    this.token = token;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Long getOperatorId() {
    return operatorId;
  }

  public void setOperatorId(Long operatorId) {
    this.operatorId = operatorId;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getTempType() {
    return tempType;
  }

  public void setTempType(Integer tempType) {
    this.tempType = tempType;
  }

  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
