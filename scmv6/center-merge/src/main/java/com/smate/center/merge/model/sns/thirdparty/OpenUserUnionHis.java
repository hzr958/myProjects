package com.smate.center.merge.model.sns.thirdparty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 第三方系统与SNS关联表 历史表.
 * 
 * 
 */
@Entity
@Table(name = "V_OPEN_USER_UNION_HIS")
public class OpenUserUnionHis implements Serializable {
  private static final long serialVersionUID = 7419926601538077302L;
  @Id
  @Column(name = "ID")
  private Long id;
  @Column(name = "PSNID")
  private Long psnId;
  @Column(name = "OPEN_ID")
  private Long openId;
  @Column(name = "TOKEN")
  private String token;
  @Column(name = "CREATE_DATE")
  private Date createDate;
  @Column(name = "CREATE_TYPE")
  private int createType; // 0 标示用户自己绑定(open系统验证页面) // 1标示 通过juid //2通过人员注册(人员同步)生成
  @Column(name = "DEL_DATE")
  private Date delDate; // 移动到历史表的时间
  @Column(name = "DEAL_DATE")
  private Date dealDate; // 处理时间
  @Column(name = "status")
  private Integer status; // 0 未处理 ,1已经处理

  public int getCreateType() {
    return createType;
  }

  public void setCreateType(int createType) {
    this.createType = createType;
  }

  public OpenUserUnionHis() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public Date getDelDate() {
    return delDate;
  }

  public void setDelDate(Date delDate) {
    this.delDate = delDate;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getDealDate() {
    return dealDate;
  }

  public void setDealDate(Date dealDate) {
    this.dealDate = dealDate;
  }
}
