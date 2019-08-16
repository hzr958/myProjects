package com.smate.core.base.utils.model.wechat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


/**
 * 第三方系统与SNS关联表
 * 
 * 
 */
@Entity
@Table(name = "V_OPEN_USER_UNION")
public class OpenUserUnion implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 7419926601538077302L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_OPEN_USER_UNION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
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
  private int createType; // 0 标示用户自己绑定(open系统验证页面) // 1标示 通过juid //2通过人员注册(人员同步)生成 // 7=调open接口成

  public int getCreateType() {
    return createType;
  }

  public void setCreateType(int createType) {
    this.createType = createType;
  }

  public OpenUserUnion() {
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


}
