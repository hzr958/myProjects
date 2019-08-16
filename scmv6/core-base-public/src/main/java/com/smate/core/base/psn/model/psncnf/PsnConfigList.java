package com.smate.core.base.psn.model.psncnf;



import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：列表结果汇总(如成果、工作经历等)
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_LIST")
public class PsnConfigList implements PsnCnfBase, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -3159820930164809581L;

  // 配置主键
  private Long cnfId;

  // 工作经历：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserWorks = 4;
  // 工作经历：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewWorks = 4;
  // 教育经历：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserEdus = 4;
  // 教育经历：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewEdus = 4;
  // 成果：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserPubs = 4;
  // 成果：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewPubs = 4;
  // 项目：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserPrjs = 4;
  // 项目：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewPrjs = 4;
  // 联系方式：用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUserContacts = 4;
  // 联系方式：数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyViewContacts = 4;
  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigList() {

  }

  public PsnConfigList(Long cnfId) {
    this.cnfId = cnfId;
  }

  @Override
  @Id
  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "ANY_USER_WORKS")
  public Integer getAnyUserWorks() {
    return anyUserWorks;
  }

  @Column(name = "ANY_VIEW_WORKS")
  public Integer getAnyViewWorks() {
    return anyViewWorks;
  }

  @Column(name = "ANY_USER_EDUS")
  public Integer getAnyUserEdus() {
    return anyUserEdus;
  }

  @Column(name = "ANY_VIEW_EDUS")
  public Integer getAnyViewEdus() {
    return anyViewEdus;
  }

  @Column(name = "ANY_USER_PUBS")
  public Integer getAnyUserPubs() {
    return anyUserPubs;
  }

  @Column(name = "ANY_VIEW_PUBS")
  public Integer getAnyViewPubs() {
    return anyViewPubs;
  }

  @Column(name = "ANY_USER_PRJS")
  public Integer getAnyUserPrjs() {
    return anyUserPrjs;
  }

  @Column(name = "ANY_VIEW_PRJS")
  public Integer getAnyViewPrjs() {
    return anyViewPrjs;
  }

  @Column(name = "ANY_USER_CONTACTS")
  public Integer getAnyUserContacts() {
    return anyUserContacts;
  }

  @Column(name = "ANY_VIEW_CONTACTS")
  public Integer getAnyViewContacts() {
    return anyViewContacts;
  }

  @Column(name = "CREATE_DATE")
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "UPDATE_DATE")
  public Date getUpdateDate() {
    return updateDate;
  }

  public void setCnfId(Long cnfId) {
    this.cnfId = cnfId;
  }

  public void setAnyUserWorks(Integer anyUserWorks) {
    this.anyUserWorks = anyUserWorks;
  }

  public void setAnyViewWorks(Integer anyViewWorks) {
    this.anyViewWorks = anyViewWorks;
  }

  public void setAnyUserEdus(Integer anyUserEdus) {
    this.anyUserEdus = anyUserEdus;
  }

  public void setAnyViewEdus(Integer anyViewEdus) {
    this.anyViewEdus = anyViewEdus;
  }

  public void setAnyUserPubs(Integer anyUserPubs) {
    this.anyUserPubs = anyUserPubs;
  }

  public void setAnyViewPubs(Integer anyViewPubs) {
    this.anyViewPubs = anyViewPubs;
  }

  public void setAnyUserPrjs(Integer anyUserPrjs) {
    this.anyUserPrjs = anyUserPrjs;
  }

  public void setAnyViewPrjs(Integer anyViewPrjs) {
    this.anyViewPrjs = anyViewPrjs;
  }

  public void setAnyUserContacts(Integer anyUserContacts) {
    this.anyUserContacts = anyUserContacts;
  }

  public void setAnyViewContacts(Integer anyViewContacts) {
    this.anyViewContacts = anyViewContacts;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }

}
