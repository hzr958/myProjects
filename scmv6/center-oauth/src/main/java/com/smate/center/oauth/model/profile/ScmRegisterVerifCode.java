package com.smate.center.oauth.model.profile;

import java.io.Serializable;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author LJ
 *
 *         2017年6月23日
 */
@Entity
@Table(name = "SCM_VERIF_CODE")
public class ScmRegisterVerifCode implements Serializable {
  private static final long serialVersionUID = -6701039226422877692L;
  private String account;// 账号
  private String code;// 验证码
  private Date updateDate;// 更新时间
  private Date createdDate;// 创建时间
  private Integer count;// 验证次数，默认为0
  private Integer status;// 验证状态，0：未验证，1验证通过，2验证失败

  public ScmRegisterVerifCode() {
    super();
  }

  public ScmRegisterVerifCode(String account, String code, Date updateDate, Date createdDate, Integer count,
      Integer status) {
    super();
    this.account = account;
    this.code = code;
    this.updateDate = updateDate;
    this.createdDate = createdDate;
    this.count = count;
    this.status = status;
  }

  @Id
  @Column(name = "ACCOUNT")
  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  @Column(name = "CODE")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Column(name = "CREATED_DATE")
  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

  @Column(name = "COUNT")
  public Integer getCount() {
    if (count == null) {
      count = 0;
    }
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  @Column(name = "STATUS")
  public Integer getStatus() {
    if (status == null) {
      status = 0;
    }
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

}
