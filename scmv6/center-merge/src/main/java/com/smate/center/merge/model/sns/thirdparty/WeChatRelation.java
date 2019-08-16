package com.smate.center.merge.model.sns.thirdparty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 微信openid与scm-openid关系表.
 * 
 * @since 6.0.1
 */
@Entity
@Table(name = "V_WECHAT_RELATION")
public class WeChatRelation implements Serializable {
  private static final long serialVersionUID = 896812447028492075L;
  @Id
  @Column(name = "ID")
  @SequenceGenerator(name = "SEQ_STORE", sequenceName = "V_SEQ_WECHAT_RELATION", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
  private Long id;// 主键id
  @Column(name = "WECHAT_OPENID")
  private String webChatOpenId;// 微信openid
  @Column(name = "WECHAT_UNIONID")
  private String weChatUnionId;// 微信unionid
  @Column(name = "SMATE_OPENID")
  private Long smateOpenId;// 科研之友openid
  @Column(name = "CREATE_TIME")
  private Date createTime;// 创建时间
  @Column(name = "BIND_TYPE")
  private Integer bindType;// 微信绑定方式 0-微信端 1-PC端 PC端通过开放平台，2=ios,openId默认0
  @Column(name = "NICK_NAME")
  private String nickName;// 微信昵称

  public WeChatRelation() {
    super();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWebChatOpenId() {
    return webChatOpenId;
  }

  public void setWebChatOpenId(String webChatOpenId) {
    this.webChatOpenId = webChatOpenId;
  }

  public Long getSmateOpenId() {
    return smateOpenId;
  }

  public void setSmateOpenId(Long smateOpenId) {
    this.smateOpenId = smateOpenId;
  }

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public String getWeChatUnionId() {
    return weChatUnionId;
  }

  public void setWeChatUnionId(String weChatUnionId) {
    this.weChatUnionId = weChatUnionId;
  }

  public Integer getBindType() {
    return bindType;
  }

  public void setBindType(Integer bindType) {
    this.bindType = bindType;
  }

  public String getNickName() {
    return nickName;
  }

  public void setNickName(String nickName) {
    this.nickName = nickName;
  }
}
