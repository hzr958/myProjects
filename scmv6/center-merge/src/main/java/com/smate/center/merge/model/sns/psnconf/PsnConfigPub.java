package com.smate.center.merge.model.sns.psnconf;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 个人配置：成果,.
 * 
 * @author zhuangyanming
 * 
 */
@Entity
@Table(name = "PSN_CONFIG_PUB")
public class PsnConfigPub implements PsnCnfBase, Serializable {
  private static final long serialVersionUID = -3096252790341768415L;
  private PsnConfigPubPk id;
  // 用户查看权限(位运算任意组合使用)：7公开和4私密
  private Integer anyUser = 7;
  // 数据有无标识(位运算任意组合使用)：7公开和4私密；和any_user进行与运算
  private Integer anyView = 7;
  // 创建日期
  private Date createDate = new Date();
  // 更新日期
  private Date updateDate = new Date();

  public PsnConfigPub() {
    this.id = new PsnConfigPubPk();
  }

  public PsnConfigPub(Long cnfId) {
    this.id = new PsnConfigPubPk(cnfId);
  }

  public PsnConfigPub(PsnConfigPubPk id) {
    this.id = id;
  }

  @JsonIgnore
  @EmbeddedId
  public PsnConfigPubPk getId() {
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

  public void setId(PsnConfigPubPk id) {
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
