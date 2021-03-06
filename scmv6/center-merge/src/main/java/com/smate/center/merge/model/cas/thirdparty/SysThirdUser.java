package com.smate.center.merge.model.cas.thirdparty;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 第三方登录.
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
  private Long id;
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
  @Column(name = "UNION_ID")
  private String unionId;

  public SysThirdUser() {
    super();
  }

  public String getUnionId() {
    return unionId;
  }

  public void setUnionId(String unionId) {
    this.unionId = unionId;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
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
