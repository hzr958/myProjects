package com.smate.core.base.psn.model.psncnf;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smate.core.base.psn.service.psncnf.PsnCnfBase;

/**
 * 个人配置：项目
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_PRJ")
public class PsnConfigPrj implements PsnCnfBase, Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5446176028376722901L;

  private PsnConfigPrjPk id;

  // 用户查看权限(位运算任意组合使用)：1陌生人、2好友和4本人
  private Integer anyUser = 7;
  // 数据有无标识(位运算任意组合使用)：1陌生人、2好友和4本人；和any_user进行与运算
  private Integer anyView = 7;

  // 创建日期
  private Date createDate = new Date();

  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigPrj() {
    this.id = new PsnConfigPrjPk();
  }

  public PsnConfigPrj(Long cnfId) {
    this.id = new PsnConfigPrjPk(cnfId);
  }

  public PsnConfigPrj(PsnConfigPrjPk id) {
    this.id = id;
  }

  @JsonIgnore
  @EmbeddedId
  public PsnConfigPrjPk getId() {
    return id;
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

  public void setId(PsnConfigPrjPk id) {
    this.id = id;
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

  @JsonIgnore
  @Override
  @Transient
  public Long getCnfId() {
    return id.getCnfId();
  }

}
