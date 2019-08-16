package com.smate.core.base.psn.model.psncnf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：联系方式
 * 
 * @author zhuangyanming
 */
@Entity
@Table(name = "PSN_CONFIG_CONTACT")
public class PsnConfigContact implements PsnCnfBase, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -6613226398936581616L;

  // 配置主键
  private Long cnfId;
  // Email：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserEmail = 6;
  // Email：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewEmail = 4;
  // Tel：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserTel = 6;
  // Tel：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewTel = 4;
  // Mobile：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserMobile = 4;
  // Mobile：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewMobile = 4;
  // QQ：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserQq = 6;
  // QQ：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewQq = 4;
  // Skype：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserSkype = 6;
  // Skype：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewSkype = 4;
  // Msn：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserMsn = 6;
  // Msn：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewMsn = 4;
  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigContact() {}

  public PsnConfigContact(Long cnfId) {
    this.cnfId = cnfId;
  }

  @JsonIgnore
  @Override
  @Id
  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "ANY_USER_EMAIL")
  public Integer getAnyUserEmail() {
    return anyUserEmail;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_EMAIL")
  public Integer getAnyViewEmail() {
    return anyViewEmail;
  }

  @Column(name = "ANY_USER_TEL")
  public Integer getAnyUserTel() {
    return anyUserTel;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_TEL")
  public Integer getAnyViewTel() {
    return anyViewTel;
  }

  @Column(name = "ANY_USER_MOBILE")
  public Integer getAnyUserMobile() {
    return anyUserMobile;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_MOBILE")
  public Integer getAnyViewMobile() {
    return anyViewMobile;
  }

  @Column(name = "ANY_USER_QQ")
  public Integer getAnyUserQq() {
    return anyUserQq;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_QQ")
  public Integer getAnyViewQq() {
    return anyViewQq;
  }

  @Column(name = "ANY_USER_SKYPE")
  public Integer getAnyUserSkype() {
    return anyUserSkype;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_SKYPE")
  public Integer getAnyViewSkype() {
    return anyViewSkype;
  }

  @Column(name = "ANY_USER_MSN")
  public Integer getAnyUserMsn() {
    return anyUserMsn;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW_MSN")
  public Integer getAnyViewMsn() {
    return anyViewMsn;
  }

  @JsonIgnore
  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @JsonIgnore
  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setAnyUserEmail(Integer anyUserEmail) {
    this.anyUserEmail = anyUserEmail;
  }

  public void setAnyViewEmail(Integer anyViewEmail) {
    this.anyViewEmail = anyViewEmail;
  }

  public void setAnyUserTel(Integer anyUserTel) {
    this.anyUserTel = anyUserTel;
  }

  public void setAnyViewTel(Integer anyViewTel) {
    this.anyViewTel = anyViewTel;
  }

  public void setAnyUserMobile(Integer anyUserMobile) {
    this.anyUserMobile = anyUserMobile;
  }

  public void setAnyViewMobile(Integer anyViewMobile) {
    this.anyViewMobile = anyViewMobile;
  }

  public void setAnyUserQq(Integer anyUserQq) {
    this.anyUserQq = anyUserQq;
  }

  public void setAnyViewQq(Integer anyViewQq) {
    this.anyViewQq = anyViewQq;
  }

  public void setAnyUserSkype(Integer anyUserSkype) {
    this.anyUserSkype = anyUserSkype;
  }

  public void setAnyViewSkype(Integer anyViewSkype) {
    this.anyViewSkype = anyViewSkype;
  }

  public void setAnyUserMsn(Integer anyUserMsn) {
    this.anyUserMsn = anyUserMsn;
  }

  public void setAnyViewMsn(Integer anyViewMsn) {
    this.anyViewMsn = anyViewMsn;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
