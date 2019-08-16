package com.smate.web.psn.model.third.user;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 第三方登录
 * 
 * @author Scy
 * 
 */
@Entity
@Table(name = "SYS_THIRD_USER")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SysThirdUser implements Serializable {

  private static final long serialVersionUID = -2509428340463418430L;

  public static final Integer TYPE_QQ = 1; // QQ关联类型
  public static final Integer TYPE_WEIBO = 2; // 新浪微博类型
  public static final Integer TYPE_WECHAT = 3; // 微信类型

  // 主键
  @Id
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "SEQ_SYS_THIRD_USER", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long Id;

  // scm系统人员ID
  @Column(name = "PSN_ID")
  private Long psnId;

  // 关联的类型
  @Column(name = "TYPE")
  private Integer type;

  // 第三方Id
  @Column(name = "THIRD_ID")
  private String thirdId;

  @Column(name = "CREATE_DATE")
  private Date createDate;

  // 第三方帐号的昵称
  @Column(name = "NICK_NAME")
  private String nickName;

  public SysThirdUser() {
    super();
  }

  public SysThirdUser(Long psnId, Integer type, String thirdId, Date createDate) {
    super();
    this.psnId = psnId;
    this.type = type;
    this.thirdId = thirdId;
    this.createDate = createDate;
  }

  public SysThirdUser(Long psnId, Integer type, String thirdId, Date createDate, String nickName) {
    super();
    this.psnId = psnId;
    this.type = type;
    this.thirdId = thirdId;
    this.createDate = createDate;
    this.nickName = nickName;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }

  public Long getId() {
    return Id;
  }

  public void setId(Long id) {
    Id = id;
  }

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  public String getThirdId() {
    return thirdId;
  }

  public void setThirdId(String thirdId) {
    this.thirdId = thirdId;
  }

  public static long getSerialversionuid() {
    return serialVersionUID;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

}
