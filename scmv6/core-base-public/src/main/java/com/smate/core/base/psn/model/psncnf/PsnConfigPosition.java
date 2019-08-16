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
 * 个人配置：职称
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_POSITION")
public class PsnConfigPosition implements PsnCnfBase, Serializable {

  private static final long serialVersionUID = 2741169457674528312L;
  /**
   * 
   */
  // 配置主键
  private Long cnfId;
  // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人 7公开
  private Integer anyUser = 7;
  // 数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyView = 7;
  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigPosition() {

  }

  public PsnConfigPosition(Long cnfId) {
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

  @Column(name = "CREATE_DATE")
  @JsonIgnore
  public Date getCreateDate() {
    return createDate;
  }

  @Column(name = "UPDATE_DATE")
  @JsonIgnore
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((anyUser == null) ? 0 : anyUser.hashCode());
    result = prime * result + ((anyView == null) ? 0 : anyView.hashCode());
    result = prime * result + ((cnfId == null) ? 0 : cnfId.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof PsnConfigPosition)) {
      return false;
    }
    PsnConfigPosition other = (PsnConfigPosition) obj;
    if (anyUser == null) {
      if (other.anyUser != null) {
        return false;
      }
    } else if (!anyUser.equals(other.anyUser)) {
      return false;
    }
    if (anyView == null) {
      if (other.anyView != null) {
        return false;
      }
    } else if (!anyView.equals(other.anyView)) {
      return false;
    }
    if (cnfId == null) {
      if (other.cnfId != null) {
        return false;
      }
    } else if (!cnfId.equals(other.cnfId)) {
      return false;
    }
    return true;
  }

}
