package com.smate.center.open.model.wechat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * 微信个人消息表
 * 
 * @since 6.0.1
 */
@Entity
@Table(name = "V_WECHAT_MESSAGE_PSN")
public class WeChatMessagePsn implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -4052083943331753103L;

  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_WECHAT_MESSAGE_PSN", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id; // 主键id

  @Column(name = "TOKEN")
  private String token;// 第三方系统id

  @Column(name = "OPENID")
  private Long openId;// 第三方用户来源标识

  @Column(name = "CONTENT")
  private String content;// 消息内容

  @Column(name = "STATUS")
  private Integer status;// 处理状态，1已处理，0未处理

  @Column(name = "CREATE_TIME")
  private Date createTime;// 创建时间

  public WeChatMessagePsn() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public Long getOpenId() {
    return openId;
  }

  public void setOpenId(Long openId) {
    this.openId = openId;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }


}
