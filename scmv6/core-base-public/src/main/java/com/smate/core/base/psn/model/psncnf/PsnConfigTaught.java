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
 * 个人配置：所教课程
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_TAUGHT")
public class PsnConfigTaught implements PsnCnfBase, Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 7908805699711884664L;

  // 配置主键
  private Long cnfId;

  // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUser = 7;
  // 数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyView = 7;

  // 创建日期
  private Date createDate = new Date();

  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigTaught() {}

  public PsnConfigTaught(Long cnfId) {
    this.cnfId = cnfId;
  }

  @JsonIgnore
  @Override
  @Id
  @Column(name = "CNF_ID")
  public Long getCnfId() {
    return cnfId;
  }

  @Column(name = "ANY_USER")
  public Integer getAnyUser() {
    return anyUser;
  }

  @JsonIgnore
  @Column(name = "ANY_VIEW")
  public Integer getAnyView() {
    return anyView;
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

  public void setAnyUser(Integer anyUser) {
    this.anyUser = anyUser;
  }

  public void setAnyView(Integer anyView) {
    this.anyView = anyView;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public void setUpdateDate(Date updateDate) {
    this.updateDate = updateDate;
  }
}
