package com.smate.center.batch.model.sns.wechat;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 微信群发消息历史表，V_WECHAT_MESSAGE_PUBLIC表中的已处理和重复的信息
 * 
 * @since 6.0.1
 */
@Entity
@Table(name = "V_BATCH_WECHAT_HISTORY_PUBLIC")
public class WeChatMessageHistoryPublic implements Serializable {



  /**
   * 
   */
  private static final long serialVersionUID = -3239065499090948591L;

  @Id
  @Column(name = "ID")
  private Long id; // 主键id，与V_WECHAT_MESSAGE_PUBLIC中Id对应

  @Column(name = "TOKEN")
  private String token;// 第三方系统id

  @Column(name = "OPENID")
  private Long openId;// 第三方用户来源标识

  @Column(name = "PSN_ID")
  private Long psnId; // scm系统用户Id

  @Column(name = "CONTENT")
  private String content;// 消息内容

  @Column(name = "CONTENT_MD5")
  private String contentMd5; // 消息内容的Md5码，用于查重

  @Column(name = "STATUS")
  private Integer status;// 处理状态，1已成功处理，0未处理

  @Column(name = "DUPLICATE_COUNTS")
  private Long duplicateCounts;// 重复消息次数

  @Column(name = "CREATE_TIME")
  private Date createTime;// 创建时间

  @Column(name = "UPDATE_TIME")
  private Date updateTime;

  public WeChatMessageHistoryPublic() {
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

  public Long getPsnId() {
    return psnId;
  }

  public void setPsnId(Long psnId) {
    this.psnId = psnId;
  }

  public String getContentMd5() {
    return contentMd5;
  }

  public void setContentMd5(String contentMd5) {
    this.contentMd5 = contentMd5;
  }

  public Long getDuplicateCounts() {
    return duplicateCounts;
  }

  public void setDuplicateCounts(Long duplicateCounts) {
    this.duplicateCounts = duplicateCounts;
  }

  public Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

}
